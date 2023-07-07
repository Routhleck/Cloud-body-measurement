<template>
  <div class="test-table">
    <!-- 表格展示 -->
    <el-table :data="filteredTestItems" class="test-items-table">
      <!-- <el-table-column prop="user_id" label="用户ID" width="100"></el-table-column> -->
      <el-table-column
        prop="test_time"
        label="体测时间"
        width="160"
      ></el-table-column>
      <el-table-column
        prop="height"
        label="身高(cm)"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="weight"
        label="体重(kg)"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="vital_capacity"
        label="肺活量(ml)"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="standing_long_jump"
        label="立定跳远(cm)"
        width="140"
      ></el-table-column>
      <el-table-column
        prop="sit_and_reach"
        label="坐位体前屈(cm)"
        width="140"
      ></el-table-column>
      <el-table-column
        prop="pull_or_sit_up"
        label="引体向上/仰卧起坐(time)"
        width="180"
      ></el-table-column>
      <el-table-column
        prop="50m"
        label="50米跑(second)"
        width="140"
      ></el-table-column>
      <el-table-column
        prop="800_or_1000m"
        label="800或1000米(second)"
        width="160"
      ></el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-show="shouldShowPagination"
      v-model="currentPage"
      :page-count="pageCount"
      :page-size="pageSize"
      layout="total, prev, pager, next,jumper"
      :total="totalItems"
      @current-change="handlePageChange"
      class="pagination"
    ></el-pagination>
  </div>
</template>

<script>
import axios from "axios";
import { ElTable, ElPagination } from "element-plus";

export default {
  components: {
    ElTable,
    ElPagination,
  },
  data() {
    return {
      testItems: [],
      currentPage: 1,
      pageSize: 10,
      pageCount: 3,
    };
  },
  computed: {
    filteredTestItems() {
      if (this.searchText) {
        return this.testItems.filter((item) => {
          return (
            item.user_id.toString().includes(this.searchText) ||
            item.test_time.includes(this.searchText) ||
            item.height.toString().includes(this.searchText) ||
            item.weight.toString().includes(this.searchText) ||
            item.vital_capacity.toString().includes(this.searchText) ||
            item.standing_long_jump.toString().includes(this.searchText) ||
            item.sit_and_reach.toString().includes(this.searchText) ||
            item.pull_or_sit_up.toString().includes(this.searchText) ||
            item["50m"].toString().includes(this.searchText) ||
            item["800_or_1000m"].toString().includes(this.searchText)
          );
        });
      } else {
        return this.testItems;
      }
    },
    totalItems() {
      return this.testItems.length;
    },
    totalPages() {
      return Math.ceil(this.totalItems / this.pageSize);
    },
    currentPageItems() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.testItems.slice(startIndex, endIndex);
    },
    shouldShowPagination() {
      return true;
    },
  },
  mounted() {
    this.fetchTestItems();
  },
  methods: {
    fetchTestItems() {
      axios
        .get("/api/test-items") // 替换为您的后端接口地址
        .then((response) => {
          this.testItems = response.data;
        })
        .catch((error) => {
          console.error("Failed to fetch test items:", error);
        });
    },
    handlePageChange(currentPage) {
      this.currentPage = currentPage;
    },
  },
};
</script>

<style scoped>
.test-table {
  max-width: 100%;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
}

.test-items-table {
  width: 100%;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
