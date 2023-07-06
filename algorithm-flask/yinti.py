import cv2
import mediapipe as mp
import numpy as np
from sklearn.neighbors import KNeighborsClassifier


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


def process_video_stream(video_stream_url):
    pose_classifier = PoseClassifier()  # Instantiate pose classifier
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

        # Convert image from BGR to RGB
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        # Perform pose detection
        results = mp_pose.process(frame_rgb)

        # Get pose landmarks
        landmarks = results.pose_landmarks

        # Classify the pose
        class_label = pose_classifier.classify_pose(landmarks)

        # Update the pull-ups count
        if class_label == 'pull_up_start':
            pull_up_count += 1

        # Draw the landmarks on the image
        if landmarks is not None:
            mp_drawing.draw_landmarks(
                frame,
                landmarks,
                mp.solutions.pose.POSE_CONNECTIONS  # Use POSE_CONNECTIONS for connections between landmarks
            )

        # Display the pull-ups count on the image
        cv2.putText(frame, f"Pull-ups: {pull_up_count}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.imshow("Video Stream", frame)

        # Release the memory of processed frame
        del frame_rgb
        del results

        # Exit loop if 'q' key is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

    return pull_up_count


def main():
    # Video stream URL
    video_stream = 'http://39.106.13.47:8080/live/111.live.flv'

    # Process video stream and count pull-ups
    pull_up_count = process_video_stream(video_stream)

    # Output result
    print(f'Pull-up count: {pull_up_count}')


if __name__ == '__main__':
    main()
