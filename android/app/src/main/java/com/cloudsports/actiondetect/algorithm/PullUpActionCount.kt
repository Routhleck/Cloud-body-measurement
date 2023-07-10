package com.cloudsports.actiondetect.algorithm

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


class PullUpActionCount(context: Context?) {

    private val className = "pullUp"
    private val className1 = "pullUps_up"
    private val className2 = "pullUps_down"

    private val videoFps = 24
    private val videoWidth = 640
    private val videoHeight = 480

    // folder的路径在assets/samples文件夹下
    private val poseSamplesFolder = "samples/pullUps"

    // 初始化pose_embedder
    private val poseEmbedder = PoseEmbedder()

    // 初始化pose_classifier
    private val poseClassifier = PoseClassifier(
        context = context,
        poseSamplesFolder = poseSamplesFolder,
        poseEmbedder = poseEmbedder,
        topNByMaxDistance = 30,
        topNByMeanDistance = 10
    )

    // 初始化 EMA smoothing
    private val emaSmoothing = EMADictSmoothing(windowSize = 10, alpha = 0.2)

    // 初始化计数器
    private val repetitionCounter = RepetitionCounter(
        className1 = className1,
        className2 = className2,
        enterThreshold = 8.0,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(pose_landmarks: Array<DoubleArray>?, frame_height: Int, frame_width: Int): Int {
        if (pose_landmarks != null) {
            val poseLandmarks = pose_landmarks.map { lmk ->
                doubleArrayOf(lmk[0] * frame_width, lmk[1] * frame_height, lmk[2] * frame_width)
            }.toTypedArray()

            // 用pose_classifier来预测动作
            val poseClassification = poseClassifier(poseLandmarks)
            Log.d("poseClassification", poseClassification.toString())

            // 用EMA smoothing来平滑预测结果
            val smoothedPoseClassification = emaSmoothing(poseClassification)

            // 用repetition_counter来计算重复次数
            repetitionCounter(smoothedPoseClassification)

        }
        return repetitionCounter.repeats
    }





}