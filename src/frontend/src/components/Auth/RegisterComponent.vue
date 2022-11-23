<template>
    <vue-recaptcha ref='recaptcha' sitekey='6LfhziwjAAAAAGq2sJeHReEJXKL2LSsuBg1V7Yw4' @verify='onCaptchaVerified' @expired='onCaptchaExpired'></vue-recaptcha>
    <h2>Rejestracja</h2>
    <form @submit.prevent="onSubmit">
      <p>Your email is: {{form.email}}</p>
      <input type="text" v-model = "form.firstname" autocomplete="username" placeholder="Imię"><br>
      <input type="text" v-model = "form.lastname" autocomplete="username" placeholder="Nazwisko"><br>
      <input type="email" v-model = "form.email" autocomplete="username" placeholder="Email"><br>
      <input type="password" v-model="form.password" autocomplete="current-password" placeholder="Hasło"><br>
      <input type="password" v-model="form.repeatPassword" autocomplete="current-password" placeholder="Powtórz hasło"><br>
      <button type="submit" @click='register()'>Zarejestruj się</button><br>
      <router-link to="/login">Logowanie</router-link><br>
    </form>
  </template>
  
  <script>
  import { VueRecaptcha } from 'vue-recaptcha';

  export default {
    components: {
      VueRecaptcha 
    },
    data(){
      return {
        form:{
          firstname: "",
          lastname: "",
          email: "",
          password: "",
          repeatPassword: "",
          recaptchaToken: ""
        }
      };
    },
    methods: {
      onEvent() {
        this.$refs.recaptcha.execute();
      },
      register(){
        let prepareBody = JSON.stringify({
          firstname: this.form.firstname,
          lastname: this.form.lastname,
          email: this.form.email,
          password: this.form.password,
          confirmedPassword: this.form.repeatPassword,
          recaptchaToken: this.form.recaptchaToken
        });

        this.axios.post("/register", prepareBody)
        .then( data => {
          console.log(data);
        })
        .catch(error => {
          console.log({error});
        });
      },
      onCaptchaVerified: function (recaptchaToken) {
        this.form.recaptchaToken = recaptchaToken;
      },
      onCaptchaExpired: function () {
        this.$refs.recaptcha.reset();
      }
    }
  }
  </script>
  
  