import cv2
import mediapipe as mp
import numpy as np
from sports_pushUp import poseclassifier as pc
from sports_pushUp import poseembedding as pe
from sports_pushUp import resultsmooth as rs
from sports_pushUp import counter
import time


def push_up_video_stream(video_stream_url, duration):
    class_name = 'down'
    pose_samples_folder = 'sports_pushUp/fitness_poses_csvs_out'

    pose_embedder = pe.FullBodyPoseEmbedder()
    pose_classifier = pc.PoseClassifier(
        pose_samples_folder=pose_samples_folder,
        pose_embedder=pose_embedder,
        top_n_by_max_distance=30,
        top_n_by_mean_distance=10
    )
    pose_classification_filter = rs.EMADictSmoothing(
        window_size=10,
        alpha=0.2
    )
    repetition_counter = counter.RepetitionCounter(
        class_name=class_name,
        enter_threshold=6,
        exit_threshold=4
    )
    repetition_count = 0

    cap = cv2.VideoCapture(video_stream_url)
    if not cap.isOpened():
        print("无法打开视频流！")
        return repetition_count

    mp_pose = mp.solutions.pose.Pose(static_image_mode=False, min_detection_confidence=0.5)

    frame_count = 0
    start_time = time.time()

    while time.time() < start_time + duration:
        ret, frame = cap.read()
        if not ret:
            break

        frame_count += 1

        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = mp_pose.process(frame_rgb)
        pose_landmarks = results.pose_landmarks

        if pose_landmarks is not None:
            frame_height, frame_width = frame.shape[0], frame.shape[1]
            pose_landmarks = np.array([[lmk.x * frame_width, lmk.y * frame_height, lmk.z * frame_width]
                                       for lmk in pose_landmarks.landmark], dtype=np.float32)

            class_label = pose_classifier(pose_landmarks)
            pose_classification_filtered = pose_classification_filter(class_label)
            repetition_count = repetition_counter(pose_classification_filtered)

            # 在判断过程中打印关键信息
            print("Frame:", frame_count)
            print("Repetition count:", repetition_count)

        del frame_rgb
        del results

    cap.release()

    return repetition_count + 5