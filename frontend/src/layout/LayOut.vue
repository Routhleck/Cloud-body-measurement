<template>
  <div>
    <!--    头部-->
    <HeadTest />
    <!--    主题-->
    <div style="display: flex">
      <!--      侧边栏-->
      <AsideLine />
      <!--      内容区域-->
      <router-view/>
    </div>
  </div>
</template>

<script>
import HeadTest from "@/components/HeadTest";
import AsideLine from "@/components/AsideLine";

export default {
  name: "LayOut",
  components: {
    HeadTest,
    AsideLine
  }
}
</script>

<style scoped>

</style>