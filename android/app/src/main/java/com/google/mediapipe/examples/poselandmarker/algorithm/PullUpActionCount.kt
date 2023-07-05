package com.google.mediapipe.examples.poselandmarker.algorithm


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





}