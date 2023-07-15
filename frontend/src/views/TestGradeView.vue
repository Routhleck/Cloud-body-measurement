<!-- eslint-disable vue/no-deprecated-filter -->
<template>
  <div class="element-main">
    <div class="select-container">
      <span class="select-label">请选择年份：</span>
      <el-select v-model="selectedYear" placeholder="请选择年份" class="select">
        <el-option value="" label="全部年份" />
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
          <span v-if="column.prop !== 'test_time'">{{ row[column.prop] }}</span>
          <span v-else>{{ row[column.prop] | formatDate }}</span>
        </template>
      </el-table-column>
    </el-table>
    <div v-else class="no-data">暂无数据</div>
  </div>
</template>

<script>
// eslint-disable-next-line
import axios from "axios";

export default {
  filters: {
    formatDate(value) {
      return value.toString(); // Modify this to format the date as per your requirement
    },
  },
  data() {
    return {
      selectedYear: null,
      years: null, // 年份下拉框选项
      tableData: [],
      filteredTableData: [],
      columns: [
        // 表格列配置
        { prop: "test_time", label: "年份" },
        { prop: "height", label: "身高(cm)" },
        { prop: "weight", label: "体重(kg)" },
        { prop: "vital_capacity", label: "肺活量(ml)" },
        { prop: "standing_long_jump", label: "立定跳远(cm)" },
        { prop: "sit_and_reach", label: "坐位体前屈(cm)" },
        { prop: "pull_up", label: "引体向上(time)" },
        { prop: "sprint_50m", label: "50米跑(second)" },
        { prop: "long_distance_run", label: "800或1000米(second)" },
        { prop: "push_up", label: "俯卧撑(time)" },
        { prop: "sit_up", label: "仰卧起坐(time)" },
        { prop: "squat", label: "深蹲(time)" },
      ],
    };
  },
  methods: {
    async fetchData() {
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id; // 更新为正确的属性名
      try {
        const response = await axios.get(
          `http://cloudsports.top:9090/test?UserId=${userId}`
        );
        this.tableData = response.data.data;
        this.handleQuery();
      } catch (error) {
        console.error("获取数据失败：", error);
      }
    },
    handleQuery() {
      if (this.selectedYear === "") {
        this.filteredTableData = this.tableData;
      } else if (this.selectedYear) {
        const filteredData = this.tableData.filter(
          (item) => item.test_time === this.selectedYear.toString()
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
</style>
