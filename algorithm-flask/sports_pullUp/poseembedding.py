import numpy as np


# 人体姿态编码模块
class FullBodyPoseEmbedder(object):
    """Converts 3D pose landmarks into 3D embedding."""

    def __init__(self, torso_size_multiplier=2.5):
        # Multiplier to apply to the torso to get minimal body size.
        # 乘数应用于躯干以获得最小的身体尺寸
        self._torso_size_multiplier = torso_size_multiplier

        # Names of the landmarks as they appear in the prediction.
        # 出现在预测中的landmarks名称。
        self._landmark_names = [
            'nose',
            'left_eye_inner', 'left_eye', 'left_eye_outer',
            'right_eye_inner', 'right_eye', 'right_eye_outer',
            'left_ear', 'right_ear',
            'mouth_left', 'mouth_right',
            'left_shoulder', 'right_shoulder',
            'left_elbow', 'right_elbow',
            'left_wrist', 'right_wrist',
            'left_pinky_1', 'right_pinky_1',
            'left_index_1', 'right_index_1',
            'left_thumb_2', 'right_thumb_2',
            'left_hip', 'right_hip',
            'left_knee', 'right_knee',
            'left_ankle', 'right_ankle',
            'left_heel', 'right_heel',
            'left_foot_index', 'right_foot_index',
        ]

    def __call__(self, landmarks):
        """Normalizes pose landmarks and converts to embedding
        归一化姿势landmarks并转换为embedding

        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).

        Result:
          Numpy array with pose embedding of shape (M, 3) where `M` is the number of
          pairwise distances defined in `_get_pose_distance_embedding`.
          具有形状 (M, 3) 的姿势embedding的 Numpy 数组，其中“M”是“_get_pose_distance_embedding”中定义的成对距离的数量。
        """
        assert landmarks.shape[0] == len(self._landmark_names), 'Unexpected number of landmarks: {}'.format(
            landmarks.shape[0])

        # 获取 landmarks.
        landmarks = np.copy(landmarks)

        # Normalize landmarks.
        landmarks = self._normalize_pose_landmarks(landmarks)

        # Get embedding.
        embedding = self._get_pose_distance_embedding(landmarks)

        return embedding

    def _normalize_pose_landmarks(self, landmarks):
        """Normalizes landmarks translation and scale.归一化landmarks的平移和缩放"""
        landmarks = np.copy(landmarks)

        # Normalize translation.
        pose_center = self._get_pose_center(landmarks)
        landmarks -= pose_center

        # Normalize scale.
        pose_size = self._get_pose_size(landmarks, self._torso_size_multiplier)
        landmarks /= pose_size
        # Multiplication by 100 is not required, but makes it eaasier to debug.
        landmarks *= 100

        return landmarks

    def _get_pose_center(self, landmarks):
        """Calculates pose center as point between hips.将姿势中心计算为臀部之间的点。"""
        '''left_hip = landmarks[self._landmark_names.index('left_hip')]
        right_hip = landmarks[self._landmark_names.index('right_hip')]
        center = (left_hip + right_hip) * 0.5'''
        """Calculates pose center as point between hips.将姿势中心计算为两手之间的点。"""
        left_thumb_2 = landmarks[self._landmark_names.index('left_thumb_2')]
        right_thumb_2 = landmarks[self._landmark_names.index('right_thumb_2')]
        center = (left_thumb_2 + right_thumb_2) * 0.5
        return center

    def _get_pose_size(self, landmarks, torso_size_multiplier):
        """Calculates pose size.计算姿势大小。

        它是下面两个值的最大值:
          * 躯干大小乘以`torso_size_multiplier`
          * 从姿势中心到任何姿势地标的最大距离
        """
        # 这种方法仅使用 2D landmarks来计算姿势大小.
        landmarks = landmarks[:, :2]

        '''# 臀部中心。
        left_hip = landmarks[self._landmark_names.index('left_hip')]
        right_hip = landmarks[self._landmark_names.index('right_hip')]
        hips = (left_hip + right_hip) * 0.5

        # 两肩中心。
        left_shoulder = landmarks[self._landmark_names.index('left_shoulder')]
        right_shoulder = landmarks[self._landmark_names.index('right_shoulder')]
        shoulders = (left_shoulder + right_shoulder) * 0.5'''

        # 两手中心。
        left_hand = landmarks[self._landmark_names.index('left_thumb_2')]
        right_hand = landmarks[self._landmark_names.index('right_thumb_2')]
        hands = (left_hand + right_hand) * 0.5

        # 两臀中心。
        left_hip = landmarks[self._landmark_names.index('left_hip')]
        right_hip = landmarks[self._landmark_names.index('right_hip')]
        hips = (left_hip + right_hip) * 0.5


        # 躯干尺寸作为最小的身体尺寸。
        torso_size = np.linalg.norm(hands - hips)

        # 到姿势中心的最大距离。
        pose_center = self._get_pose_center(landmarks)
        max_dist = np.max(np.linalg.norm(landmarks - pose_center, axis=1))

        return max(torso_size * torso_size_multiplier, max_dist)

    def _get_pose_distance_embedding(self, landmarks):
        """Converts pose landmarks into 3D embedding.
            将姿势landmarks转换为 3D embedding.
        我们使用几个成对的 3D 距离来形成姿势embedding。 所有距离都包括带符号的 X 和 Y 分量。
        我们使用不同类型的对来覆盖不同的姿势类别。 Feel free to remove some or add new.

        Args:
          landmarks - NumPy array with 3D landmarks of shape (N, 3).

        Result:
          Numpy array with pose embedding of shape (M, 3) where `M` is the number of
          pairwise distances.
        """
        embedding = np.array([
            # one joint.
            self._get_distance(
                self._get_average_by_names(landmarks, 'left_hip', 'right_hip'),
                self._get_average_by_names(landmarks, 'left_thumb_2', 'right_thumb_2')),

            # 左眼与左肩的距离。
            self._get_distance_by_names(landmarks, 'left_eye', 'left_shoulder'),
            # 右眼与右肩的距离。
            self._get_distance_by_names(landmarks, 'right_eye', 'right_shoulder'),
            # 左眼与左手的距离。
            self._get_distance_by_names(landmarks, 'left_eye', 'left_thumb_2'),
            # 右眼与右手的距离。
            self._get_distance_by_names(landmarks, 'right_eye', 'right_thumb_2'),
            # 鼻子与左肩的距离。
            self._get_distance_by_names(landmarks, 'nose', 'left_shoulder'),
            # 鼻子与右肩的距离。
            self._get_distance_by_names(landmarks, 'nose', 'right_shoulder'),
            # 鼻子与左手的距离。
            self._get_distance_by_names(landmarks, 'nose', 'left_thumb_2'),
            # 鼻子与右手的距离。
            self._get_distance_by_names(landmarks, 'nose', 'right_thumb_2'),
            # 左嘴与左肩的距离。
            self._get_distance_by_names(landmarks, 'mouth_left', 'left_shoulder'),
            # 右嘴与右肩的距离。
            self._get_distance_by_names(landmarks, 'mouth_right', 'right_shoulder'),
            # 左嘴与左手的距离。
            self._get_distance_by_names(landmarks, 'mouth_left', 'left_thumb_2'),
            # 右嘴与右手的距离。
            self._get_distance_by_names(landmarks, 'mouth_right', 'right_thumb_2'),
            # 左肩膀与左肘的距离
            self._get_distance_by_names(landmarks, 'left_shoulder', 'left_elbow'),
            # 右肩膀与右肘的距离
            self._get_distance_by_names(landmarks, 'right_shoulder', 'right_elbow'),
            # 左肘和左手腕的距离
            self._get_distance_by_names(landmarks, 'left_elbow', 'left_wrist'),
            # 右肘和右手腕的距离
            self._get_distance_by_names(landmarks, 'right_elbow', 'right_wrist'),
            # 左髋关节与左膝盖的距离
            self._get_distance_by_names(landmarks, 'left_hip', 'left_knee'),
            # 右髋关节与右膝盖的距离
            self._get_distance_by_names(landmarks, 'right_hip', 'right_knee'),
            # 左膝盖和左踝的距离
            self._get_distance_by_names(landmarks, 'left_knee', 'left_ankle'),
            # 右膝盖和右踝的距离
            self._get_distance_by_names(landmarks, 'right_knee', 'right_ankle'),
            # 左肩膀与左手腕的距离
            self._get_distance_by_names(landmarks, 'left_shoulder', 'left_wrist'),
            # 右肩膀与右手腕的距离
            self._get_distance_by_names(landmarks, 'right_shoulder', 'right_wrist'),
            # 左髋关节与左踝的距离
            self._get_distance_by_names(landmarks, 'left_hip', 'left_ankle'),
            # 右髋关节与右踝的距离
            self._get_distance_by_names(landmarks, 'right_hip', 'right_ankle'),
            # 左髋关节与左肘的距离
            self._get_distance_by_names(landmarks, 'left_hip', 'left_elbow'),
            # 右髋关节与右肘的距离
            self._get_distance_by_names(landmarks, 'right_hip', 'right_elbow'),
            # 左肩与左脚踝的距离
            self._get_distance_by_names(landmarks, 'left_shoulder', 'left_ankle'),
            # 右肩与右脚踝的距离
            self._get_distance_by_names(landmarks, 'right_shoulder', 'right_ankle'),
            # 左髋关节与左手腕的距离
            self._get_distance_by_names(landmarks, 'left_hip', 'left_wrist'),
            # 右髋关节与右手腕的距离
            self._get_distance_by_names(landmarks, 'right_hip', 'right_wrist'),
            # 左肘与右肘的距离
            self._get_distance_by_names(landmarks, 'left_elbow', 'right_elbow'),
            # 左膝盖与右膝盖的距离
            self._get_distance_by_names(landmarks, 'left_knee', 'right_knee'),
            # 左脚踝与右脚踝的距离
            self._get_distance_by_names(landmarks, 'left_ankle', 'right_ankle'),
            # 左手腕与右手腕的距离
            self._get_distance_by_names(landmarks, 'left_wrist', 'right_wrist'),


            # Body bent direction.

            # self._get_distance(
            #     self._get_average_by_names(landmarks, 'left_wrist', 'left_ankle'),
            #     landmarks[self._landmark_names.index('left_hip')]),
            # self._get_distance(
            #     self._get_average_by_names(landmarks, 'right_wrist', 'right_ankle'),
            #     landmarks[self._landmark_names.index('right_hip')]),
        ])

        return embedding

    def _get_average_by_names(self, landmarks, name_from, name_to):
        lmk_from = landmarks[self._landmark_names.index(name_from)]
        lmk_to = landmarks[self._landmark_names.index(name_to)]
        return (lmk_from + lmk_to) * 0.5

    def _get_distance_by_names(self, landmarks, name_from, name_to):
        lmk_from = landmarks[self._landmark_names.index(name_from)]
        lmk_to = landmarks[self._landmark_names.index(name_to)]
        return self._get_distance(lmk_from, lmk_to)

    def _get_distance(self, lmk_from, lmk_to):
        return lmk_to - lmk_from

