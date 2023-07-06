package com.google.mediapipe.examples.poselandmarker.algorithm

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import org.apache.commons.lang3.ObjectUtils.Null
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j


class PullUpActionCount {

    private val class_name = "pullUp"
    private val class_name1 = "pullUps_up"
    private val class_name2 = "pullUps_down"

    private val video_fps = 24
    private val video_width = 640
    private val video_height = 480

    // folder的路径在PoseSample/{class_name}文件夹下
    private val pose_samples_folder = "PoseSample/" + class_name.toString()

    // 初始化pose_embedder
    private val pose_embedder = PoseEmbedder()

    // 初始化pose_classifier
    private val pose_classifier = PoseClassifier(
        pose_samples_folder = pose_samples_folder,
        pose_embedder = pose_embedder,
        top_n_by_max_distance = 30,
        top_n_by_mean_distance = 10
    )

    // 初始化 EMA smoothing
    private val ema_smoothing = EMADictSmoothing(windowSize = 10, alpha = 0.2)

    // 初始化计数器
    private val repetition_counter = RepetitionCounter(
        class_name1 = class_name1,
        class_name2 = class_name2,
        enter_threshold = 8.3,
        exit_threshold = 7.5,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(pose_landmarks: Array<DoubleArray>, frame_height: Int, frame_width: Int): Int {
        if (pose_landmarks != null) {
            val pose_landmarks = pose_landmarks.map { lmk ->
                doubleArrayOf(lmk[0] * frame_width, lmk[1] * frame_height, lmk[2] * frame_width)
            }.toTypedArray()

            // 用pose_classifier来预测动作
            val pose_classification = pose_classifier(pose_landmarks)

            // 用EMA smoothing来平滑预测结果
            val smoothed_pose_classification = ema_smoothing(pose_classification)

            // 用repetition_counter来计算重复次数
            val repetition_count = repetition_counter(smoothed_pose_classification)

        }
        return repetition_counter.get_n_repeats()
    }





}