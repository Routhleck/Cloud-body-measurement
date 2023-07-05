package com.google.mediapipe.examples.poselandmarker.algorithm
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.indexing.NDArrayIndex
import kotlin.math.max

class PoseEmbedder(torsoSizeMultiplier: Double = 2.5) {
    private val _torso_size_multiplier = torsoSizeMultiplier

    private val _landmark_names = listOf(
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


    private fun _get_pose_center(landmarks: Array<DoubleArray>): DoubleArray {
        /*
        Calculates pose center as point between hips.将姿势中心计算为臀部之间的点。"""
        left_hip = landmarks[self._landmark_names.index('left_hip')]
        right_hip = landmarks[self._landmark_names.index('right_hip')]
        center = (left_hip + right_hip) * 0.5
        */
        val left_thumb_2 = landmarks[_landmark_names.indexOf("left_thumb_2")]
        val right_thumb_2 = landmarks[_landmark_names.indexOf("right_thumb_2")]
        return (Nd4j.create(left_thumb_2).add(Nd4j.create(right_thumb_2))).div(2).toDoubleVector()
    }

    private fun _get_pose_size(landmarks: Array<DoubleArray>, torso_size_multiplier: Double): Double {
        /*
        计算姿势大小。
        它是下面两个值的最大值:
          * 躯干大小乘以`torso_size_multiplier`
          * 从姿势中心到任何姿势地标的最大距离
        */

        val newLandmarks: INDArray = Nd4j.createFromArray(landmarks)
        val landmarks2d: INDArray = newLandmarks.get(NDArrayIndex.all(), NDArrayIndex.interval(0, 2))

        // 两手中心
        val left_hand = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf("left_wrist").toLong()))
        val right_hand = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf("right_wrist").toLong()))
        val hands = (left_hand.add(right_hand)).div(2).toDoubleVector()

        // 两臀中心
        val left_hip = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf("left_hip").toLong()))
        val right_hip = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf("right_hip").toLong()))
        val hips = (left_hip.add(right_hip)).div(2).toDoubleVector()

        // 躯干尺寸作为最小的身体尺寸
        val torso_size = (Nd4j.create(hands).sub(Nd4j.create(hips))).norm2Number().toDouble() * torso_size_multiplier

        // 到姿势中心的最大距离
        val pose_center = _get_pose_center(landmarks)
        val diff: INDArray = newLandmarks.sub(Nd4j.createFromArray(*pose_center))
        val norms: Double = diff.norm2Number().toDouble()
        val max_dist: Double = norms

        return max(torso_size, max_dist)
    }

    private fun _normalize_pose_landmarks(landmarks: Array<DoubleArray>): INDArray {
        /*
        将姿势地标归一化为单位尺寸。
        1. 将姿势中心移动到原点。
        2. 将姿势缩放到单位大小。
        3. 将姿势移动到原始中心。
        */
        val landmarks3d: INDArray = Nd4j.createFromArray(landmarks)

        // 1. 将姿势中心移动到原点。
        val pose_center = _get_pose_center(landmarks)
        landmarks3d.subi(Nd4j.createFromArray(*pose_center))

        // 2. 将姿势缩放到单位大小。
        val pose_size = _get_pose_size(landmarks, _torso_size_multiplier)
        landmarks3d.divi(pose_size)

        // Multiplication by 100 is not required, but makes it eaasier to debug
        landmarks3d.muli(100)

        return landmarks3d
    }

    private fun _get_pose_distance_embedding(landmarks: Array<DoubleArray>): INDArray {
        /*
        将姿势landmarks转换为 3D embedding.
        我们使用几个成对的 3D 距离来形成姿势embedding。 所有距离都包括带符号的 X 和 Y 分量。
        我们使用不同类型的对来覆盖不同的姿势类别。
        Args:
          landmarks - 3D landmarks的NumPy数组，形状为 (N, 3)。

        Result:
          形状为 (M, 3) 的pose embedding的INDArray，其中 `M` 是成对距离的数量。
        */
        val embeddingList: MutableList<INDArray> = mutableListOf()

        // one joint.
        val jointDistance = _get_distance(
            _get_average_by_names(landmarks, "left_hip", "right_hip"),
            _get_average_by_names(landmarks, "left_thumb_2", "right_thumb_2")
        )
        embeddingList.add(jointDistance)

        // 左眼与左肩的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_eye", "left_shoulder"))
        // 右眼与右肩的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_eye", "right_shoulder"))
        // 左眼与左手的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_eye", "left_thumb_2"))
        // 右眼与右手的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_eye", "right_thumb_2"))
        // 鼻子与左肩的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "nose", "left_shoulder"))
        // 鼻子与右肩的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "nose", "right_shoulder"))
        // 鼻子与左手的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "nose", "left_thumb_2"))
        // 鼻子与右手的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "nose", "right_thumb_2"))
        // 左嘴与左肩的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "mouth_left", "left_shoulder"))
        // 右嘴与右肩的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "mouth_right", "right_shoulder"))
        // 左嘴与左手的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "mouth_left", "left_thumb_2"))
        // 右嘴与右手的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "mouth_right", "right_thumb_2"))
        // 左肩膀与左肘的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_shoulder", "left_elbow"))
        // 右肩膀与右肘的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_shoulder", "right_elbow"))
        // 左肘和左手腕的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_elbow", "left_wrist"))
        // 右肘和右手腕的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_elbow", "right_wrist"))
        // 左髋关节与左膝盖的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_hip", "left_knee"))
        // 右髋关节与右膝盖的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_hip", "right_knee"))
        // 左膝盖和左踝的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_knee", "left_ankle"))
        // 右膝盖和右踝的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_knee", "right_ankle"))
        // 左肩膀与左手腕的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_shoulder", "left_wrist"))
        // 右肩膀与右手腕的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_shoulder", "right_wrist"))
        // 左髋关节与左踝的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_hip", "left_ankle"))
        // 右髋关节与右踝的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_hip", "right_ankle"))
        // 左髋关节与左肘的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_hip", "left_elbow"))
        // 右髋关节与右肘的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_hip", "right_elbow"))
        // 左肩与左脚踝的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_shoulder", "left_ankle"))
        // 右肩与右脚踝的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_shoulder", "right_ankle"))
        // 左髋关节与左手腕的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_hip", "left_wrist"))
        // 右髋关节与右手腕的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "right_hip", "right_wrist"))
        // 左肘与右肘的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_elbow", "right_elbow"))
        // 左膝盖与右膝盖的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_knee", "right_knee"))
        // 左脚踝与右脚踝的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_ankle", "right_ankle"))
        // 左手腕与右手腕的距离。
        embeddingList.add(_get_distance_by_names(landmarks, "left_wrist", "right_wrist"))

        return Nd4j.vstack(embeddingList)
    }

    private fun _get_average_by_names(landmarks: Array<DoubleArray>, name_from: String, name_to: String): INDArray {
        /*
        计算两个姿势地标之间的平均值。
        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).
          name_from - Name of the first landmark.
          name_to - Name of the second landmark.

        Result:
          Numpy array with the average of the two landmarks.
        */
        val newLandmarks: INDArray = Nd4j.createFromArray(landmarks)
        val landmarks2d: INDArray = newLandmarks.get(NDArrayIndex.all(), NDArrayIndex.interval(0, 2))

        val lmk_from = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf(name_from).toLong()))
        val lmk_to = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf(name_to).toLong()))

        return (lmk_from.add(lmk_to)).div(2)
    }

    private fun _get_distance_by_names(landmarks: Array<DoubleArray>, name_from: String, name_to: String): INDArray {
        /*
        计算两个姿势地标之间的距离。
        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).
          name_from - Name of the first landmark.
          name_to - Name of the second landmark.

        Result:
          Numpy array with the distance between the two landmarks.
        */
        val newLandmarks: INDArray = Nd4j.createFromArray(landmarks)
        val landmarks2d: INDArray = newLandmarks.get(NDArrayIndex.all(), NDArrayIndex.interval(0, 2))

        val lmk_from = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf(name_from).toLong()))
        val lmk_to = landmarks2d.get(NDArrayIndex.point(_landmark_names.indexOf(name_to).toLong()))

        return _get_distance(lmk_from, lmk_to)
    }

    private fun _get_distance(lmk_from: INDArray, lmk_to: INDArray): INDArray {
        /*
        计算两个3D点之间的距离。
        */
        return lmk_from.sub(lmk_to)
    }

    operator fun invoke(landmarks: Array<DoubleArray>): INDArray {
        /*
        归一化姿势landmarks并转换为embedding

        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).

        Result:
          Numpy array with pose embedding of shape (M, 3) where `M` is the number of
          pairwise distances defined in `_get_pose_distance_embedding`.
          具有形状 (M, 3) 的姿势embedding的 Numpy 数组，其中“M”是“_get_pose_distance_embedding”中定义的成对距离的数量。
        */
        assert(landmarks.size == _landmark_names.size) { "Expected ${_landmark_names.size} landmarks, but got ${landmarks.size}" }

        // Normalize landmarks.
        var newLandmarks = _normalize_pose_landmarks(landmarks)

        // 将newLandmarks转化为Array<DoubleArray>
        val newLandmarksArray = newLandmarks.toDoubleMatrix()

        // Get embedding.
        return _get_pose_distance_embedding(newLandmarksArray)

    }

}
