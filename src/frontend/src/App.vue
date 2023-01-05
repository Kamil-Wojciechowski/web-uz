<template>
<div class="modal fade" id="shoppingCartModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">Twój koszyk</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <table class="table">
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Nazwa produktu</th>
      <th scope="col">Ilość</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="item in cart" :key="item.productId">
      <td>{{ item.productId }}</td>
      <td>{{ item.name }}</td>
      <td>{{ item.amount }}</td>
    </tr>
  </tbody>
</table>
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Zamknij</button>
        <button type="button" class="btn btn-outline-info">Złóż zamówienie</button>
      </div>
    </div>
  </div>
</div>

  <nav class="navbar navbar-light bg-light">
  <a class="navbar-brand">
    <router-link to="/">
      <img src="./assets/images/logo.png" width="30" height="30" class="d-inline-block align-top" alt="">
    </router-link> 
    ŚWIAT ZWIERZĄT
  </a>
  <div>
    
    <div v-if="logged">
      <font-awesome-icon title="Wyloguj"  @Click="logout" icon="fa-solid fa-right-from-bracket"/>
      <font-awesome-icon title="Koszyk" @click ="getCartItems" type="button" data-bs-toggle="modal" data-bs-target="#shoppingCartModal" icon="fa-solid fa-cart-shopping"/>
    </div>
    <div v-else-if="!logged">
      <router-link to="/register"><font-awesome-icon title="Zarejestruj się" icon="fa-solid fa-user-plus" /></router-link>
      <router-link to="/login"><font-awesome-icon title="Zaloguj" icon="fa-solid fa-right-to-bracket"/></router-link>
      <font-awesome-icon title="Koszyk" @click ="getCartItems" type="button" data-bs-toggle="modal" data-bs-target="#shoppingCartModal" icon="fa-solid fa-cart-shopping"/>
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
  },
  data(){
    return{
      logged: null,
      cart: []
    }
  },
  methods: {
    logout() {
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      location.reload(true);
    },
    getCartItems(){
      
      if(localStorage.getItem("items")){
        this.cart = JSON.parse(localStorage.getItem("items"));
      }
      
    }
  }
}
</script>

<style src="./assets/styles/app.css"></style>
