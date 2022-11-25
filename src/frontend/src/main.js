import { createApp } from 'vue';
import App from './App.vue';
import router from './router.js'
import VueAxios from 'vue-axios'
import http from './config/axios';

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

createApp(App).use(VueAxios, http).use(router).mount('#app')