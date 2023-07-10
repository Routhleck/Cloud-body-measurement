import cv2
import mediapipe as mp
import numpy as np
from sports_squat import poseclassifier as pc
from sports_squat import poseembedding as pe
from sports_squat import resultsmooth as rs
from sports_squat import counter
from sports_squat import visualizer as vs


def squat_video_stream(video_stream_url):
    class_name = 'up'
    pose_samples_folder = 'sports_squat/fitness_poses_csvs_out'

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
    pose_classification_visualizer = vs.PoseClassificationVisualizer(
        class_name=class_name,
        plot_y_max=15
    )
    repetition_count = 0

    cap = cv2.VideoCapture(video_stream_url)
    if not cap.isOpened():
        print("Unable to open video stream!")
        return repetition_count

    mp_pose = mp.solutions.pose.Pose(static_image_mode=False, min_detection_confidence=0.5)
    mp_drawing = mp.solutions.drawing_utils

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = mp_pose.process(frame_rgb)
        pose_landmarks = results.pose_landmarks
        output_frame = frame.copy()

        if pose_landmarks is not None:
            mp_drawing.draw_landmarks(
                frame,
                pose_landmarks,
                mp.solutions.pose.POSE_CONNECTIONS
            )

        if pose_landmarks is not None:
            frame_height, frame_width = output_frame.shape[0], output_frame.shape[1]
            pose_landmarks = np.array([[lmk.x * frame_width, lmk.y * frame_height, lmk.z * frame_width]
                                       for lmk in pose_landmarks.landmark], dtype=np.float32)
            assert pose_landmarks.shape == (33, 3), 'Unexpected landmarks shape: {}'.format(pose_landmarks.shape)

            pose_classification = pose_classifier(pose_landmarks)
            pose_classification_filtered = pose_classification_filter(pose_classification)
            repetition_count = repetition_counter(pose_classification_filtered)

        cv2.putText(frame, f"Squats: {repetition_count}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.imshow("Video Stream", frame)

        del frame_rgb
        del results

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

    return repetition_count
