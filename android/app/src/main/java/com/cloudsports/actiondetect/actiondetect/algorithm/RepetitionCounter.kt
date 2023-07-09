package com.cloudsports.actiondetect.actiondetect.algorithm

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant

class RepetitionCounter(
    private val className1: String,
    private val className2: String,
    private val enterThreshold: Double = 6.0
) {
    private var pose1Entered = false
    private var pose2Entered = false
    private var nRepeats = 0
    private var isStart = false

    @RequiresApi(Build.VERSION_CODES.O)
    private var time = Instant.now()

    val repeats: Int
        get() = nRepeats

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(poseClassification: Map<String, Double>): Int {
        val poseConfidence1 = poseClassification[className1] ?: 0.0
        val poseConfidence2 = poseClassification[className2] ?: 0.0

        // If neither pose has been entered yet, check if we're entering pose2
        if (!pose1Entered && !pose2Entered && !isStart) {
            if (poseConfidence2 > enterThreshold) {
                pose2Entered = true
                isStart = true
            }
        }

        // If pose1 has been entered
        if (pose1Entered) {
            if (poseConfidence1 < poseConfidence2) {
                pose1Entered = false
                pose2Entered = true
                nRepeats += 1
            }
            // If pose1 hasn't been detected for 2 seconds, switch to pose2
            if (Duration.between(time, Instant.now()).seconds > 2) {
                pose1Entered = false
                pose2Entered = true
            }
        }

        // If pose2 has been entered
        if (pose2Entered) {
            if (poseConfidence2 < poseConfidence1) {
                pose2Entered = false
                pose1Entered = true
                time = Instant.now()
            }
        }

        return nRepeats
    }
}
