<template>
  <div style="height: 50px; line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px;padding-left: 60px">
      <img src="../assets/云体测.png" alt="Logo" style="height: 100%;">
    </div>
    <div style="flex: 1"></div>
    <div style="width: 150px">
      <el-icon><UserFilled /></el-icon>
      <el-dropdown style="padding: 15px 0;margin: 0 10px">
        <span class="el-dropdown-link">
            {{ user.nickName }}
          <el-icon class="el-icon--right"> <arrow-down /> </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="this.$router.push('/layout/person')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="this.$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { ArrowDown } from '@element-plus/icons-vue'
export default {
  name: "HeadTest",
  data(){
    return{
      user:{}
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
  },
  components:{
    ArrowDown
  }
}
</script>

<style scoped>

</style>
