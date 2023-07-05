package com.google.mediapipe.examples.poselandmarker.algorithm

import PoseEmbedder
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms
import java.io.File
import java.nio.file.Paths
import kotlin.math.abs

class PoseClassifier(
    pose_samples_folder: String,
    pose_embedder: PoseEmbedder,
    file_extension:String ="csv",
    file_separator:String = ",",
    n_landmarks:Int = 33,
    n_dimensions:Int = 3,
    top_n_by_max_distance:Int = 30,
    top_n_by_mean_distance:Int = 10,
    axes_weights: List<Double> = listOf<Double>(1.0, 1.0, 0.2)) {
    
    private val _pose_embedder = pose_embedder
    private val _n_landmarks = n_landmarks
    private val _n_dimensions = n_dimensions
    // KNN算法中的K
    private val _top_n_by_max_distance = top_n_by_max_distance
    private val _top_n_by_mean_distance = top_n_by_mean_distance
    
    // 加载所有的样本
    @RequiresApi(Build.VERSION_CODES.O)
    private val _pose_samples = _load_pose_samples(pose_samples_folder,
        file_extension,
        file_separator,
        _n_landmarks,
        _n_dimensions,
        _pose_embedder)

    private val _axes_weights = axes_weights

    @RequiresApi(Build.VERSION_CODES.O)
    private fun _load_pose_samples(pose_samples_folder: String,
                                   file_extension: String,
                                   file_separator: String,
                                   n_landmarks: Int,
                                   n_dimensions: Int,
                                   pose_embedder: PoseEmbedder): List<PoseSample> {
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
        val poseSamplesFolder = File(pose_samples_folder)
        val file_names = poseSamplesFolder.listFiles { _, name -> name.endsWith(file_extension) }
            ?.map { it.name } ?: listOf()

        var pose_samples = mutableListOf<PoseSample>()
        for (file_name in file_names) {
            val class_name = file_name.removeSuffix(".$file_extension")

            val csvFile = File(Paths.get(pose_samples_folder, file_name).toString())
            val rows = csvReader().readAll(csvFile)

            for (row in rows) {
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
                    landmarks = landmarks.toList(),
                    class_name = class_name,
                    embedding = pose_embedder.call(landmarks)
                )

                pose_samples.add(poseSample)
            }
        }
        return pose_samples
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun find_pose_sample_outliers() {
        // 针对整个数据库对每个样本进行分类
        
        // 找出目标姿势中的异常值
        var outliers = mutableListOf<PoseSampleOutlier>()
        for (sample in _pose_samples) {
            // 为目标找到最近的姿势
            val pose_landmarks = sample.landmarks.toTypedArray()
            val pose_classification = call(pose_landmarks)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun call(pose_landmarks: Array<DoubleArray>): Map<String, Int> {
        check(pose_landmarks.size == _n_landmarks && pose_landmarks.all { it.size == _n_dimensions }) {
            "Expected $_n_landmarks landmarks with $_n_dimensions dimensions each."
        }

        val pose_embedding: INDArray = _pose_embedder.call(pose_landmarks)
        val flipped_pose_embedding: INDArray = _pose_embedder.call(pose_landmarks.map{ doubleArrayOf(-it[0], it[1], it[2])}.toTypedArray())

        // Convert _axes_weights to INDArray
        val axes_weights_array = Nd4j.create(_axes_weights.toDoubleArray())

        /*
        按最大距离过滤。
        这有助于去除异常值——与给定的姿势几乎相同，但一个关节弯曲到另一个方向，实际上代表不同的姿势类别。
         */
        val max_dist_heap = _pose_samples.mapIndexed { index, sample ->
            val max_dist = maxOf(
                Transforms.abs(sample.embedding.sub(pose_embedding)).mul(axes_weights_array).maxNumber().toDouble(),
                Transforms.abs(sample.embedding.sub(flipped_pose_embedding)).mul(axes_weights_array).maxNumber().toDouble()
            )
            max_dist to index
        }.sortedBy { it.first }.take(_top_n_by_max_distance)

        /*
        按平均距离过滤。
        去除异常值后，我们可以通过平均距离找到最近的姿势。
         */
        val mean_dist_heap = max_dist_heap.map { (_, sample_idx) ->
            val sample = _pose_samples[sample_idx]
            val mean_dist = minOf(
                Transforms.abs(sample.embedding.sub(pose_embedding)).mul(axes_weights_array).meanNumber().toDouble(),
                Transforms.abs(sample.embedding.sub(flipped_pose_embedding)).mul(axes_weights_array).meanNumber().toDouble()
            )
            mean_dist to sample_idx
        }.sortedBy { it.first }.take(_top_n_by_mean_distance)

        // Collect results into map: (class_name -> n_samples)
        val class_names = mean_dist_heap.map { (_, sample_idx) -> _pose_samples[sample_idx].class_name }
        val result = class_names.groupingBy { it }.eachCount()

        return result
    }

}