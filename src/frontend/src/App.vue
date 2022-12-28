<template>
  <nav class="navbar navbar-light bg-light">
  <a class="navbar-brand">
    <router-link to="/">
      <img src="./assets/images/logo.png" width="30" height="30" class="d-inline-block align-top" alt="">
    </router-link> 
    ŚWIAT ZWIERZĄT
  </a>
  <div>
    <div v-if="logged">
      <font-awesome-icon  title="Wyloguj"  @Click="logout" icon="fa-solid fa-right-from-bracket"/>
    </div>
    <div v-else-if="!logged">
      <router-link to="/register"><font-awesome-icon title="Zarejestruj się" icon="fa-solid fa-user-plus" /></router-link>
      <router-link to="/login"><font-awesome-icon title="Zaloguj" icon="fa-solid fa-right-to-bracket"/></router-link>
    </div>
  </div>
</nav>
  
  <router-view></router-view>
</template>

<script>
import Mixins from '@/mixins';

export default {
  name: 'App',
  mixins: [Mixins],
  

  async beforeMount() {
    const valid = await Mixins.methods.checkValidity(this.$route.name);


    this.logged = valid.isLogged
    console.log(valid);
    console.log(this.logged);
  },
  data(){
    return{
      logged: null,
    }
  },
  methods: {
    logout() {
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      location.reload(true);
    }
  }
}
</script>

<style src="./assets/styles/app.css"></style>
