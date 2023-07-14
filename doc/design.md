# 前端设计

## 侧边栏

- 用户中心
- 体测中心
- 运动中心
- 成绩录入

## 具体设计

### 用户中心

- 体侧成绩查询
- 训练数据
- 登录登出

### 体测中心

里面加入几个模块类似下面的
<img src="C:\Users\13107\AppData\Roaming\Typora\typora-user-images\image-20230703115959997.png" alt="image-20230703115959997" style="zoom:50%;" />

### 运动中心

类似体测中心不过模块有些额外的

# 后台管理模块-admin-front

Python，操作界面

# 算法模块-algorithm

Python，实时分析视频流

## 计数动作

### 引体向上

mediapipe + knn

### 仰卧起坐

mediapipe + knn

### 深蹲

mediapipe + knn

### 俯卧撑

mediapipe + knn

## 其它动作

### 跳远

yolo + mediapipe

### 1km

人脸识别(face recognition / mediapipe?)

### 坐位体前屈

yolo + mediapipe

# 流媒体模块-stream-media

C++，流媒体服务

# 分析器模块-analyzer

C++, 核心模块，实时分析视频流

# 中期检查

- 前端Vue
  - 登录注册页
  - 体测成绩页
  - 身份验证页

- 逻辑后端SpringBoot
  - API接口设计
  - 统一前后端参数传递格式
  - 登录注册实现
  - 成绩上传/展示实现
  - 文件上传/下载实现
  - 人脸识别初步解决

- 数据收集
  - 收集各类运动的视频数据

- 算法Flask后端
  - 实现引体向上、俯卧撑、蹲起、仰卧起坐的识别与计数
  - 确定跳远和坐位体前屈的解决方案

- 推拉流服务
  - 实现简单的推拉流服务
  - 支持rstp，rtmp方式进行推流
  - 支持flv拉流，支持H256标准
  - 支持rtmp到flv流的协议转换

- 安卓
  - 实现身体关键点识别
  - 迁移算法后端的算法从Python到Kotlin
  - 实现引体向上的demo