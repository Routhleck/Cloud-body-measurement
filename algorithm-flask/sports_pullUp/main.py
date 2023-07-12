from flask import Flask, render_template, Response
import cv2
from mediapipe.python.solutions import pose as mp_pose
import numpy as np
import poseembedding as pe
import poseclassifier as pc
import resultsmooth as rs
import counter
import visualizer as vs
import requests
import io

app = Flask(__name__)


def process_frame(frame, pose_tracker, pose_classifier, pose_classification_filter, repetition_counter,
                  pose_classification_visualizer):
    # Convert frame to RGB
    frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

    # Process the frame
    result = pose_tracker.process(frame_rgb)
    pose_landmarks = result.pose_landmarks

    # Draw pose landmarks on the frame
    # if pose_landmarks is not None:
    #     mp_pose.drawing_utils.draw_landmarks(
    #         image=frame,
    #         landmark_list=pose_landmarks,
    #         connections=mp_pose.POSE_CONNECTIONS
    #     )

    if pose_landmarks is not None:
        # Get landmarks
        frame_height, frame_width = frame.shape[0], frame.shape[1]
        pose_landmarks = np.array([[lmk.x * frame_width, lmk.y * frame_height, lmk.z * frame_width]
                                   for lmk in pose_landmarks.landmark], dtype=np.float32)
        assert pose_landmarks.shape == (33, 3), 'Unexpected landmarks shape: {}'.format(pose_landmarks.shape)

        # Classify the pose on the current frame
        pose_classification = pose_classifier(pose_landmarks)

        # Smooth classification using EMA
        pose_classification_filtered = pose_classification_filter(pose_classification)

        # Count repetitions
        repetitions_count = repetition_counter(pose_classification_filtered)
    else:
        # No pose, reset classification and counter
        pose_classification = None
        pose_classification_filtered = None
        repetitions_count = repetition_counter.n_repeats

    # Visualize classification and repetition count
    frame = pose_classification_visualizer(
        frame=frame,
        pose_classification=pose_classification,
        pose_classification_filtered=pose_classification_filtered,
        repetitions_count=repetitions_count
    )

    return frame


def video_stream(api_url):
    # Initialize pose tracker, pose embedder, pose classifier, result smoother, and counter.
    pose_tracker = mp_pose.Pose()
    pose_embedder = pe.FullBodyPoseEmbedder()
    pose_classifier = pc.PoseClassifier(pose_samples_folder=pose_samples_folder, pose_embedder=pose_embedder)
    pose_classification_filter = rs.EMADictSmoothing(window_size=10, alpha=0.2)
    repetition_counter = counter.RepetitionCounter(class_name=class_name, enter_threshold=9.9, exit_threshold=9.9)
    pose_classification_visualizer = vs.PoseClassificationVisualizer(class_name=class_name)

    # Create an OpenCV video capture object
    cap = cv2.VideoCapture(api_url)

    while True:
        # Read the next frame from the video stream
        ret, frame = cap.read()

        if not ret:
            break

        # Process the frame
        processed_frame = process_frame(
            frame=frame,
            pose_tracker=pose_tracker,
            pose_classifier=pose_classifier,
            pose_classification_filter=pose_classification_filter,
            repetition_counter=repetition_counter,
            pose_classification_visualizer=pose_classification_visualizer
        )

        # Convert the processed frame to JPEG format
        # ret, jpeg = cv2.imencode('.jpg', processed_frame)
        # frame_bytes = jpeg.tobytes()

        # Yield the frame as a byte string
        # yield (b'--frame\r\n'
        #        b'Content-Type: image/jpeg\r\n\r\n' + frame_bytes + b'\r\n')


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/video_feed')
def video_feed():
    # Set the API URL for the video stream
    api_url = 'http://39.106.13.47:8080/live/111.live.flv'

    # Provide the video feed as a response
    return Response(video_stream(api_url),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    # Set the pose samples folder and class name
    pose_samples_folder = 'fitness_poses_csvs_out'
    class_name = 'pushups_down'

    app.run(debug=True)
