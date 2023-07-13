<!-- 训练数据展示 -->
<template>
  <div class="element-main">
    <div class="select-container">
      <span class="select-label">请选择运动项目：</span>
      <el-select
        v-model="selectedSport"
        placeholder="请选择运动项目"
        class="select"
      >
        <el-option value="" label="全部项目" />
        <el-option
          v-for="sport in sports"
          :key="sport"
          :value="sport"
          :label="sport"
        ></el-option>
      </el-select>
      <el-button type="primary" @click="handleQuery" class="query-button">
        查询
      </el-button>
    </div>

    <div v-if="totalDuration" class="total-duration">
      用户运动总时长: {{ totalDuration }} 分钟
    </div>

    <el-table
      v-if="tableData.length > 0"
      :data="filteredTableData"
      style="width: 80%"
    >
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
      >
        <template v-slot="{ row }">
          {{ row[column.prop] }}
        </template>
      </el-table-column>
    </el-table>
    <div v-else class="no-data">暂无数据</div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      selectedSport: null,
      sports: ["引体向上", "俯卧撑", "仰卧起坐", "深蹲"],
      tableData: [
        { time: "2022/1/1", sport: "引体向上", duration: 10, result: "10次" },
        { time: "2022/1/1", sport: "俯卧撑", duration: 15, result: "15次" },
        { time: "2022/1/1", sport: "仰卧起坐", duration: 8, result: "8次" },
        { time: "2022/1/1", sport: "深蹲", duration: 12, result: "12次" },
        { time: "2022/1/1", sport: "引体向上", duration: 12, result: "12次" },
        { time: "2022/1/1", sport: "俯卧撑", duration: 18, result: "18次" },
        { time: "2022/1/1", sport: "仰卧起坐", duration: 10, result: "10次" },
        { time: "2022/1/1", sport: "深蹲", duration: 14, result: "14次" },
        { time: "2022/1/1", sport: "引体向上", duration: 8, result: "8次" },
        { time: "2022/1/1", sport: "俯卧撑", duration: 14, result: "14次" },
        { time: "2022/1/1", sport: "仰卧起坐", duration: 6, result: "6次" },
        { time: "2022/1/1", sport: "深蹲", duration: 10, result: "10次" },
      ],
      filteredTableData: [],
      columns: [
        { prop: "time", label: "运动时间" },
        { prop: "sport", label: "运动项目" },
        { prop: "duration", label: "运动时长" },
        { prop: "result", label: "运动结果" },
      ],
      totalDuration: 600,
    };
  },
  methods: {
    async fetchData() {
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id;
      const data = {
        userId: userId,
      };
      try {
        const response = await axios.get("http://127.0.0.1:9090/sports", data);
        this.tableData = response.data.tableData;
        this.totalDuration = response.data.totalDuration;
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    },
    handleQuery() {
      if (this.selectedSport === "") {
        this.filteredTableData = this.tableData;
      } else if (this.selectedSport) {
        const filteredData = this.tableData.filter(
          (item) => item.sport === this.selectedSport
        );
        this.filteredTableData = filteredData;
      } else {
        this.filteredTableData = [];
      }
    },
  },

  mounted() {
    this.fetchData();
  },
};
</script>

<style scoped>
.element-main {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
}

.select-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  margin-top: 20px;
}

.select-label {
  margin-right: 10px;
}

.select {
  width: 120px;
  margin-right: 10px;
}

.query-button {
  width: 80px;
}

.no-data {
  margin-top: 20px;
  color: #999;
}

.total-duration {
  margin-top: 10px;
  margin-bottom: 10px;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
}
</style>
