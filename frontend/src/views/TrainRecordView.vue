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
      tableData: [],
      filteredTableData: [],
      columns: [
        { prop: "time", label: "运动时间" },
        { prop: "name", label: "运动项目" },
        { prop: "practiceTime", label: "运动时长（秒）" },
        { prop: "count", label: "运动结果" },
      ],
      totalDuration: null,
    };
  },
  methods: {
    mapSportName(name) {
      const sportNames = {
        pullUp: "引体向上",
        sitUp: "仰卧起坐",
        squat: "深蹲",
        pushUp: "俯卧撑",
      };
      return sportNames[name] || name;
    },
    async fetchData() {
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id;
      try {
        const response = await axios.post(
          `http://cloudsports.top:9090/train/record?UserId=${userId}`
        );
        if (response.data.code === "200") {
          this.tableData = response.data.data.ItemList.map((item) => ({
            ...item,
            name: this.mapSportName(item.name),
          }));
          this.totalDuration = (response.data.data.totalTime / 60).toFixed(2);
          this.handleQuery();
        } else {
          console.error("请求失败:", response.data.message);
        }
      } catch (error) {
        console.error("请求失败:", error);
      }
    },
    handleQuery() {
      if (this.selectedSport === "") {
        this.filteredTableData = this.tableData;
      } else if (this.selectedSport) {
        const filteredData = this.tableData.filter(
          (item) => item.name === this.selectedSport
        );
        this.filteredTableData = filteredData;
      } else {
        this.filteredTableData = [];
      }
    },
  },
  mounted() {
    this.fetchData();

    this.selectedSport = ""; //默认选中全部项目
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
