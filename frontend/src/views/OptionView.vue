<template>
  <div class="container">
    <div class="left-panel">
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
    <div class="right-panel">
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
</template>

<script>
// import { ref } from "vue";
import axios from "axios";

export default {
  name: "FitnessTest",
  data() {
    return {
      streamCodes: Array.from({ length: 20 }, (_, index) => index + 1),
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
    };
  },
  methods: {
    startFitnessTest() {
      if (this.selectedStreamCode && this.selectedFitnessTest) {
        this.$confirm("确认开始体测?", "提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            // 发送选择的推流码和体测项目到后端
            const data = {
              streamCode: this.selectedStreamCode,
              fitnessTest: this.selectedFitnessTest,
            };
            axios
              .post("/api/start-fitness-test", data)
              .then((response) => {
                console.log(response);
              })
              .catch((error) => {
                console.error(error);
              });
          })
          .catch(() => {
            // 取消开始体测
          });
      } else {
        this.$message.error("请选择推流码和体测项目");
      }
    },
  },
};
</script>

<style>
.container {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 80vh;
  margin-top: 90px;
}

.left-panel,
.right-panel {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
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
</style>
