<template>
  <nav class="navbar navbar-light bg-light">
  <a class="navbar-brand">
    <router-link to="/">
      <img src="./assets/images/logo.png" width="30" height="30" class="d-inline-block align-top" alt="">
    </router-link> 
    ŚWIAT ZWIERZĄT
  </a>
  <div class="btn-group" role="group" aria-label="Basic example">
    <router-link class="btn btn-outline-info" to="/login">Logowanie</router-link>
    <router-link class="btn btn-outline-info" to="/register">Rejestracja</router-link>
  </div>
  <!-- <div class="form-group">
    <router-link class="btn btn-outline-info" to="/">Wyloguj</router-link>
  </div> -->
</nav>
  
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style src="./assets/styles/app.css"></style>
