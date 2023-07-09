# docker-compose 启动命令

docker-compose up -d


# 20220819更新信息

## 数据集增加至66个

## videcapture.py文件更改

70-73行阈值修改

## videoprocess.py文件更改

70-73行阈值修改

## poseembedding.py文件增加

146-169行增加更多距离信息

# 20220809更新信息

## 优化计数阈值，动作识别更加精准

## 优化matplotlib内存使用，不再出现内存溢出

# 文件信息

**代码根目录**：code

**模型**：mediapipe的blazepose模型为直接调用api使用，在videocapture(调用摄像头进行动作评估)和videoprocess(直接对本地文件进行评估)使用
KNN分类模型在poseclassifier中使用

**原始数据**：为fitness_poses_images_in中的pullUps_up中的13中图片，pullUps_down为测试用实际并没有用到

**标记数据**：为fitness_poses_csvs_out中的pullUps_up.csv文件（已经使用KNN分类后的训练集)

# 主要修改内容

不同的动作评估(class_name)在poseembedding.py中的_get_pose_center, _get_pose_size,_get_pose_distance_embedding也会不同

在videocapture.py与videoprocess.py中的class_name修改为对应动作名，在此处有注释

# 如何运行

使用pycharm运行main.py文件

python 3.10, 以下为该项目库文件版本（**加粗**为关建库）

absl-py==1.2.0
attrs==22.1.0
cachetools==5.2.0
certifi==2022.6.15
charset-normalizer==2.1.0
colorama==0.4.5
cycler==0.11.0
dlengine==0.1.0
fonttools==4.34.4
google-auth==2.9.1
google-auth-oauthlib==0.4.6
grpcio==1.48.0
idna==3.3
kiwisolver==1.4.4
Markdown==3.4.1
MarkupSafe==2.1.1
**matplotlib==3.5.2**
**mediapipe==0.8.10.1**
**numpy==1.23.1**
oauthlib==3.2.0
**opencv-contrib-python==4.6.0.66**
**opencv-python==4.6.0.66**
packaging==21.3
Pillow==9.2.0
protobuf==3.19.4
pyasn1==0.4.8
pyasn1-modules==0.2.8
pyparsing==3.0.9
python-dateutil==2.8.2
requests==2.28.1
requests-oauthlib==1.3.1
rsa==4.9
scipy==1.9.0
six==1.16.0
tensorboard==2.9.1
tensorboard-data-server==0.6.1
tensorboard-plugin-wit==1.8.1
torch==1.12.0+cu116
torchaudio==0.12.0+cu116
torchvision==0.13.0+cu116
**tqdm==4.64.0**
typing_extensions==4.3.0
urllib3==1.26.11
Werkzeug==2.2.1