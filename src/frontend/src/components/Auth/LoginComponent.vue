<template>
    <br>
  <h1 class="text-center">LOGOWANIE</h1>
  <br>
  <div class="card container">
    <div class="card-body">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <input type="email" class="form-control" v-model = "form.email" autocomplete="username" placeholder="Login"><br>
        </div>
        <div class="form-group">
          <input type="password" class="form-control" v-model="form.password" autocomplete="current-password" placeholder="Hasło"><br>
        </div>
        <div class="form-group">
          <button class="btn btn-outline-info" type="submit" @click='login()'>Zaloguj</button><br>
        </div>
        <div class="form-group">
          Zapomniałeś hasła? 
          <router-link to="/recovery">Przypomnij.</router-link>
        </div>
        <div class="form-group">
          Nie masz konta? 
          <router-link to="/register">Zarejestruj się.</router-link>
        </div>
      </form>
    </div>
  </div>
    
  </template>
  
  <script>
  export default {
    data(){
      return {
        form:{
          email: "",
          password: "",
        }
      };
    },
    methods: {
      login(){
        let prepareBody = {
          username: this.form.email,
          password: this.form.password
        };

        let formBody = [];
        for (var property in prepareBody) {
          var encodedKey = encodeURIComponent(property);
          var encodedValue = encodeURIComponent(prepareBody[property]);
          formBody.push(encodedKey + "=" + encodedValue);
        }
        formBody = formBody.join("&");

        console.log(formBody);

        this.$http.post("/login", formBody, {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          }
        })
        .then(data => {
          console.log(data);
            // localStorage.setItem("token", "Bearer "+data.data.token);
        }).catch(error => {
          console.log({error});
        });
      }
  
    }
  }
  </script>
  
  