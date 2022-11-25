<template>
  <br>
  <h1 class="text-center">STRONA GŁÓWNA</h1>
  <br>
  <ul class="nav justify-content-center">
  <li class="nav-item">
    <router-link to="/login" class="btn btn-outline-info">Logowanie</router-link>
  </li>
  <li class="nav-item">
    <router-link to="/register" class="btn btn-outline-info">Rejestracja</router-link>
  </li>
</ul>
  
  
</template>

<script>

export default {
    data(){
      return {
        msg: "donee"
      };
    }
}
</script>

