package com.cloudsports.actiondetect.algorithm

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


class MainActionCount(context: Context?, name: String) {

    private lateinit var className1: String
    private lateinit var className2: String

    // folder的路径在assets/samples文件夹下
    private lateinit var poseSamplesFolder: String

    // 初始化pose_embedder
    private val poseEmbedder = PoseEmbedder()

    // 初始化pose_classifier
    private var poseClassifier: PoseClassifier

    // 初始化 EMA smoothing
    private var emaSmoothing: EMADictSmoothing

    // 初始化计数器
    private var repetitionCounter: RepetitionCounter

    init {
        when(name) {
            "pullUp" -> {
                className1 = "pullUp_up"
                className2 = "pullUp_down"
                poseSamplesFolder = "samples/pullUp"
            }
            "pushUp" -> {
                className1 = "pushUp_down"
                className2 = "pushUp_up"
                poseSamplesFolder = "samples/pushUp"
            }
            "squat" -> {
                className1 = "squat_down"
                className2 = "squat_up"
                poseSamplesFolder = "samples/squat"
            }
            "sitUp" -> {
                className1 = "sitUp_up"
                className2 = "sitUp_down"
                poseSamplesFolder = "samples/sitUp"
            }
        }
        poseClassifier = PoseClassifier(
            context = context,
            poseSamplesFolder = poseSamplesFolder,
            poseEmbedder = poseEmbedder,
            topNByMaxDistance = 30,
            topNByMeanDistance = 10)

        emaSmoothing = EMADictSmoothing(windowSize = 10, alpha = 0.2)

        repetitionCounter = RepetitionCounter(
            className1 = className1,
            className2 = className2,
            enterThreshold = 8.0,
            )
    }



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