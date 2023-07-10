<template>
  <div class="camera_outer">
    <div class="left_section">
      <h2>上传身份证照片</h2>
      <div class="upload_button_container">
        <input
          type="file"
          accept="image/*"
          @change="selectIdCardImage"
          ref="idCardInput"
          style="display: none"
        />
        <button @click="openIdCardInput" class="upload_button">
          选择身份证照片
        </button>
        <button @click="uploadIdCard" class="upload_button">
          上传身份证照片
        </button>
      </div>
      <div v-if="idCardImage" class="img_preview">
        <img :src="idCardImage" alt="身份证照片预览" />
      </div>
    </div>
    <div class="right_section">
      <h2>拍摄照片</h2>
      <video
        id="videoCamera"
        :width="videoWidth"
        :height="videoHeight"
        autoplay
      ></video>
      <div class="upload_button_container">
        <button @click="getCompetence" class="upload_button">打开摄像头</button>
        <button @click="captureImage" class="upload_button">拍照</button>
        <button @click="uploadPhoto" class="upload_button">上传照片</button>
      </div>
      <div v-if="cameraImage" class="img_preview">
        <img :src="cameraImage" alt="拍摄照片预览" />
      </div>
    </div>
    <div class="submit_section">
      <button
        @click="submitAuthentication"
        class="submit_button"
        :disabled="!idCardImage || !cameraImage"
      >
        提交认证
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      videoWidth: 250,
      videoHeight: 350,
      idCardImage: null,
      cameraImage: null,
      idCardImageData: null,
      cameraImageData: null,
    };
  },
  methods: {
    // 调用权限（打开摄像头功能）
    getCompetence() {
      const video = document.getElementById("videoCamera");

      // 检查浏览器兼容性
      navigator.mediaDevices
        .getUserMedia({ video: true })
        .then((stream) => {
          video.srcObject = stream;
          video.play();
        })
        .catch((error) => {
          console.error("无法打开摄像头：", error);
        });
    },
    // 绘制图片（拍照功能）
    captureImage() {
      const video = document.getElementById("videoCamera");
      const canvas = document.createElement("canvas");
      const context = canvas.getContext("2d");
      canvas.width = this.videoWidth;
      canvas.height = this.videoHeight;

      // 绘制视频画面到canvas
      context.drawImage(video, 0, 0, canvas.width, canvas.height);

      // 获取canvas上的图像数据并转换为base64
      const image = canvas.toDataURL("image/png");
      this.cameraImage = image; // 赋值并预览图片
      this.cameraImageData = this.dataURLtoFile(image, "camera_image.png"); // 转换为File对象并保存图像数据
    },
    // 从本地选择身份证照片
    selectIdCardImage(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = () => {
          this.idCardImage = reader.result; // 赋值并预览图片
          this.idCardImageData = file; // 保存图像数据
        };
        reader.readAsDataURL(file);
      }
    },
    // 打开文件选择器（选择身份证照片）
    openIdCardInput() {
      this.$refs.idCardInput.click();
    },
    // 上传身份证照片
    uploadIdCard() {
      // 获取身份证照片数据
      const imageData = this.idCardImageData;

      // 构建FormData对象
      const formData = new FormData();
      formData.append("file", imageData);

      // 发送POST请求到身份证照片上传的后端API
      axios
        .post("http://your-backend-api-url/upload/idcard", formData)
        .then((response) => {
          // 上传成功的处理逻辑
          console.log("身份证照片上传成功！");
          console.log(response);
        })
        .catch((error) => {
          // 上传失败的处理逻辑
          console.error("身份证照片上传失败！", error);
        });
    },
    // 上传拍摄的照片
    uploadPhoto() {
      // 获取拍摄照片数据
      const imageData = this.cameraImageData;

      // 构建FormData对象
      const formData = new FormData();
      formData.append("file", imageData);

      // 发送POST请求到拍摄照片上传的后端API
      axios
        .post("http://your-backend-api-url/upload/camera", formData)
        .then((response) => {
          // 上传成功的处理逻辑
          console.log("拍摄照片上传成功！");
          console.log(response);
        })
        .catch((error) => {
          // 上传失败的处理逻辑
          console.error("拍摄照片上传失败！", error);
        });
    },
    // 提交认证
    submitAuthentication() {
      // 判断两个照片是否都已上传，如果上传则调用认证API
      if (this.idCardImageData && this.cameraImageData) {
        // 调用认证API，传递身份证照片和拍摄照片数据
        const formData = new FormData();
        formData.append("idCardImage", this.idCardImageData);
        formData.append("cameraImage", this.cameraImageData);

        axios
          .post("http://your-backend-api-url/authentication", formData)
          .then((response) => {
            // 认证成功的处理逻辑
            console.log("认证成功！");
            console.log(response);
          })
          .catch((error) => {
            // 认证失败的处理逻辑
            console.error("认证失败！", error);
          });
      }
    },
    // 将base64数据转换为File对象
    dataURLtoFile(dataURL, filename) {
      const arr = dataURL.split(",");
      const mime = arr[0].match(/:(.*?);/)[1];
      const bstr = atob(arr[1]);
      let n = bstr.length;
      const u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, { type: mime });
    },
  },
};
</script>

<style scoped>
.camera_outer {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.left_section,
.right_section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload_button_container {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.img_preview {
  margin-top: 20px;
}

.submit_section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.submit_button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: #fff;
  font-weight: bold;
  border: none;
  cursor: pointer;
}

.submit_button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
