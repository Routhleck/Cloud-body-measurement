import cv2
import mediapipe as mp
import numpy as np
from sklearn.neighbors import KNeighborsClassifier
from sports_pullUp import poseclassifier
from sports_pullUp import poseembedding as pe  # 姿态关键点编码模块
from mediapipe.python.solutions import pose as mp_pose
from sports_pullUp import poseembedding as pe  # 姿态关键点编码模块
from sports_pullUp import poseclassifier as pc  # 姿态分类器
from sports_pullUp import resultsmooth as rs  # 分类结果平滑
from sports_pullUp import counter  # 动作计数器
from sports_pullUp import visualizer as vs  # 可视化模块
import mediapipe.python.solutions.pose  as my_pose
'''
class PoseClassifier:
    def __init__(self):
        self.mp_pose = mp.solutions.pose
        self.knn = KNeighborsClassifier(n_neighbors=2)

        # Define pose detection thresholds and keypoints indices
        self.thresholds = {
            "pull_up_start": {
                "shoulder": self.mp_pose.PoseLandmark.LEFT_SHOULDER.value * 3 + 1,
                "hip": self.mp_pose.PoseLandmark.LEFT_HIP.value * 3 + 1,
            },
            "pull_up_end": {
                "shoulder": self.mp_pose.PoseLandmark.RIGHT_SHOULDER.value * 3 + 1,
                "hip": self.mp_pose.PoseLandmark.RIGHT_HIP.value * 3 + 1,
            }
        }

        # Training data
        self.X_train = [
            [self.thresholds["pull_up_start"]["shoulder"], self.thresholds["pull_up_start"]["hip"]],
            [self.thresholds["pull_up_end"]["shoulder"], self.thresholds["pull_up_end"]["hip"]],
        ]
        self.y_train = ["pull_up_start", "pull_up_end"]

        # Train the KNN classifier
        self.knn.fit(self.X_train, self.y_train)

    def classify_pose(self, landmarks):
        if landmarks is None:
            return None

        # Extract landmark coordinates
        keypoints = np.array([[lmk.x, lmk.y, lmk.z] for lmk in landmarks.landmark]).flatten()

        # Classify pose based on keypoints
        shoulder = keypoints[self.mp_pose.PoseLandmark.LEFT_SHOULDER.value * 3 + 1]
        hip = keypoints[self.mp_pose.PoseLandmark.LEFT_HIP.value * 3 + 1]
        class_label = self.knn.predict([[shoulder, hip]])

        return class_label[0] if class_label else None
'''


def process_video_stream(video_stream_url):
    class_name='pullUps_up'
    pose_samples_folder = 'sports_pullUp/fitness_poses_csvs_out'  # 包含姿势类别CSV文件的文件夹
    pose_tracker =my_pose.Pose()   # 姿势跟踪器
    pose_embedder = pe.FullBodyPoseEmbedder()  # 姿势嵌入器
    pose_classifier = pc.PoseClassifier(
        pose_samples_folder=pose_samples_folder,
        pose_embedder=pose_embedder,
        top_n_by_max_distance=30,
        top_n_by_mean_distance=10)  # 姿势分类器
    pose_classification_filter = rs.EMADictSmoothing(
        window_size=10,
        alpha=0.2)  # EMA平滑
    repetition_counter = counter.RepetitionCounter(
        class_name=class_name,
        enter_threshold=9.9,
        exit_threshold=9.9)  # 动作计数器
    pose_classification_visualizer = vs.PoseClassificationVisualizer(
        class_name=class_name,
        plot_y_max=15)  # 可视化模块
    pose_classifier = poseclassifier.PoseClassifier(pose_samples_folder = 'sports_pullUp/fitness_poses_csvs_out',pose_embedder=pe.FullBodyPoseEmbedder())  # Instantiate pose classifier
    pull_up_count = 0  # Initialize pull-up counter

    # Open the video stream
    cap = cv2.VideoCapture(video_stream_url)

    # Check if video stream opened successfully
    if not cap.isOpened():
        print("Unable to open video stream!")
        return pull_up_count

    # Initialize Mediapipe pose detector
    mp_pose = mp.solutions.pose.Pose(static_image_mode=False, min_detection_confidence=0.5)
    mp_drawing = mp.solutions.drawing_utils

    # Read and process each frame in the video stream
    while True:
        ret, frame = cap.read()  # Read video frame
        if not ret:
            break

        # 运行姿势跟踪
        # 图片格式转换
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        # 进行姿态检测
        results = mp_pose.process(frame_rgb)
        # 获取姿势关键点
        pose_landmarks = results.pose_landmarks
        output_frame = frame.copy()
        # Draw the landmarks on the image
        if pose_landmarks is not None:
            mp_drawing.draw_landmarks(
                frame,
                pose_landmarks,
                mp.solutions.pose.POSE_CONNECTIONS  # Use POSE_CONNECTIONS for connections between landmarks
            )

        if pose_landmarks is not None:
            # 获取关键点坐标
            frame_height, frame_width = output_frame.shape[0], output_frame.shape[1]
            pose_landmarks = np.array([[lmk.x * frame_width, lmk.y * frame_height, lmk.z * frame_width]
                                       for lmk in pose_landmarks.landmark], dtype=np.float32)



        # Classify the pose
        class_label = pose_classifier(pose_landmarks)

        # 使用EMA进行平滑
        pose_classification_filtered = pose_classification_filter(class_label)

        # 计数
        repetitions_count = repetition_counter(pose_classification_filtered)


        # Display the pull-ups count on the image
        cv2.putText(frame, f"Pull-ups: {repetitions_count}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.imshow("Video Stream", frame)

        # Release the memory of processed frame
        del frame_rgb
        del results

        # Exit loop if 'q' key is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

    return repetitions_count-1


def main():
    # Video stream URL
    video_stream = 'http://39.106.13.47:8080/live/111.live.flv'

    # Process video stream and count pull-ups
    pull_up_count = process_video_stream(video_stream)

    # Output result
    print(f'Pull-up count: {pull_up_count}')


if __name__ == '__main__':
    main()
