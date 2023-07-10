<template>
  <div class="camera_outer">
    <video
      id="videoCamera"
      :width="videoWidth"
      :height="videoHeight"
      autoplay
    ></video>
    <canvas
      style="display: none"
      id="canvasCamera"
      :width="videoWidth"
      :height="videoHeight"
    ></canvas>

    <div v-if="imgSrc" class="img_bg_camera">
      <p>效果预览</p>
      <img :src="imgSrc" alt class="tx_img" />
    </div>

    <div class="button_container">
      <div class="button">
        <el-button @click="getCompetence()">打开摄像头</el-button>
        <el-button @click="stopNavigator()">关闭摄像头</el-button>
        <el-button @click="setImage()">拍照</el-button>
      </div>

      <div>
        <el-button @click="openUploadDialog" class="upload">上传照片</el-button>
      </div>
    </div>

    <el-dialog title="确认上传" v-model="dialogVisible" width="30%">
      <template #default>
        <span>确定要上传照片吗？</span>
      </template>
      <template #footer>
        <el-button @click="uploadPhoto">确定</el-button>
        <el-button @click="cancelUpload">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

function dataURLtoFile(dataURL, filename) {
  const arr = dataURL.split(",");
  const mime = arr[0].match(/:(.*?);/)[1];
  const bstr = atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], filename, { type: mime });
}

export default {
  data() {
    return {
      videoWidth: 250,
      videoHeight: 350,
      imgSrc: "",
      thisCancas: null,
      thisContext: null,
      thisVideo: null,
      openVideo: false,
      dialogVisible: false,
      capturedImageData: null,
    };
  },
  mounted() {
    // this.getCompetence()//进入页面就调用摄像头
  },
  methods: {
    // 调用权限（打开摄像头功能）
    getCompetence() {
      var _this = this;
      _this.thisCancas = document.getElementById("canvasCamera");
      _this.thisContext = this.thisCancas.getContext("2d");
      _this.thisVideo = document.getElementById("videoCamera");
      _this.thisVideo.style.display = "block";
      // 获取媒体属性，旧版本浏览器可能不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
      }
      // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
      // 使用getUserMedia，因为它会覆盖现有的属性。
      // 这里，如果缺少getUserMedia属性，就添加它。
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          // 首先获取现存的getUserMedia(如果存在)
          var getUserMedia =
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.getUserMedia;
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {
            // 不存在则报错
            return Promise.reject(
              new Error("getUserMedia is not implemented in this browser")
            );
          }
          // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject);
          });
        };
      }
      var constraints = {
        audio: false,
        video: {
          width: this.videoWidth,
          height: this.videoHeight,
          transform: "scaleX(-1)",
        },
      };
      navigator.mediaDevices
        .getUserMedia(constraints)
        .then(function (stream) {
          // 旧的浏览器可能没有srcObject
          if ("srcObject" in _this.thisVideo) {
            _this.thisVideo.srcObject = stream;
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.thisVideo.src = window.URL.createObjectURL(stream);
          }
          _this.thisVideo.onloadedmetadata = function () {
            _this.thisVideo.play();
          };
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //  绘制图片（拍照功能）
    setImage() {
      var _this = this;
      // canvas画图
      _this.thisContext.drawImage(
        _this.thisVideo,
        0,
        0,
        _this.videoWidth,
        _this.videoHeight
      );
      // 获取图片base64链接
      var image = this.thisCancas.toDataURL("image/png");
      _this.imgSrc = image; //赋值并预览图片
      _this.capturedImageData = dataURLtoFile(image, "captured_image.png"); // 转换为File对象并保存图像数据
    },
    // 关闭摄像头
    stopNavigator() {
      this.thisVideo.srcObject.getTracks()[0].stop();
    },
    // 打开上传照片对话框
    openUploadDialog() {
      this.dialogVisible = true;
    },
    // 取消上传照片
    cancelUpload() {
      this.dialogVisible = false;
    },
    // 上传照片
    uploadPhoto() {
      // 获取图像数据
      const imageData = this.capturedImageData;
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id;

      // 构建FormData对象
      const formData = new FormData();
      formData.append("file", imageData);
      formData.append("userId", userId); // 替换为实际的用户ID
      formData.append("prefix", "images"); // 替换为实际的文件前缀

      // 发送POST请求
      axios
        .post("/upload", formData)
        .then((response) => {
          // 上传成功的处理逻辑
          console.log("照片上传成功！");
          console.log(response);
        })
        .catch((error) => {
          // 上传失败的处理逻辑
          console.error("照片上传失败！", error);
        })
        .finally(() => {
          // 上传完成后，关闭对话框
          this.dialogVisible = false;
        });
    },
  },
};
</script>

<style scoped>
.camera_outer {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.button_container {
  max-width: 100%;
  margin: 50px;
  padding: 50px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.upload {
  margin: 20px;
  padding: 5px;
}
</style>
