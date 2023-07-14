package com.cloudsports.actiondetect.algorithm
import com.cloudsports.actiondetect.algorithm.Util.ArrayOperation
import com.cloudsports.actiondetect.algorithm.Util.MyDoubleArray
import kotlin.math.max

class PoseEmbedder(torsoSizeMultiplier: Double = 2.5) {
    private val _torsoSizeMultiplier = torsoSizeMultiplier

    private val _landmarkNames = listOf(
        "nose",
        "left_eye_inner", "left_eye", "left_eye_outer",
        "right_eye_inner", "right_eye", "right_eye_outer",
        "left_ear", "right_ear",
        "mouth_left", "mouth_right",
        "left_shoulder", "right_shoulder",
        "left_elbow", "right_elbow",
        "left_wrist", "right_wrist",
        "left_pinky_1", "right_pinky_1",
        "left_index_1", "right_index_1",
        "left_thumb_2", "right_thumb_2",
        "left_hip", "right_hip",
        "left_knee", "right_knee",
        "left_ankle", "right_ankle",
        "left_heel", "right_heel",
        "left_foot_index", "right_foot_index",
    )


    private fun getPoseCenter(landmarks: Array<DoubleArray>): DoubleArray {
        /*
        Calculates pose center as point between hips.将姿势中心计算为臀部之间的点。"""
        left_hip = landmarks[self._landmark_names.index('left_hip')]
        right_hip = landmarks[self._landmark_names.index('right_hip')]
        center = (left_hip + right_hip) * 0.5
        */
        val leftThumb2 = landmarks[_landmarkNames.indexOf("left_thumb_2")]
        val rightThumb2 = landmarks[_landmarkNames.indexOf("right_thumb_2")]
        return ArrayOperation.averageDoubleArray(leftThumb2, rightThumb2)
    }

    private fun getPoseSize(landmarks: Array<DoubleArray>, torsoSizeMultiplier: Double): Double {
        /*
        计算姿势大小。
        它是下面两个值的最大值:
          * 躯干大小乘以`torso_size_multiplier`
          * 从姿势中心到任何姿势地标的最大距离
        */

        val landmarks2d = ArrayOperation.getLandmarksByDimensionRange(landmarks, 0..1)

        // 两手中心
        val leftHand = landmarks[_landmarkNames.indexOf("left_wrist")]
        val rightHand = landmarks[_landmarkNames.indexOf("right_wrist")]
        val hands = ArrayOperation.averageDoubleArray(leftHand, rightHand)

        // 两臀中心
        val leftHip = landmarks2d[_landmarkNames.indexOf("left_hip")]
        val rightHip = landmarks2d[_landmarkNames.indexOf("right_hip")]
        val hips = ArrayOperation.averageDoubleArray(leftHip, rightHip)

        // 躯干尺寸作为最小的身体尺寸
        val torsoSize = ArrayOperation.getNorm(MyDoubleArray(hands).sub(MyDoubleArray(hips)))

        // 到姿势中心的最大距离
        val poseCenter = getPoseCenter(landmarks)
        val diff = landmarks2d.map { ArrayOperation.getNorm(MyDoubleArray(it).sub(MyDoubleArray(poseCenter))) }
        val maxDist = diff.maxOrNull() ?: 0.0

        return max((torsoSize * torsoSizeMultiplier), maxDist)
    }

    private fun normalizePoseLandmarks(landmarks: Array<DoubleArray>): Array<DoubleArray> {
        /*
        将姿势地标归一化为单位尺寸。
        1. 将姿势中心移动到原点。
        2. 将姿势缩放到单位大小。
        3. 将姿势移动到原始中心。
        */
        var landmarks3d = landmarks

        // 1. 将姿势中心移动到原点。
        val poseCenter = getPoseCenter(landmarks)
        landmarks3d = ArrayOperation.subArrayDoubleArrayByDoubleArray(landmarks3d, poseCenter)

        // 2. 将姿势缩放到单位大小。
        val poseSize = getPoseSize(landmarks, _torsoSizeMultiplier)
        landmarks3d = ArrayOperation.divArrayDoubleArrayByDouble(landmarks3d, poseSize)

        // Multiplication by 100 is not required, but makes it eaasier to debug
        landmarks3d = ArrayOperation.multiplyArrayDoubleArrayByDouble(landmarks3d, 100.0)

        return landmarks3d
    }

    private fun getPoseDistanceEmbedding(landmarks: Array<DoubleArray>): Array<DoubleArray> {
        /*
        将姿势landmarks转换为 3D embedding.
        我们使用几个成对的 3D 距离来形成姿势embedding。 所有距离都包括带符号的 X 和 Y 分量。
        我们使用不同类型的对来覆盖不同的姿势类别。
        Args:
          landmarks - 3D landmarks的NumPy数组，形状为 (N, 3)。

        Result:
          形状为 (M, 3) 的pose embedding的INDArray，其中 `M` 是成对距离的数量。
        */
        val embeddingList: MutableList<DoubleArray> = mutableListOf()

        // one joint.
        val jointDistance = getDistance(
            getAverageByNames(landmarks, "left_hip", "right_hip"),
            getAverageByNames(landmarks, "left_thumb_2", "right_thumb_2")
        )
        embeddingList.add(jointDistance)

        // 左眼与左肩的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_eye", "left_shoulder"))
        // 右眼与右肩的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_eye", "right_shoulder"))
        // 左眼与左手的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_eye", "left_thumb_2"))
        // 右眼与右手的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_eye", "right_thumb_2"))
        // 鼻子与左肩的距离。
        embeddingList.add(getDistanceByNames(landmarks, "nose", "left_shoulder"))
        // 鼻子与右肩的距离。
        embeddingList.add(getDistanceByNames(landmarks, "nose", "right_shoulder"))
        // 鼻子与左手的距离。
        embeddingList.add(getDistanceByNames(landmarks, "nose", "left_thumb_2"))
        // 鼻子与右手的距离。
        embeddingList.add(getDistanceByNames(landmarks, "nose", "right_thumb_2"))
        // 左嘴与左肩的距离。
        embeddingList.add(getDistanceByNames(landmarks, "mouth_left", "left_shoulder"))
        // 右嘴与右肩的距离。
        embeddingList.add(getDistanceByNames(landmarks, "mouth_right", "right_shoulder"))
        // 左嘴与左手的距离。
        embeddingList.add(getDistanceByNames(landmarks, "mouth_left", "left_thumb_2"))
        // 右嘴与右手的距离。
        embeddingList.add(getDistanceByNames(landmarks, "mouth_right", "right_thumb_2"))
        // 左肩膀与左肘的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_shoulder", "left_elbow"))
        // 右肩膀与右肘的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_shoulder", "right_elbow"))
        // 左肘和左手腕的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_elbow", "left_wrist"))
        // 右肘和右手腕的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_elbow", "right_wrist"))
        // 左髋关节与左膝盖的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_hip", "left_knee"))
        // 右髋关节与右膝盖的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_hip", "right_knee"))
        // 左膝盖和左踝的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_knee", "left_ankle"))
        // 右膝盖和右踝的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_knee", "right_ankle"))
        // 左肩膀与左手腕的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_shoulder", "left_wrist"))
        // 右肩膀与右手腕的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_shoulder", "right_wrist"))
        // 左髋关节与左踝的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_hip", "left_ankle"))
        // 右髋关节与右踝的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_hip", "right_ankle"))
        // 左髋关节与左肘的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_hip", "left_elbow"))
        // 右髋关节与右肘的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_hip", "right_elbow"))
        // 左肩与左脚踝的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_shoulder", "left_ankle"))
        // 右肩与右脚踝的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_shoulder", "right_ankle"))
        // 左髋关节与左手腕的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_hip", "left_wrist"))
        // 右髋关节与右手腕的距离。
        embeddingList.add(getDistanceByNames(landmarks, "right_hip", "right_wrist"))
        // 左肘与右肘的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_elbow", "right_elbow"))
        // 左膝盖与右膝盖的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_knee", "right_knee"))
        // 左脚踝与右脚踝的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_ankle", "right_ankle"))
        // 左手腕与右手腕的距离。
        embeddingList.add(getDistanceByNames(landmarks, "left_wrist", "right_wrist"))

        return ArrayOperation.vstack(embeddingList)
    }

    private fun getAverageByNames(landmarks: Array<DoubleArray>, name_from: String, name_to: String): DoubleArray {
        /*
        计算两个姿势地标之间的平均值。
        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).
          name_from - Name of the first landmark.
          name_to - Name of the second landmark.

        Result:
          Numpy array with the average of the two landmarks.
        */

        val lmkFrom = landmarks[_landmarkNames.indexOf(name_from)]
        val lmkTo = landmarks[_landmarkNames.indexOf(name_to)]

        return ArrayOperation.averageDoubleArray(lmkFrom, lmkTo)
    }

    private fun getDistanceByNames(landmarks: Array<DoubleArray>, name_from: String, name_to: String): DoubleArray {
        /*
        计算两个姿势地标之间的距离。
        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).
          name_from - Name of the first landmark.
          name_to - Name of the second landmark.

        Result:
          Numpy array with the distance between the two landmarks.
        */
        val lmkFrom = landmarks[_landmarkNames.indexOf(name_from)]
        val lmkTo = landmarks[_landmarkNames.indexOf(name_to)]

        return getDistance(lmkFrom, lmkTo)
    }

    private fun getDistance(lmk_from: DoubleArray, lmk_to: DoubleArray): DoubleArray {
        /*
        计算两个3D点之间的距离。
        */
        return MyDoubleArray(lmk_to).getDistance(MyDoubleArray(lmk_from))
    }

    operator fun invoke(landmarks: Array<DoubleArray>): Array<DoubleArray> {
        /*
        归一化姿势landmarks并转换为embedding

        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).

        Result:
          Numpy array with pose embedding of shape (M, 3) where `M` is the number of
          pairwise distances defined in `_get_pose_distance_embedding`.
          具有形状 (M, 3) 的姿势embedding的 Numpy 数组，其中“M”是“_get_pose_distance_embedding”中定义的成对距离的数量。
        */
        assert(landmarks.size == _landmarkNames.size) { "Expected ${_landmarkNames.size} landmarks, but got ${landmarks.size}" }

        // Normalize landmarks.
        val newLandmarks = normalizePoseLandmarks(landmarks)

        // Get embedding.
        return getPoseDistanceEmbedding(newLandmarks)

    }

}
