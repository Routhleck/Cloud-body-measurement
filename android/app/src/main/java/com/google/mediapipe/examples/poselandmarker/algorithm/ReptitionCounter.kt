package com.google.mediapipe.examples.poselandmarker.algorithm

class ReptitionCounter(class_name1: String, class_name2: String, enter_threshold: Double = 6.0, exit_threshold: Double = 4.0) {
    private val _class_name1 = class_name1
    private val _class_name2 = class_name2

    // 如果姿势通过了给定的阈值，那么我们就进入该动作的计数
    private val _enter_threshold = enter_threshold
    private val _exit_threshold = exit_threshold

    // 是否处于给定的姿势
    private var _pose1_entered = false
    private var _pose2_entered = false

    // 计数器
    private var _n_repeats = 0


}