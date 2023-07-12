<template>
  <div class="main">
    <div class="image_container">
      <div class="left_section">
        <div v-if="idCardImage" class="img_preview">
          <img :src="idCardImage" alt="身份证照片预览" class="img_content" />
        </div>
        <h2>上传身份证照片</h2>
        <div class="upload_button_container">
          <input
            type="file"
            accept="image/*"
            @change="selectIdCardImage"
            ref="idCardInput"
            style="display: none"
          />
          <el-button @click="openIdCardInput" class="upload_button">
            选择身份证照片
          </el-button>
          <el-button @click="openUploadIDcardDialog" class="upload_button">
            上传身份证照片
          </el-button>
        </div>
      </div>
      <div class="right_section">
        <video
          id="videoCamera"
          :width="videoWidth"
          :height="videoHeight"
          autoplay
        ></video>
        <div v-if="cameraImage" class="img_preview">
          <img :src="cameraImage" alt="拍摄照片预览" class="img_content" />
        </div>
        <h2>拍摄照片</h2>
        <div class="upload_button_container">
          <el-button @click="toggleCamera" class="upload_button">
            {{ isCameraOpen ? "关闭摄像头" : "打开摄像头" }}
          </el-button>
          <el-button @click="captureImage" class="upload_button"
            >拍照</el-button
          >
          <el-button @click="openUploadPhotoDialog" class="upload_button">
            上传照片
          </el-button>
        </div>
      </div>
    </div>
    <div class="submit_section">
      <el-button
        @click="submitAuthentication"
        class="submit_button"
        :disabled="!idCardImage || !cameraImage"
      >
        提交认证
      </el-button>
    </div>

    <el-dialog title="确认上传" v-model="dialogVisible_1" width="30%">
      <template #default>
        <span>确定要上传身份证吗？</span>
      </template>
      <template #footer>
        <el-button @click="uploadIdCard">确定</el-button>
        <el-button @click="cancelIDcardUpload">取消</el-button>
      </template>
    </el-dialog>

    <el-dialog title="确认上传" v-model="dialogVisible_2" width="30%">
      <template #default>
        <span>确定要上传照片吗？</span>
      </template>
      <template #footer>
        <el-button @click="uploadPhoto">确定</el-button>
        <el-button @click="cancelPhotoUpload">取消</el-button>
      </template>
    </el-dialog>

    <el-dialog title="认证结果" v-model="dialogVisible_3" width="20%">
      <template #default>
        <span>认证通过</span>
      </template>
      <template #footer>
        <el-button @click="comfirm_3">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog title="认证结果" v-model="dialogVisible_4" width="20%">
      <template #default>
        <span>认证未通过</span>
      </template>
      <template #footer>
        <el-button @click="comfirm_4">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      videoWidth: 250,
      videoHeight: 250,
      idCardImage: null,
      cameraImage: null,
      idCardImageData: null,
      cameraImageData: null,
      isCameraOpen: false,
      dialogVisible_1: false,
      dialogVisible_2: false,
      dialogVisible_3: false,
      dialogVisible_4: false,
    };
  },
  methods: {
    // 打开上传身份证对话框
    openUploadIDcardDialog() {
      this.dialogVisible_1 = true;
    },
    // 取消上传身份证
    cancelIDcardUpload() {
      this.dialogVisible_1 = false;
    },
    // 打开上传摄像头照片对话框
    openUploadPhotoDialog() {
      this.dialogVisible_2 = true;
    },
    // 取消上传摄像头照片
    cancelPhotoUpload() {
      this.dialogVisible_2 = false;
    },
    comfirm_3() {
      this.dialogVisible_3 = false;
    },
    comfirm_4() {
      this.dialogVisible_4 = false;
    },
    captureImage() {
      const video = document.getElementById("videoCamera");
      const canvas = document.createElement("canvas");
      const context = canvas.getContext("2d");
      // this.videoWidth = video.videoWidth;
      // this.videoHeight = video.videoHeight;
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;

      context.drawImage(video, 0, 0, canvas.width, canvas.height);

      const image = canvas.toDataURL("image/png");
      this.cameraImage = image;
      this.cameraImageData = this.dataURLtoFile(image, "camera_image.png");
    },
    selectIdCardImage(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = () => {
          this.idCardImage = reader.result;
          this.idCardImageData = file;
        };
        reader.readAsDataURL(file);
      }
    },
    openIdCardInput() {
      this.$refs.idCardInput.click();
    },
    uploadIdCard() {
      const imageData = this.idCardImageData;
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id;

      const formData = new FormData();
      formData.append("file", imageData);
      formData.append("userId", userId);
      formData.append("prefix", "/action/userImages");

      axios
        .post("http://127.0.0.1:9090/upload", formData)
        .then((response) => {
          console.log("身份证照片上传成功！");
          console.log(response);
        })
        .catch((error) => {
          console.error("身份证照片上传失败！", error);
        })
        .finally(() => {
          // 上传完成后，关闭对话框
          this.dialogVisible_1 = false;
        });
    },
    uploadPhoto() {
      const imageData = this.cameraImageData;
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id;

      const formData = new FormData();
      formData.append("file", imageData);
      formData.append("userId", userId);
      formData.append("prefix", "/action/checkImages");

      axios
        .post("http://127.0.0.1:9090/upload", formData)
        .then((response) => {
          console.log("拍摄照片上传成功！");
          console.log(response);
        })
        .catch((error) => {
          console.error("拍摄照片上传失败！", error);
        })
        .finally(() => {
          // 上传完成后，关闭对话框
          this.dialogVisible_2 = false;
        });
    },
    //提交认证
    submitAuthentication() {
      if (this.idCardImageData && this.cameraImageData) {
        const userJson = sessionStorage.getItem("user");
        const user = JSON.parse(userJson);
        const userId = user.user_id;
        const requestData = {
          userId: userId,
        };

        console.log("userid为=================》" + userId);

        axios
          .post("http://127.0.0.1:9090/auth", requestData)
          .then((response) => {
            console.log("认证成功！");
            console.log(response);
            if (response.data.result.score > 60) {
              this.authResult = "认证通过";
              this.dialogVisible_3 = true;
            } else {
              this.authResult = "认证未通过";
              this.dialogVisible_4 = true;
            }
          })
          .catch((error) => {
            console.error("认证失败！", error);
          });
      }
    },
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
    //开关摄像头
    toggleCamera() {
      const video = document.getElementById("videoCamera");
      const stream = video.srcObject;

      if (this.isCameraOpen) {
        if (stream && stream.getTracks) {
          const tracks = stream.getTracks();

          tracks.forEach((track) => {
            track.stop();
          });

          video.srcObject = null;

          this.isCameraOpen = false;
        }
      } else {
        navigator.mediaDevices
          .getUserMedia({ video: true })
          .then((stream) => {
            video.srcObject = stream;
            video.play();
            this.isCameraOpen = true;
          })
          .catch((error) => {
            console.error("无法打开摄像头：", error);
          });
      }
    },
  },
};
</script>

<style scoped>
.main {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 90vh;
}

.image_container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.left_section {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;
  height: 80%;
  margin-right: 100px;
  margin-left: 20px;
}

.right_section {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;
  height: 80%;
  margin-right: 20px;
  margin-left: 100px;
}

.upload_button_container {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.img_preview {
  width: 100%;
  /*background: #f2f2f2;*/
  display: flex;
  justify-content: center;
  align-items: center;
  .img_content {
    max-width: 100%;
    max-height: 200px;
  }
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
