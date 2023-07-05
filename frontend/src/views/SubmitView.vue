<template>
  <div>
    <form @submit.prevent="submitForm">
      <label for="userId">用户ID:</label>
      <input type="text" id="userId" v-model="formData.userId" required><br>
      <label for="testTime">体测时间:</label>
      <input type="datetime-local" id="testTime" v-model="formData.testTime" required><br>
      <label for="height">身高(cm):</label>
      <input type="number" id="height" v-model="formData.height" required><br>
      <label for="weight">体重(kg):</label>
      <input type="number" id="weight" v-model="formData.weight" required><br>
      <label for="vitalCapacity">肺活量(ml):</label>
      <input type="number" id="vitalCapacity" v-model="formData.vitalCapacity" required><br>
      <label for="standingLongJump">立定跳远(cm):</label>
      <input type="number" id="standingLongJump" v-model="formData.standingLongJump" required><br>
      <label for="sitAndReach">坐位体前屈(cm):</label>
      <input type="number" id="sitAndReach" v-model="formData.sitAndReach" required><br>
      <label for="pullOrSitUp">引体向上/仰卧起坐(time):</label>
      <input type="number" id="pullOrSitUp" v-model="formData.pullOrSitUp" required><br>
      <label for="fiftyM">50米跑(second):</label>
      <input type="number" id="fiftyM" v-model="formData.fiftyM" required><br>
      <label for="eightHundredOrThousandM">800或1000米(second):</label>
      <input type="number" id="eightHundredOrThousandM" v-model="formData.eightHundredOrThousandM" required><br>
      <button type="submit">提交</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        userId: '',
        testTime: '',
        height: null,
        weight: null,
        vitalCapacity: null,
        standingLongJump: null,
        sitAndReach: null,
        pullOrSitUp: null,
        fiftyM: null,
        eightHundredOrThousandM: null
      }
    };
  },
  methods: {
    submitForm() {
      // 将数据提交到服务器端
      // 使用formData对象中的数据发送POST请求到后端API，并将数据存储到数据库中
      // 在这里编写你的HTTP请求逻辑，使用Axios、Fetch API等库发送请求
    }
  }
};
</script>
