import cv2
import mediapipe as mp
import numpy as np


def sit_up_video_stream(video_steam_url):
    # 类名命名
    class_name = 'up'

    # 获取包含姿势类别的csv文件
    pose_samples_folder = 'sports_sitUp/fitness_poses_csvs_out'
    '''
    对所需要的所有组件进行初始化
    '''
    # 初始化姿势跟踪器、姿势嵌入器、姿势分类器、EMA平滑、动作计数器、可视化模块、动作分类器
    # 姿势跟踪器
    import mediapipe.python.solutions.pose as my_pose
    pose_tracker = my_pose.Pose()

    # 姿势嵌入器
    from sports_sitUp import poseembedding as pe
    pose_embedder = pe.FullBodyPoseEmbedder()

    # 姿势分类器
    from sports_sitUp import poseclassifier as pc
    pose_classifier = pc.PoseClassifier(
        pose_samples_folder=pose_samples_folder,
        pose_embedder=pose_embedder,
        top_n_by_max_distance=30,
        top_n_by_mean_distance=10
    )

    # EMA平滑
    from sports_sitUp import resultsmooth as rs
    pose_classification_filter = rs.EMADictSmoothing(
        window_size=10,
        alpha=0.2
    )

    # 动作计数器
    from sports_sitUp import counter
    repetition_counter = counter.RepetitionCounter(
        class_name=class_name,
        enter_threshold=5,
        exit_threshold=4
    )

    # 可视化模块
    from sports_sitUp import visualizer as vs
    pose_classification_visualizer = vs.PoseClassificationVisualizer(
        class_name=class_name,
        plot_y_max=15
    )

    '''
    进行视频流处理
    '''
    # 打开视频流
    cap = cv2.VideoCapture(video_steam_url)
    if not cap.isOpened():
        print("无法打开该视频流")

    # 初始化Mediapipe姿势检测器
    my_pose = mp.solutions.pose.Pose(static_image_mode=False, min_detection_confidence=0.5)
    mp_drawing = mp.solutions.drawing_utils

    # 读取并且处理视频流的每一帧
    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # 图片格式从BGR转换为RGB
        input_frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        # 进行姿态检测
        results = my_pose.process(input_frame_rgb)

        # 获取姿势关键点
        pose_landmarks = results.pose_landmarks
        output_frame_rgb = frame.copy()

        # 在图片中绘制关键点
        if pose_landmarks is not None:
            mp_drawing.draw_landmarks(
                frame,
                pose_landmarks,
                mp.solutions.pose.POSE_CONNECTIONS  # 使用POSE_CONNECTIONS 链接各个标记点
            )

        if pose_landmarks is not None:
            # 获取关键点坐标
            frame_height, frame_width = output_frame_rgb.shape[0], output_frame_rgb.shape[1]
            pose_landmarks = np.array([[lmk.x * frame_width, lmk.y * frame_height, lmk.z * frame_width]
                                       for lmk in pose_landmarks.landmark], dtype=np.float32)
            assert pose_landmarks.shape == (33, 3), 'Unexpected landmarks shape: {}'.format(pose_landmarks.shape)

        '''
        关键点识别处理
        '''
        # 动作分类
        pose_classification = pose_classifier(pose_landmarks)

        # 使用EMA进行平滑处理
        pose_classification_filtered = pose_classification_filter(pose_classification)

        # 计数
        repetition_count = repetition_counter(pose_classification_filtered)

        # 根据图像计算俯卧撑个数
        cv2.putText(frame, f"Sit-ups: {repetition_count}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.imshow("Viedo Stream", frame)

        # 释放资源
        del input_frame_rgb
        del results

        # 单击"q"键退出循环
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
    cap.release()
    cv2.destroyAllWindows()

    return repetition_count


