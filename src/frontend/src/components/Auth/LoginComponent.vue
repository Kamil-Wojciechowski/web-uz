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
  
  