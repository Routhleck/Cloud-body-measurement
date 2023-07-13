<!-- 侧边栏 -->
<template>
  <div>
    <el-menu
      default-active="user"
      class="el-menu-vertical-demo"
      style="width: 200px; min-height: 80vh"
      router
    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon><location /></el-icon>
          <span style="padding: 20px; font-weight: bold">用户中心</span>
        </template>
        <el-menu-item index="/layout/grade">
          <el-icon><User /></el-icon>体测成绩查询
        </el-menu-item>

        <el-menu-item index="/layout/record">
          <el-icon><User /></el-icon>训练数据
        </el-menu-item>

        <el-menu-item index="/layout/submit">
          <el-icon><User /></el-icon>上传成绩
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="2">
        <template #title>
          <el-icon><location /></el-icon>
          <span style="padding: 20px; font-weight: bold">体测中心</span>
        </template>

        <el-menu-item index="/layout/authen">
          <el-icon><View /></el-icon>身份验证
        </el-menu-item>

        <el-menu-item index="/layout/test">
          <el-icon><View /></el-icon>体测
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="3">
        <template #title>
          <el-icon><location /></el-icon>
          <span style="padding: 20px; font-weight: bold">运动中心</span>
        </template>
        <el-menu-item index="/layout/train">
          <el-icon><View /></el-icon>训练
        </el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "AsideLine",
};
</script>

<style scoped></style>
