<template>
  <nav class="navbar navbar-light bg-light">
  <a class="navbar-brand" href="#">
    <img src="./assets/images/logo.png" width="30" height="30" class="d-inline-block align-top" alt="">
    ŚWIAT ZWIERZĄT
  </a>
  </nav>
  
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style src="./assets/styles/app.css"></style>
