<template>
  <body class="bg">
  <div id="formContainer" class="dwo">
    <div class="formLeft">
      <img src="../static/images/avatar.jpg">
    </div>
    <div class="formRight">
      <!-- Forgot password form -->
      <form id="forgot" class="otherForm">
        <header>
          <h1>忘记密码</h1>
          <p>找回密码</p>
        </header>
        <section>
          <label>
            <p>用户名</p>
            <input type="text" v-model="form.username">
            <div class="border"></div>
          </label>
          <label>
            <p>修改密码</p>
            <input type="password" v-model="form.password">
            <div class="border"></div>
          </label>
          <button type="button" @click="revise">确认修改</button>
        </section>
        <footer>
          <button type="button" class="forgotBtn">返回</button>
        </footer>
      </form>

      <!-- Login form -->
      <form id="login">
        <header>
          <h1>欢迎回来</h1>
          <p>请先登录</p>
        </header>
        <section>
          <label>
            <p>用户名</p>
            <input type="text" v-model="form.username">
            <div class="border"></div>
          </label>
          <label>
            <p>密码</p>
            <input type="password" v-model="form.password">
            <div class="border"></div>
          </label>
          <button type="button" @click="Login">登 录</button>
        </section>
        <footer>
          <button type="button" class="forgotBtn" >忘记密码？</button>
          <button type="button" class="registerBtn" >新用户？</button>
        </footer>
      </form>

      <!-- Register form -->
      <form id="register" class="otherForm">
        <header>
          <h1>用户注册</h1>
          <p>注册后享受更多服务</p>
        </header>
        <section>
          <label>
            <p>用户名</p>
            <input type="text" v-model="form.username">
            <div class="border"></div>
          </label>
          <label>
            <p>密码</p>
            <input type="password" v-model="form.password">
            <div class="border"></div>
          </label>
          <label>
            <p>重复密码</p>
            <input type="password" v-model="form.confirm">
            <div class="border"></div>
          </label>
          <button type="button" @click="Register">注 册</button>
        </section>
        <footer>
          <button type="button" class="registerBtn">返回</button>
        </footer>
      </form>
    </div>
  </div>
  </body>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "LoginView",
  data() {
    return {
      form:{},
      LOGIN:true,
      REGISTER:false,
      FORGET:false
    }
  },
  methods:{
    Login(){
      request.post("/api/user/login",this.form).then(res=>{
        if (res.code==='0'){
          this.$message({
            type:"success",
            message:"登录成功",
          })
          sessionStorage.setItem("user", JSON.stringify(res.data))
          this.$router.push("/layout")
        }else {
          this.$message({
            type:"error",
            message:res.msg,
          })
        }
      })
    },
    Register(){
      if (this.form.password!==this.form.confirm){
        this.$message({
          type:"error",
          message:"两次密码不一致"
        })
      }else {
        request.post("/api/user/register",this.form).then(res=>{
          if (res.code==='0'){
            this.$message({
              type:"success",
              message:"注册成功",
            })
            this.FORGET=false
            this.REGISTER=false
            this.LOGIN=true
          }else{
            this.$message({
              type:"error",
              message:res.msg,
            })
          }
        })
      }
      this.form={}
    },
    revise(){
      request.put("/api/user/revise",this.form).then(res=>{
        // console.log(res)
        if (res.code==='0'){
          this.$message({
            type:"success",
            message:"修改成功",
          })
          this.FORGET=false
          this.REGISTER=false
          this.LOGIN=true
        }else{
          this.$message({
            type:"error",
            message:res.msg,
          })
        }
        this.form={}
      })
    },
  }

}
</script>

<style src="../static/css/normalize.min.css" scoped/>
<style src="../static/css/style.css" scoped/>
