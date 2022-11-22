import { createApp } from 'vue';
import App from './App.vue';
import router from './router.js'
import VueAxios from 'vue-axios'
import http from './config/axios';

createApp(App).use(VueAxios, http).use(router).mount('#app')