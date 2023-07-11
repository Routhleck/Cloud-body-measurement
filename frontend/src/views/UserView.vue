<template>
  <div style="padding:10px;width: 100%">
    <div style="margin: 10px">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%;" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="nickName" label="昵称"/>
      <el-table-column prop="age" label="年龄"/>
      <el-table-column prop="sex" label="性别"/>
      <el-table-column prop="address" label="地址"/>
      <el-table-column fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)"
          >编辑</el-button
          >
          <el-popconfirm title="确认删除吗?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button link type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5,10,20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />

      <el-dialog v-model="dialogVisible" title="Tips" width="30%">
        <el-form label-width="120px" :model="form" >
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 70%"/>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickName" style="width: 70%"/>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 70%"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.sex" label="男" size="large">男</el-radio>
            <el-radio v-model="form.sex" label="女" size="large">女</el-radio>
            <el-radio v-model="form.sex" label="未知" size="large">未知</el-radio>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" style="width: 70%"/>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src

import request from "@/utils/request";

export default {
  name: 'UserView',
  components: {

  },
  data(){
    return{
      form:{

      },
      dialogVisible:false,
      search :'',
      currentPage:1,
      pageSize:10,
      total:0,
      tableData :[

      ]
    }
  },
  methods:{
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible=true
    },
    handleSizeChange(){
      this.load()
    },
    handleCurrentChange(){
      this.load()
    },
    // add(){
    //   this.dialogVisible=true
    //   this.form={}
    // },
    save(){
      if (this.form.id){  //更新
        request.put("/api/user",this.form).then(res=>{
          // console.log(res)
          if (res.code==='0'){
            this.$message({
              type:"success",
              message:"更新成功",
            })
            this.load()
          }else{
            this.$message({
              type:"error",
              message:res.msg,
            })
          }
        })
      }
      this.dialogVisible=false
      this.form={}
    },
    load(){
      request.get("/api/user",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          search:this.search
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },

    handleDelete(id){
      request.delete("/api/user/"+id).then(res=>{
        if (res.code==='0'){
          this.$message({
            type:"success",
            message:"删除成功",
          })
          this.load()
        }else{
          this.$message({
            type:"error",
            message:res.msg,
          })
        }
      })
    }
  },
  created() {
    this.load()
  }
}
</script>
