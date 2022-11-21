<template>
    <h2>Rejestracja</h2>
    <form @submit.prevent="onSubmit">
      <p>Your email is: {{form.email}}</p>
      <input type="text" v-model = "form.firstname" autocomplete="username" placeholder="Imię"><br>
      <input type="text" v-model = "form.lastname" autocomplete="username" placeholder="Nazwisko"><br>
      <input type="email" v-model = "form.email" autocomplete="username" placeholder="Email"><br>
      <input type="password" v-model="form.password" autocomplete="current-password" placeholder="Hasło"><br>
      <input type="password" v-model="form.repeatPassword" autocomplete="current-password" placeholder="Powtórz hasło"><br>
      <button type="submit" @click='register()'>Zrejestruj się</button><br>
      <router-link to="/login">Logowanie</router-link><br>
    </form>
  </template>
  
  <script>
  export default {
    data(){
      return {
        form:{
          firstname: "",
          lastname: "",
          email: "",
          password: "",
          repeatPassword: ""
        }
      };
    },
    methods: {
      register(){
        let prepareBody = JSON.stringify({
          firstname: this.form.firstname,
          lastname: this.form.lastname,
          email: this.form.email,
          password: this.form.password,
          confirmedPassword: this.form.repeatPassword
        })

        let requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: prepareBody
        }

        fetch("/api/register", requestOptions)
        .then(response => response.json())
        .then( data => {
          console.log(data);
        })
        .catch(error => {
          console.log(error);
        });
      }
  
    }
  }
  </script>
  
  