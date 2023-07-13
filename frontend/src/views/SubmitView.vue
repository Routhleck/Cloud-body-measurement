<!-- 体测成绩上传 -->
<template>
  <div class="form-container">
    <div class="form">
      <el-form ref="form" label-width="200px" label-position="left">
        <div class="form-item">
          <el-form-item label="用户ID" required>
            <el-input v-model="formData.userId"></el-input>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="身高(cm)">
            <el-input-number v-model="formData.height"></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="体重(kg)">
            <el-input-number v-model="formData.weight"></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="肺活量(ml)">
            <el-input-number v-model="formData.vitalCapacity"></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="立定跳远(cm)">
            <el-input-number
              v-model="formData.standingLongJump"
            ></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="坐位体前屈(cm)">
            <el-input-number v-model="formData.sitAndReach"></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="引体向上/仰卧起坐(time)">
            <el-input-number v-model="formData.pullOrSitUp"></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="50米跑(second)">
            <el-input-number v-model="formData.fiftyM"></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item label="800或1000米(second)">
            <el-input-number
              v-model="formData.eightHundredOrThousandM"
            ></el-input-number>
          </el-form-item>
        </div>
        <div class="form-item">
          <el-form-item>
            <el-button type="primary" native-type="submit">提交</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      formData: {
        userId: "",
        height: null,
        weight: null,
        vitalCapacity: null,
        standingLongJump: null,
        sitAndReach: null,
        pullOrSitUp: null,
        fiftyM: null,
        eightHundredOrThousandM: null,
      },
    };
  },
  methods: {
    submitForm() {
      // 将数据提交到服务器端
      // 使用formData对象中的数据发送POST请求到后端API，并将数据存储到数据库中

      // 构造要提交的数据对象
      const data = {
        user_id: this.formData.userId,
        test_time: this.formData.testTime,
        height: this.formData.height,
        weight: this.formData.weight,
        vital_capacity: this.formData.vitalCapacity,
        standing_long_jump: this.formData.standingLongJump,
        sit_and_reach: this.formData.sitAndReach,
        pull_or_sit_up: this.formData.pullOrSitUp,
        "50m": this.formData.fiftyM,
        "800_or_1000m": this.formData.eightHundredOrThousandM,
      };

      // 发送POST请求到后端API
      axios
        .post("/api/submitTest", data)
        .then((response) => {
          // 请求成功处理逻辑
          console.log("数据提交成功");
          console.log(response);
        })
        .catch((error) => {
          // 请求失败处理逻辑
          console.error("数据提交失败:", error);
        });
    },
  },
};
</script>

<style scoped>
.form-container {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 150px;
  height: 100vh;
}

.form {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
}
</style>
