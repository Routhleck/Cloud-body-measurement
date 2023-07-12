<template>
  <div class="element-main">
    <el-table style="width: 100%" :data="tableData">
      <el-table-column prop="date" label="日期" width="180"> </el-table-column>
      <el-table-column prop="name" label="姓名" width="180"> </el-table-column>
      <el-table-column prop="address" label="地址"> </el-table-column>
    </el-table>

    <el-table style="width: 100%" :data="getValues()" :show-header="false">
      <el-table-column
        v-for="(item, index) in getHeaders()"
        :key="index"
        :prop="item"
      >
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      headers: [
        {
          prop: "date",
          label: "日期",
        },
        {
          prop: "name",
          label: "姓名",
        },
        {
          prop: "address",
          label: "地址",
        },
      ],
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1517 弄",
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1519 弄",
        },
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1516 弄",
        },
      ],
    };
  },
  computed: {
    getHeaders() {
      //["title", "value0", "value1", "value2", "value3"]
      return this.tableData.reduce(
        (pre, cur, index) => pre.concat(`value${index}`),
        ["title"]
      );
    },
    getValues() {
      //[ {title: "日期", value0: "2016-05-02", value1: "2016-05-04", value2: "2016-05-01", value3: "2016-05-03"} ]
      return this.headers.map((item) => {
        return this.tableData.reduce(
          (pre, cur, index) =>
            Object.assign(pre, { ["value" + index]: cur[item.prop] }),
          { title: item.label }
        );
      });
    },
  },
};
</script>

<style scoped>
.element-main {
  max-width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
}
</style>
