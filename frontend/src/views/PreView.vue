<template>
<div style="width: 100%">
  <div style="display: flex;">
    <div>
      <div style="color: deepskyblue;font-size: 30px;text-align: center;padding: 30px 0;">
        缺陷预测
      </div>
      <div style="margin: 20px;width: 500px">
        <el-upload
          drag
          action="http://localhost:9090/files/upload"
          :on-success="filesUploadSuccess"
        >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text" style="color: deepskyblue">
            添加文件
        </div>
      </el-upload>
      </div>
      <div style="display: flex ;border-bottom: 1px solid lightskyblue;margin: 20px">
        <div style="text-align: center; padding: 10px; color: deepskyblue;width: 100px">
          训练数据
        </div>
        <div style="width: 400px;padding: 0 30px">
          <el-radio v-model="form.trainFile" label="JDT.csv" size="large">JDT</el-radio>
          <el-radio v-model="form.trainFile" label="Lucene.csv" size="large">Lucene</el-radio>
          <el-radio v-model="form.trainFile" label="PDE.csv" size="large">PDE</el-radio>
        </div>
      </div>
      <div style="display: flex ;border-bottom: 1px solid lightskyblue;margin: 20px">
        <div style="text-align: center; padding: 10px; color: deepskyblue;width: 100px">
          算法选择
        </div>
        <div style="width: 400px;padding: 0 30px">
        <el-radio v-model="form.alg" label="KNN" size="large">KNN</el-radio>
        <el-radio v-model="form.alg" label="逻辑回归" size="large">逻辑回归</el-radio>
        </div>
      </div>
      <div style="padding: 10px 200px">
        <el-button type="text" @click="preview" style="margin: 0 20px;font-size: large">
          开 始 预 测
        </el-button>
      </div>
    </div>
    <div style="margin: 0  25px">
      <div style="color: deepskyblue;font-size: 30px;text-align: center;padding: 30px 0;">
        结果显示
      </div>
      <el-table :data="tableData" border style="width: 100%;margin: 20px 0">
        <el-table-column prop="id" label="id" width="50" />
        <el-table-column prop="uploadTime" label="日期" width="180" value-format="yyyy-MM-dd HH:mm:ss" />
        <el-table-column prop="alg" label="算法" width="120"/>
        <el-table-column prop="trainFile" label="训练数据" width="140"/>
        <el-table-column label="结果" width="100" prop="result">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="predict(scope.row.result)"> 查看 </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="accuracy" label="准确率" width="150"/>
      </el-table>
    </div>
  </div>
</div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "PreView",
  data(){
    return{
      tableData:[],
      form:{}
    }
  },
  methods:{
    predict(result){
      window.location.href=result
    },
    preview(){
      this.form.uploadTime = new Date().getTime()
      request.post("/api/preview",this.form).then(res =>{
        console.log(res)
        this.load()
      })
    },
    filesUploadSuccess(res){
      console.log(res)
      this.form.file = res.data
    },
    load(){
      request.get("/api/preview").then(res =>{
        console.log(res)
        this.tableData=res.data
      })
    }
  },
  created() {
    this.load()
  }
}
</script>

<style scoped>

</style>