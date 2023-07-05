<template>
  <div>
      <el-menu
          default-active="user"
          class="el-menu-vertical-demo"
          style="width: 200px;min-height: 80vh"
          router
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span style="padding: 20px;font-weight: bold">用户中心</span>
          </template>
          <el-menu-item index="/layout/user">
            <el-icon><User /></el-icon>体测成绩查询
          </el-menu-item>

          <el-menu-item index="/layout/preview">
            <el-icon><View /></el-icon>训练数据
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="2">
          <template #title>
            <el-icon><location /></el-icon>
            <span style="padding: 20px;font-weight: bold">体测中心</span>
          </template>
          <el-menu-item index="/layout/test">
            <el-icon><Test /></el-icon>引体向上
          </el-menu-item>

          <el-menu-item index="/layout/preview">
            <el-icon><View /></el-icon>仰卧起坐
          </el-menu-item>

          <el-menu-item index="/layout/preview">
            <el-icon><View /></el-icon>深蹲
          </el-menu-item>

          <el-menu-item index="/layout/preview">
            <el-icon><View /></el-icon>俯卧撑
          </el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="3">
          <template #title>
            <el-icon><location /></el-icon>
            <span style="padding: 20px;font-weight: bold">运动中心</span>
          </template>
          <el-menu-item index="/layout/user">
            <el-icon><User /></el-icon>跳远
          </el-menu-item>

          <el-menu-item index="/layout/preview">
            <el-icon><View /></el-icon>1000/800米
          </el-menu-item>

          <el-menu-item index="/layout/preview">
            <el-icon><View /></el-icon>坐位体前屈
          </el-menu-item>
        </el-sub-menu>


      
      </el-menu>
    </div>
</template>

<script>
export default {
  name: "AsideLine",
}
</script>

<style scoped>

</style>