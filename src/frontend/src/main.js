import { createApp } from 'vue';
import App from './App.vue';
// import Vue from 'vue';
// import VueResource from "vue-resource";

createApp(App).mount('#app')
// Vue.use(VueResource);

// import LoginComponent from './components/Auth/LoginComponent.vue'
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  }
}