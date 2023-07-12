<template>
  <div class="option_container">
    <div class="left-panel">
      <div class="option_item">
        <label for="stream-code">选择推流码:</label>
        <el-select v-model="selectedStreamCode" placeholder="请选择">
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
        <el-select v-model="selectedFitnessTest" placeholder="请选择">
          <el-option
            v-for="test in fitnessTests"
            :key="test"
            :value="test"
            :label="test"
          ></el-option>
        </el-select>
      </div>
      <div class="center-panel">
        <el-button
          type="primary"
          :disabled="!selectedStreamCode || !selectedFitnessTest"
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
        <video ref="videoElement" controls></video>
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
      streamCodes: Array.from({ length: 4 }, (_, index) => 100 + index),
      fitnessTests: [
        "引体向上",
        "仰卧起坐",
        "深蹲",
        "俯卧撑",
        "跳远",
        "800/1000米",
        "坐位体前屈",
      ],
      selectedStreamCode: null,
      selectedFitnessTest: null,
      testResults: [],
    };
  },
  mounted() {
    if (flvjs.isSupported()) {
      let videoElement = this.$refs.videoElement;
      let flvPlayer = flvjs.createPlayer({
        type: "flv",
        url: "http://39.106.13.47:8080/live/100.live.flv",
      });
      flvPlayer.attachMediaElement(videoElement);
      flvPlayer.load();
      flvPlayer.play();
    }
  },
  methods: {
    startFitnessTest() {
      if (this.selectedStreamCode && this.selectedFitnessTest) {
        ElMessageBox.confirm("确认开始体测?", "提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            const data = {
              streamCode: this.selectedStreamCode,
              actionName: this.selectedFitnessTest,
            };

            console.log(
              "推流码和体测项目为" +
                this.selectedStreamCode +
                this.selectedFitnessTest
            );
            axios
              .post("http://127.0.0.1:9090/stream/limitTime", data)
              .then((response) => {
                console.log(response);
                this.testResults = response.data.count;
              })
              .catch((error) => {
                console.error(error);
              });
          })
          .catch(() => {
            // Cancel fitness test
          });
      } else {
        ElMessage.error("请选择推流码和体测项目");
      }
    },
  },
};
</script>

<style scoped>
.option_container {
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
</style>
