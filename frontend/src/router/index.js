import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView";
import LayOut from "@/layout/LayOut";
import PersonView from "@/views/PersonView";
import TestView from "@/views/TestView";
import GradeView from "@/views/GradeView";
import TrainView from "@/views/TrainView";
import PreviewView from "@/views/PreviewView";
import SubmitView from "@/views/SubmitView";
import AuthenticationView from "@/views/AuthenticationView";
import PushstreamView from "@/views/PushstreamView";
import OptionView from "@/views/OptionView";

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
        path: "option",
        name: "option",
        component: OptionView,
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
        path: "grade",
        name: "grade",
        component: GradeView,
      },
      {
        path: "preview",
        name: "preview",
        component: PreviewView,
      },
      {
        path: "test",
        name: "test",
        component: TestView,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
