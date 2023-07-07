<template>
  <body class="bg">
    <div id="formContainer" class="dwo">
      <div class="formLeft">
        <img src="../static/images/avatar.jpg" />
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
              <input type="text" v-model="form.username" />
              <div class="border"></div>
            </label>
            <label>
              <p>修改密码</p>
              <input type="password" v-model="form.password" />
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
              <input type="text" v-model="form.username" />
              <div class="border"></div>
            </label>
            <label>
              <p>密码</p>
              <input type="password" v-model="form.password" />
              <div class="border"></div>
            </label>
<<<<<<< Updated upstream
            <button type="button" @click="Login">登 录</button>
=======
            <button type="button" @click="login">登 录</button>
>>>>>>> Stashed changes
          </section>
          <footer>
            <button type="button" class="forgotBtn">忘记密码？</button>
            <button type="button" class="registerBtn">新用户？</button>
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
              <input type="text" v-model="form.username" />
              <div class="border"></div>
            </label>
            <label>
              <p>密码</p>
              <input type="password" v-model="form.password" />
              <div class="border"></div>
            </label>
            <label>
              <p>重复密码</p>
              <input type="password" v-model="form.confirm" />
              <div class="border"></div>
            </label>
<<<<<<< Updated upstream
            <button type="button" @click="Register">注 册</button>
=======
            <button type="button" @click="register">注 册</button>
>>>>>>> Stashed changes
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
      form: {},
      LOGIN: true,
      REGISTER: false,
      FORGET: false,
    };
  },
  methods: {
<<<<<<< Updated upstream
    Login() {
      const requestData = {
        name: this.form.name,
        password: this.form.password,
      };

      request
        .post("/api/user/login", requestData)
        .then((res) => {
          if (res === "false") {
            this.$message({
              type: "error",
              message: "用户名或密码错误",
            });
          } else {
            this.$message({
              type: "success",
              message: "登录成功",
            });
            sessionStorage.setItem("user", JSON.stringify({ user_id: res }));
            this.$router.push("/layout");
          }
        })
        .catch((error) => {
          console.error("Failed to login:", error);
        });
    },

    Register() {
=======
    login() {
    const requestData = {
      name: this.form.username,
      password: this.form.password,
    };

    request
      .post("http://127.0.0.1:9090/user/login", requestData)
      .then((res) => {
        if (res.code === "200") {
          this.$message({
            type: "success",
            message: "登录成功",
          });
          sessionStorage.setItem("user", JSON.stringify({ user_id: res.user_id }));

          // 根据需要进行页面跳转
          // if (res.user_id === "admin") {
          //   this.$router.push("/admin");
          // } else {
          //   this.$router.push("/user");
          // }
          this.$router.push("/layout");
        } else {
          this.$message({
            type: "error",
            message: res.message,
          });
        }
      })
      .catch((error) => {
        console.error("Failed to login:", error);
      });
  },

    register() {
>>>>>>> Stashed changes
      if (this.form.password !== this.form.confirm) {
        this.$message({
          type: "error",
          message: "两次密码不一致",
        });
      } else {
        const requestData = {
<<<<<<< Updated upstream
          name: this.form.name,
=======
          name: this.form.username,
>>>>>>> Stashed changes
          password: this.form.password,
        };

        request
<<<<<<< Updated upstream
          .post("/api/user/register", requestData)
          .then((res) => {
            if (res) {
=======
          .post("http://127.0.0.1:9090/user/register", requestData)
          .then((res) => {
            if (res.code === "200") {
>>>>>>> Stashed changes
              this.$message({
                type: "success",
                message: "注册成功",
              });
              this.FORGET = false;
              this.REGISTER = false;
              this.LOGIN = true;
            } else {
              this.$message({
                type: "error",
                message: "用户已存在",
              });
            }
          })
          .catch((error) => {
            console.error("Failed to register:", error);
          });
      }
      this.form = {};
    },
    revise() {
      request.put("/api/user/revise", this.form).then((res) => {
<<<<<<< Updated upstream
        // console.log(res)
=======
>>>>>>> Stashed changes
        if (res.code === "0") {
          this.$message({
            type: "success",
            message: "修改成功",
          });
          this.FORGET = false;
          this.REGISTER = false;
          this.LOGIN = true;
        } else {
          this.$message({
            type: "error",
            message: res.msg,
          });
        }
        this.form = {};
      });
    },
  },
};
</script>

<style src="../static/css/normalize.min.css" scoped />
<style src="../static/css/style.css" scoped />
