<!-- 训练界面 -->
<template>
  <div class="main_container">
    <div class="left-panel">
      <div class="option_container">
        <div class="option_item">
          <label for="stream-code">选择摄像机:</label>
          <el-select
            v-model="selectedStreamCode"
            placeholder="请选择"
            class="selectedStreamCode"
          >
            <el-option
              v-for="code in streamCodes"
              :key="code"
              :value="code"
              :label="code"
            ></el-option>
          </el-select>
        </div>
        <div class="option_item">
          <label for="fitness-test">选择体测项目:</label>
          <el-select
            v-model="selectedFitnessTest"
            placeholder="请选择"
            class="selectedFitnessTest"
          >
            <el-option
              v-for="test in fitnessTests"
              :key="test"
              :value="test"
              :label="test"
            ></el-option>
          </el-select>
        </div>
        <div class="option_item">
          <label for="time">选择训练时长（单位：秒）:</label>
          <el-select v-model="selectedTestTime" placeholder="请选择">
            <el-option
              v-for="time in TestTime"
              :key="time"
              :value="time"
              :label="time"
            ></el-option>
          </el-select>
        </div>
      </div>
      <div class="center-panel">
        <el-button
          type="primary"
          :disabled="
            !selectedStreamCode || !selectedFitnessTest || !selectedTestTime
          "
          @click="startFitnessTest"
          >开始体测</el-button
        >
      </div>
    </div>
    <div class="right-panel">
      <div class="result_container">
        <h3>测试结果值:</h3>
        <ul>
          <li v-for="result in testResults" :key="result">{{ result }}</li>
        </ul>
      </div>
      <div class="video_container">
        <video ref="videoElement" controls width="250"></video>
      </div>
      <div class="upload_container">
        <el-button
          type="primary"
          :disabled="testResults === null"
          @click="showConfirmationDialog"
        >
          上传成绩
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import flvjs from "flv.js";
import {
  ElButton,
  ElSelect,
  ElOption,
  ElMessage,
  ElMessageBox,
} from "element-plus";

export default {
  name: "FitnessTest",
  components: {
    ElButton,
    ElSelect,
    ElOption,
  },
  data() {
    return {
      streamCodes: ["摄像机1", "摄像机2", "摄像机3", "摄像机4"],
      fitnessTests: [
        "引体向上", //pullUp
        "仰卧起坐", //sitUp
        "深蹲", //squat
        "俯卧撑", //pushUp
      ],
      TestTime: [30, 60, 90, 120],
      selectedStreamCode: null,
      selectedFitnessTest: null,
      selectedTestTime: null,
      testResults: null,
    };
  },

  methods: {
    showConfirmationDialog() {
      ElMessageBox.confirm("确认上传成绩?", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.uploadTestResults();
        })
        .catch(() => {
          // Cancel upload
        });
    },
    uploadTestResults() {
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id;
      let actionName = this.selectedFitnessTest;
      switch (this.selectedFitnessTest) {
        case "引体向上":
          actionName = "pullUp";
          break;
        case "仰卧起坐":
          actionName = "sitUp";
          break;
        case "深蹲":
          actionName = "squat";
          break;
        case "俯卧撑":
          actionName = "pushUp";
          break;
      }
      const data = {
        item: actionName,
        id: userId,
        number: this.testResults,
        train_time: this.selectedTestTime,
      };

      console.log(
        "体测项目/用户ID/测试结果为" + actionName + userId + this.testResults
      );

      axios
        .post("http://127.0.0.1:9090/uploadTrainGrade", data)
        .then((response) => {
          console.log(response);
          ElMessage.success("成绩上传成功");
        })
        .catch((error) => {
          console.error(error);
          ElMessage.error("成绩上传失败");
        });
    },
    startFitnessTest() {
      if (this.selectedStreamCode && this.selectedFitnessTest) {
        ElMessageBox.confirm("确认开始体测?", "提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let streamCode = null;
            let actionName = null;

            switch (this.selectedStreamCode) {
              case "摄像机1":
                streamCode = 100;
                break;
              case "摄像机2":
                streamCode = 101;
                break;
              case "摄像机3":
                streamCode = 102;
                break;
              case "摄像机4":
                streamCode = 103;
                break;
            }

            switch (this.selectedFitnessTest) {
              case "引体向上":
                actionName = "pullUp";
                break;
              case "仰卧起坐":
                actionName = "sitUp";
                break;
              case "深蹲":
                actionName = "squat";
                break;
              case "俯卧撑":
                actionName = "pushUp";
                break;
            }

            const data = {
              streamCode: streamCode,
              actionName: actionName,
              train_time: this.selectedTestTime,
            };

            console.log(
              "推流码/体测项目/训练时间为" +
                streamCode +
                actionName +
                this.selectedTestTime
            );

            let videoElement = this.$refs.videoElement;
            let flvPlayer = flvjs.createPlayer({
              type: "flv",
              url: `http://39.106.13.47:8080/live/${streamCode}.live.flv`,
            });
            flvPlayer.attachMediaElement(videoElement);
            flvPlayer.load();
            flvPlayer.play();

            axios
              .post("http://127.0.0.1:9090/stream/limitTime", data)
              .then((response) => {
                console.log(response);
                this.testResults = response.data.data;
              })
              .catch((error) => {
                console.error(error);
              });
          })
          .catch(() => {
            // Cancel fitness test
          });
      } else {
        ElMessage.error("请选择推流码、体测项目和训练时长");
      }
    },
  },
};
</script>

<style scoped>
.main_container {
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 80vh;
  margin-top: 90px;
}

.left-panel,
.right-panel {
  width: 50%;
  height: 80vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.option_item {
  padding: 50px;
}

.selectedStreamCode {
  margin-left: 110px;
}

.selectedFitnessTest {
  margin-left: 95px;
}

.left-panel label,
.right-panel label {
  margin-right: 10px;
}

.center-panel {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.center-panel .el-button {
  width: 150px;
}

.video_container {
  max-width: 100%;
  max-height: 300px;
}

.result_container {
  display: flex;
  padding: 20px;
}

.upload_container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px;
}

.upload_container .el-button {
  width: 150px;
}
</style>
