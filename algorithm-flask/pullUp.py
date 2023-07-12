import cv2
import mediapipe as mp
import numpy as np
from sports_pullUp import poseclassifier as pc
from sports_pullUp import poseembedding as pe
from sports_pullUp import resultsmooth as rs
from sports_pullUp import counter
import time


def pull_up_video_stream(video_stream_url, duration):
    class_name = 'pullUps_up'
    pose_samples_folder = 'sports_pullUp/fitness_poses_csvs_out'
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
        enter_threshold=9.9,
        exit_threshold=9.9
    )
    repetitions_count = 0

    cap = cv2.VideoCapture(video_stream_url)
    if not cap.isOpened():
        print("无法打开视频流！")
        return repetitions_count

    mp_pose = mp.solutions.pose.Pose(static_image_mode=False, min_detection_confidence=0.5)

    start_time = time.time()
    end_time = start_time + duration

    while time.time() < end_time:
        ret, frame = cap.read()
        if not ret:
            break

        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = mp_pose.process(frame_rgb)
        pose_landmarks = results.pose_landmarks

        if pose_landmarks is not None:
            frame_height, frame_width = frame.shape[0], frame.shape[1]
            pose_landmarks = np.array([[lmk.x * frame_width, lmk.y * frame_height, lmk.z * frame_width]
                                       for lmk in pose_landmarks.landmark], dtype=np.float32)

            class_label = pose_classifier(pose_landmarks)
            pose_classification_filtered = pose_classification_filter(class_label)
            repetitions_count = repetition_counter(pose_classification_filtered)

        del frame_rgb
        del results

    cap.release()

    return repetitions_count - 1
