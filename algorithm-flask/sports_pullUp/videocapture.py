import matplotlib
from matplotlib import pyplot as plt
matplotlib.use("Agg")
import cv2
import numpy as np
from mediapipe.python.solutions import drawing_utils as mp_drawing
from mediapipe.python.solutions import pose as mp_pose
import poseembedding as pe  # 姿态关键点编码模块
import poseclassifier as pc  # 姿态分类器
import resultsmooth as rs  # 分类结果平滑
import counter  # 动作计数器
import visualizer as vs  # 可视化模块


def show_image(img, figsize=(10, 10)):
    """显示图片"""
    plt.figure(figsize=figsize)
    plt.imshow(img)
    plt.show()
    plt.close('all')


def process():
    # class_name需要与你的训练样本的两个动作状态图像文件夹的名字中的一个保持一致，它后面将用于分类时的索引。
    # 具体是哪个动作文件夹的名字取决于你的运动是什么，例如：如果是深蹲，明显比较重要的判断计数动作是蹲下去；如果是引体向上，则判断计数的动作是向上拉到最高点的那个动作
    class_name = 'pullUps_up'
    out_video_path = 'squat-sample-out.mp4'

    cv2.namedWindow('video', cv2.WINDOW_NORMAL)
    video_cap = cv2.VideoCapture(0)

    # 获取一些视频参数以生成具有分类结果的输出视频
    video_fps = 24
    video_width = 640
    video_height = 480

    # 初始化跟踪器、分类器和计数器
    pose_samples_folder = 'sports_pullUp/fitness_poses_csvs_out'  # 包含姿势类别CSV文件的文件夹
    pose_tracker = mp_pose.Pose()  # 姿势跟踪器
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

    out_video = cv2.VideoWriter(out_video_path, cv2.VideoWriter_fourcc(*'mp4v'), video_fps, (video_width, video_height))

    while video_cap.isOpened():
        success, input_frame = video_cap.read()
        if not success:
            break

        # 运行姿势跟踪器
        input_frame = cv2.cvtColor(input_frame, cv2.COLOR_BGR2RGB)
        result = pose_tracker.process(image=input_frame)
        pose_landmarks = result.pose_landmarks

        output_frame = input_frame.copy()
        if pose_landmarks is not None:
            # 绘制姿势关键点
            mp_drawing.draw_landmarks(
                image=output_frame,
                landmark_list=pose_landmarks,
                connections=mp_pose.POSE_CONNECTIONS)

        if pose_landmarks is not None:
            # 获取关键点坐标
            frame_height, frame_width = output_frame.shape[0], output_frame.shape[1]
            pose_landmarks = np.array([[lmk.x * frame_width, lmk.y * frame_height, lmk.z * frame_width]
                                       for lmk in pose_landmarks.landmark], dtype=np.float32)
            assert pose_landmarks.shape == (33, 3), 'Unexpected landmarks shape: {}'.format(pose_landmarks.shape)

            # 对当前帧进行姿势分类
            pose_classification = pose_classifier(pose_landmarks)

            # 使用EMA进行平滑
            pose_classification_filtered = pose_classification_filter(pose_classification)

            # 计数
            repetitions_count = repetition_counter(pose_classification_filtered)
        else:
            pose_classification = None
            pose_classification_filtered = pose_classification_filter(dict())
            pose_classification_filtered = None
            repetitions_count = repetition_counter.n_repeats

        # 绘制分类结果和计数
        output_frame = pose_classification_visualizer(
            frame=output_frame,
            pose_classification=pose_classification,
            pose_classification_filtered=pose_classification_filtered,
            repetitions_count=repetitions_count)

        cv2.imshow('video', cv2.cvtColor(np.array(output_frame), cv2.COLOR_RGB2BGR))
        out_video.write(cv2.cvtColor(np.array(output_frame), cv2.COLOR_RGB2BGR))
        if cv2.waitKey(1) in [ord('q'), 27]:
            break

    out_video.release()
    video_cap.release()
    cv2.destroyAllWindows()
    pose_tracker.close()

    if output_frame is not None:
        show_image(output_frame)
