import { createWebHistory, createRouter } from "vue-router";
import HomeComponent from "./components/HomeComponent.vue";
import LoginComponent from "./components/Auth/LoginComponent.vue";
import RegisterComponent from "./components/Auth/RegisterComponent.vue";
import RecoveryComponent from "./components/Auth/RecoveryComponent.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: HomeComponent,
  },
  {
    path: "/login",
    name: "Login",
    component: LoginComponent,
  },
  {
    path: "/register",
    name: "Register",
    component: RegisterComponent,
  },
  {
    path: "/recovery",
    name: "Recovery",
    component: RecoveryComponent,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;