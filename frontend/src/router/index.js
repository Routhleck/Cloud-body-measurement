import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView";
import LayOut from "@/layout/LayOut";
import PersonView from "@/views/PersonView";
import TestGradeView from "@/views/TestGradeView";
import TrainView from "@/views/TrainView";
import TrainRecordView from "@/views/TrainRecordView";
import SubmitView from "@/views/SubmitView";
import AuthenticationView from "@/views/AuthenticationView";
import PushstreamView from "@/views/PushstreamView";
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
        path: "push",
        name: "push",
        component: PushstreamView,
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

export default router;
