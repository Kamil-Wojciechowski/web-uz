<template>
<div class="modal fade" id="shoppingCartModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5">Twój koszyk</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <table class="table">
  <thead>
    <tr>
      <th>#</th>
      <th >Nazwa produktu</th>
      <th>Ilość</th>
      <th>Akcje</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="item in cart" :key="item.productId">
      <td>{{ item.productId }}</td>
      <td>{{ item.name }}</td>
      <td>{{ item.amount }}</td>
      <td @click="deleteCartItem(item.productId)">Usuń</td>
    </tr>
  </tbody>
</table>
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Zamknij</button>
        <div v-if="buy">
          <router-link v-if="logged" class="btn btn-outline-info" to="/placeOrder" >Złóż zamówienie</router-link>
          <router-link v-else-if="!logged" class="btn btn-outline-info" to="/login" >Zaloguj się by złożyć zamówienie</router-link>
        </div>
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
      <font-awesome-icon class="navicon" title="Wyloguj"  @Click="logout" icon="fa-solid fa-right-from-bracket"/>
      <router-link to="/orderList"><font-awesome-icon class="navicon" title="Moje zamówienia" icon="fa-solid fa-user" /></router-link>
      <font-awesome-icon title="Koszyk" @click ="getCartItems" class="navicon" type="button" data-bs-toggle="modal" data-bs-target="#shoppingCartModal" icon="fa-solid fa-cart-shopping"/>
      <router-link to="/admin"><font-awesome-icon v-if="admin" class="navicon" title="Panel administracyjny" icon="fa-solid fa-gear" /></router-link>
    </div>
    <div v-else-if="!logged">

      <router-link to="/register"><font-awesome-icon  class="navicon" title="Zarejestruj się" icon="fa-solid fa-user-plus" /></router-link>
      <router-link to="/login"><font-awesome-icon class="navicon" title="Zaloguj" icon="fa-solid fa-right-to-bracket"/></router-link>
      <font-awesome-icon class="navicon" title="Koszyk" @click ="getCartItems" type="button" data-bs-toggle="modal" data-bs-target="#shoppingCartModal" icon="fa-solid fa-cart-shopping"/>
    </div>
  </div>
</nav>

  <div class="main"><router-view></router-view></div>
</template>

<script>
import Mixins from '@/mixins';

export default {
  name: 'App',
  mixins: [Mixins],
  

  async beforeMount() {
    const valid = await Mixins.methods.checkValidity(this.$route.name);
    this.admin = valid.isAdmin;

    this.logged = valid.isLogged
  },
  data(){
    return{
      logged: null,
      admin: null,
      buy: false,
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
        if(this.cart.length>0){
          this.buy= true;
        }
      }
      
    },
    deleteCartItem(id){
      let items = [];
      if(localStorage.getItem("items")){
        items = JSON.parse(localStorage.getItem("items"));
        items = (items.filter(product => product.productId != id)); 
      }
      localStorage.setItem('items', JSON.stringify(items));
      alert("Usunięto z koszyka");
      location.reload(true);
    }
  }
}
</script>

<style src="./assets/styles/app.css"></style>
