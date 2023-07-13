<template>
  <div class="element-main">
    <div class="select-container">
      <span class="select-label">请选择年份：</span>
      <el-select v-model="selectedYear" placeholder="请选择年份" class="select">
        <el-option
          v-for="year in years"
          :key="year"
          :label="year"
          :value="year"
        />
      </el-select>
      <el-button type="primary" @click="handleQuery" class="query-button">
        查询
      </el-button>
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
      selectedYear: null,
      years: null, // 年份下拉框选项
      tableData: [
        {
          test_time: 2022,
          height: 170,
          weight: 65,
          vital_capacity: 4000,
          standing_long_jump: 200,
          sit_and_reach: 30,
          pull_or_sitUp: 15,
          "50m": 7,
          "800_or_1000m": 180,
        },
        {
          test_time: 2023,
          height: 175,
          weight: 68,
          vital_capacity: 4200,
          standing_long_jump: 205,
          sit_and_reach: 32,
          pull_or_sitUp: 18,
          "50m": 6.5,
          "800_or_1000m": 170,
        },
      ],
      filteredTableData: [],
      columns: [
        // 表格列配置
        { prop: "test_time", label: "年份" },
        { prop: "height", label: "身高(cm)" },
        { prop: "weight", label: "体重(kg)" },
        { prop: "vital_capacity", label: "肺活量(ml)" },
        { prop: "standing_long_jump", label: "立定跳远(cm)" },
        { prop: "sit_and_reach", label: "坐位体前屈(cm)" },
        { prop: "pull_or_sitUp", label: "引体向上/仰卧起坐(time)" },
        { prop: "50m", label: "50米跑(second)" },
        { prop: "800_or_1000m", label: "800或1000米(second)" },
      ],
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
        const response = await axios.get("http://127.0.0.1:9090/test", data);
        this.tableData = response.data;
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    },
    handleQuery() {
      if (this.selectedYear) {
        const filteredData = this.tableData.filter(
          (item) => item.test_time === this.selectedYear
        );
        this.filteredTableData = filteredData;
      } else {
        this.filteredTableData = [];
      }
    },
  },
  mounted() {
    const currentYear = new Date().getFullYear();
    this.years = [
      currentYear - 4,
      currentYear - 3,
      currentYear - 2,
      currentYear - 1,
      currentYear,
      currentYear + 1,
    ];
    this.selectedYear = currentYear; // 默认选择当前年份
    this.fetchData(); // 初始化时获取数据
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
</style>
