<template>
  <div class="test-table">
    <!-- 表格展示 -->
    <el-table :data="filteredTestItems" class="test-items-table">
      <el-table-column prop="testTime" label="体测时间" width="160"></el-table-column>
      <el-table-column prop="height" label="身高(cm)" width="100"></el-table-column>
      <el-table-column prop="weight" label="体重(kg)" width="100"></el-table-column>
      <el-table-column prop="vitalCapacity" label="肺活量(ml)" width="120"></el-table-column>
      <el-table-column prop="standingLongJump" label="立定跳远(cm)" width="140"></el-table-column>
      <el-table-column prop="sitAndReach" label="坐位体前屈(cm)" width="140"></el-table-column>
      <el-table-column prop="pullOrSitUp" label="引体向上/仰卧起坐(time)" width="180"></el-table-column>
      <el-table-column prop="sprint50m" label="50米跑(second)" width="140"></el-table-column>
      <el-table-column prop="longDistanceRun" label="800或1000米(second)" width="160"></el-table-column>
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
            item.userId.toString().includes(this.searchText) ||
            item.testTime.includes(this.searchText) ||
            item.height.toString().includes(this.searchText) ||
            item.weight.toString().includes(this.searchText) ||
            item.vitalCapacity.toString().includes(this.searchText) ||
            item.standingLongJump.toString().includes(this.searchText) ||
            item.sitAndReach.toString().includes(this.searchText) ||
            item.pullOrSitUp.toString().includes(this.searchText) ||
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
      const userJson = sessionStorage.getItem("user");
      const user = JSON.parse(userJson);
      const userId = user.user_id;

      axios
        .get("http://127.0.0.1:9090/test", {
          params: {
            UserId: userId,
          },
        })
        .then((response) => {
          this.testItems = [response.data.data];
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
