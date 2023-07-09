package com.cloudsports.actiondetect.actiondetect.algorithm

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.cloudsports.actiondetect.actiondetect.algorithm.Util.ArrayOperation
import java.io.BufferedReader
import java.io.InputStream
import kotlin.math.min

class PoseClassifier(
    context: Context?,
    poseSamplesFolder: String,
    poseEmbedder: PoseEmbedder,
    fileExtension: String = "csv",
    nLandmarks: Int = 33,
    nDimensions: Int = 3,
    topNByMaxDistance: Int = 30,
    topNByMeanDistance: Int = 10,
    axesWeights: List<Double> = listOf(1.0, 1.0, 0.2)
) {
    
    private val _poseEmbedder = poseEmbedder
    private val _nLandmarks = nLandmarks
    private val _nDimensions = nDimensions
    // KNN算法中的K
    private val _topNByMaxDistance = topNByMaxDistance
    private val _topNByMeanDistance = topNByMeanDistance
    
    // 加载所有的样本
    @RequiresApi(Build.VERSION_CODES.O)
    private val _poseSamples = loadPoseSamples(
        context = context,
        poseSamplesFolder,
        fileExtension,
        _nLandmarks,
        _nDimensions,
        _poseEmbedder
    )

    private val _axesWeights = axesWeights

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadPoseSamples(
        context: Context?,
        pose_samples_folder: String,
        file_extension: String,
        n_landmarks: Int,
        n_dimensions: Int,
        pose_embedder: PoseEmbedder
    ): List<PoseSample> {
        /*
        Loads pose samples from a given folder.

        Required folder structure:
          neutral_standing.csv
          pushups_down.csv
          pushups_up.csv
          squats_down.csv
          ...

        Required CSV structure:
          sample_00001,x1,y1,z1,x2,y2,z2,....
          sample_00002,x1,y1,z1,x2,y2,z2,....
          ...
         */
        // 文件夹中的每个文件代表一个姿势类
        val assetManager = context?.assets
        val fileNames = assetManager?.list(pose_samples_folder)
            ?.filter { it.endsWith(file_extension) }
            ?: listOf()


        val poseSamples = mutableListOf<PoseSample>()
        for (fileName in fileNames) {
            val className = fileName.removeSuffix(".$file_extension")

            val inputStream: InputStream = assetManager?.open("$pose_samples_folder/$fileName")!!
            val reader = BufferedReader(inputStream.reader())

            var line: String? = reader.readLine()
            while (line != null) {
                val row = line.split(",")

                check(row.size == n_landmarks * n_dimensions + 1) {
                    "Expected $n_landmarks landmarks with $n_dimensions dimensions each, " +
                            "but got ${row.size - 1} values."
                }

                val landmarks = Array(n_landmarks) { i ->
                    DoubleArray(n_dimensions) { j ->
                        row[i * n_dimensions + j + 1].toDouble()
                    }
                }

                val poseSample = PoseSample(
                    name = row[0],
                    landmarks = landmarks,
                    className = className,
                    embedding = pose_embedder(landmarks)
                )

                poseSamples.add(poseSample)

                line = reader.readLine()
            }

            reader.close()
        }
        Toast.makeText(context, "samples加载完成," +
                "\nposeSamples.size = ${poseSamples.size}", Toast.LENGTH_SHORT).show()
        return poseSamples
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun findPoseSampleOutliers(): List<PoseSampleOutlier> {
        // 针对整个数据库对每个样本进行分类
        
        // 找出目标姿势中的异常值
        val outliers = mutableListOf<PoseSampleOutlier>()
        for (sample in _poseSamples) {
            // 为目标找到最近的姿势
            val poseLandmarks = sample.landmarks
            val poseClassification = invoke(poseLandmarks)

            // 实现类似python中的class_names = [class_name for class_name, count in pose_classification.items() if count == max(pose_classification.values())]
            val counts = poseClassification.values
            val maxCount = counts.maxOrNull()
            val classNames = mutableMapOf<String, Int>()
            for (item in poseClassification) {
                if (item.value == maxCount) {
                    classNames[item.key] = item.value
                }
            }
            if (sample.className !in classNames.keys || classNames.size != 1) {
                // 如果最近的姿势不是目标姿势，或者最近的姿势不止一个，那么这个样本就是异常值
                outliers.add(PoseSampleOutlier(sample, classNames, poseClassification))
            }
        }

        return outliers.toList()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(pose_landmarks: Array<DoubleArray>): Map<String, Int> {
        check(pose_landmarks.size == _nLandmarks && pose_landmarks.all { it.size == _nDimensions }) {
            "Expected $_nLandmarks landmarks with $_nDimensions dimensions each."
        }

        val poseEmbedding = _poseEmbedder(pose_landmarks)
        val flippedPoseEmbedding =
            _poseEmbedder(pose_landmarks.map { doubleArrayOf(-it[0], it[1], it[2]) }.toTypedArray())

        // Convert _axes_weights to INDArray
        val axesWeightsArray = _axesWeights.toDoubleArray()

        /*
        按最大距离过滤。
        这有助于去除异常值——与给定的姿势几乎相同，但一个关节弯曲到另一个方向，实际上代表不同的姿势类别。
         */
        val maxDistHeap = mutableMapOf<Double, Int>()
        for (i in _poseSamples.indices) {
            val maxDist = min(
                ArrayOperation.getMaxOfArrayDoubleArray(
                    ArrayOperation.multiplyArrayDoubleArrayByDimensionWeight(
                        ArrayOperation.absArrayDoubleArray(
                            ArrayOperation.subArrayDoubleArrayByArrayDoubleArray(
                                poseEmbedding,
                                _poseSamples[i].embedding
                            )
                        ),
                        axesWeightsArray
                    )
                ),
                ArrayOperation.getMaxOfArrayDoubleArray(
                    ArrayOperation.multiplyArrayDoubleArrayByDimensionWeight(
                        ArrayOperation.absArrayDoubleArray(
                            ArrayOperation.subArrayDoubleArrayByArrayDoubleArray(
                                flippedPoseEmbedding,
                                _poseSamples[i].embedding
                            )
                        ),
                        axesWeightsArray
                    )
                )
            )
            maxDistHeap[maxDist] = i
        }

        // 按maxDistHeap的maxDist从小到大排序,注意maxDist是key
        val sortedMaxDistHeap = maxDistHeap.toList().sortedBy { (key, _) -> key }.toMap()
        // 截取前topNByMaxDist个
        val topNByMaxDist = sortedMaxDistHeap.toList().subList(0, _topNByMaxDistance)


        /*
        按平均距离过滤。
        去除异常值后，我们可以通过平均距离找到最近的姿势。
         */
        val meanDistHeap = mutableMapOf<Double, Int>()
        for (i in topNByMaxDist.toMap().values) {
            val meanDist = min(
                ArrayOperation.getMeanOfArrayDoubleArray(
                    ArrayOperation.multiplyArrayDoubleArrayByDimensionWeight(
                        ArrayOperation.absArrayDoubleArray(
                            ArrayOperation.subArrayDoubleArrayByArrayDoubleArray(
                                poseEmbedding,
                                _poseSamples[i].embedding
                            )
                        ),
                        axesWeightsArray
                    )
                ),
                ArrayOperation.getMeanOfArrayDoubleArray(
                    ArrayOperation.multiplyArrayDoubleArrayByDimensionWeight(
                        ArrayOperation.absArrayDoubleArray(
                            ArrayOperation.subArrayDoubleArrayByArrayDoubleArray(
                                flippedPoseEmbedding,
                                _poseSamples[i].embedding
                            )
                        ),
                        axesWeightsArray
                    )
                )
            )
            meanDistHeap[meanDist] = i
        }

        // 按mean_dist_heap的meanDist从小到大排序,注意meanDist是key
        val sortedMeanDistHeap = meanDistHeap.toList().sortedBy { (key, _) -> key }.toMap()
        // 截取前topNByMeanDist个
        val topNByMeanDist = sortedMeanDistHeap.toList().subList(0, _topNByMeanDistance)

        // Collect results into map: (class_name -> n_samples)
        // 根据topNByMeanDist中的index找到对应的className并统计每个className的个数, 最后返回Map<String, Int>
        val poseClassification = mutableMapOf<String, Int>()
        for (i in topNByMeanDist.indices) {
            val className = _poseSamples[topNByMeanDist[i].second].className
            if (className in poseClassification.keys) {
                poseClassification[className] = poseClassification[className]!! + 1
            } else {
                poseClassification[className] = 1
            }
        }

        return poseClassification
    }

}