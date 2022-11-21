<template>
    <h2>Logowanie - /login</h2>
    <form @submit.prevent="onSubmit">
      <p>Your email is: {{form.email}}</p>
      <input type="email" v-model = "form.email" autocomplete="username" placeholder="Login"><br>
      <input type="password" v-model="form.password" autocomplete="current-password" placeholder="Hasło"><br>
      <button type="submit" @click='login()'>Zaloguj</button><br>
      Zapomniałeś hasła? <router-link to="/recovery">Przypomnij.</router-link><br>
      Nie masz konta? <router-link to="/register">Zarejestruj się.</router-link><br>
    </form>
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
        let prepareBody = JSON.stringify({
          email: this.form.email,
          password: this.form.password
        })

        let requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: prepareBody
        }

        fetch("/api/login", requestOptions)
        .then(response => response.json())
        .then( data => {
          console.log(data);
            localStorage.setItem("token", "Bearer "+data.data.token);
        })
      }
  
    }
  }
  </script>
  
  