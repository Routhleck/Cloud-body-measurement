package com.google.mediapipe.examples.poselandmarker.algorithm

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant

class RepetitionCounter(class_name1: String, class_name2: String, enter_threshold: Double = 6.0, exit_threshold: Double = 4.0) {
    private val _class_name1 = class_name1
    private val _class_name2 = class_name2

    // 如果姿势通过了给定的阈值，那么我们就进入该动作的计数
    private val _enter_threshold = enter_threshold
    private val _exit_threshold = exit_threshold

    // 是否处于给定的姿势
    private var _pose1_entered = false
    private var _pose2_entered = false

    // 时间
    @RequiresApi(Build.VERSION_CODES.O)
    private var _time= Instant.now()

    // 计数器
    private var _n_repeats = 0

    public fun get_n_repeats(): Int {
        return _n_repeats
    }

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(pose_classification: Map<String, Double>): Int {
        /*
        计算给定帧之前发生的重复次数
        我们使用两个阈值。首先，需要从较高的位置上方进入姿势，然后您需要从较低的位置下方退出。
        阈值之间的差异使其对预测抖动稳定（如果只有一个阈值，则会导致错误计数）。
        # 参数：
        #   pose_classification：当前帧上的姿势分类字典
        #         Sample:
        #         {
        #             'squat_down': 8.3,
        #             'squat_up': 1.7,
        #         }
         */

        // 获取姿势置信度
        var pose_confidence1 = 0.0
        var pose_confidence2 = 0.0
        if (_class_name1 in pose_classification) {
            pose_confidence1 = pose_classification[_class_name1]!!
        }
        if (_class_name2 in pose_classification) {
            pose_confidence2 = pose_classification[_class_name2]!!
        }

        // 若两者enter都为false, 刚开始阶段
        if (!_pose1_entered && !_pose2_entered) {
            if (pose_confidence2 > _enter_threshold) {
                _pose2_entered = true
            }
        }

        // 其中一个enter为true, 说明已经进入了某个阶段
        if (_pose1_entered) {
            if (pose_confidence1 < _exit_threshold) {
                _pose1_entered = false
                _pose2_entered = true
                _n_repeats += 1
            }
            // 如果超过2s没有检测到pose1, 那么将pose1_entered置为false, _pose2_entered置为true
            if (Duration.between(_time, Instant.now()).seconds > 2) {
                _pose1_entered = false
                _pose2_entered = true
            }
        }

        if (_pose2_entered) {
            if (pose_confidence2 < _exit_threshold) {
                _pose1_entered = true
                _pose2_entered = false
                _time = Instant.now()
            }
        }

        return _n_repeats
    }
}