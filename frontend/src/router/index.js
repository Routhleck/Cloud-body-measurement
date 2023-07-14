import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView";
import LayOut from "@/layout/LayOut";
import PersonView from "@/views/PersonView";
import TestGradeView from "@/views/TestGradeView";
import TrainView from "@/views/TrainView";
import TrainRecordView from "@/views/TrainRecordView";
import SubmitView from "@/views/SubmitView";
import AuthenticationView from "@/views/AuthenticationView";
import TestView from "@/views/TestView";

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
  },
  {
    path: "/layout",
    name: "layout",
    component: LayOut,
    children: [
      {
        path: "train",
        name: "train",
        component: TrainView,
      },
      {
        path: "test",
        name: "test",
        component: TestView,
      },
      {
        path: "submit",
        name: "submit",
        component: SubmitView,
      },

      {
        path: "authen",
        name: "authen",
        component: AuthenticationView,
      },
      {
        path: "person",
        name: "person",
        component: PersonView,
      },

      {
        path: "record",
        name: "record",
        component: TrainRecordView,
      },
      {
        path: "grade",
        name: "grade",
        component: TestGradeView,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

// router.beforeEach((to, next) => {
//   const userJson = sessionStorage.getItem("user");
//   const user = JSON.parse(userJson);
//   const isVerified = user.isVerified;

//   if (to.name === "test") {
//     // 检查用户身份验证状态
//     if (!isVerified) {
//       // 身份验证未通过，跳转到身份验证页面
//       next({ name: "authen" });
//     } else {
//       next();
//     }
//   } else {
//     next();
//   }
// });

export default router;
