import { createWebHistory, createRouter } from "vue-router";
import HomeComponent from "./components/HomeComponent.vue";
import LoginComponent from "./components/Auth/LoginComponent.vue";

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
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;