<template>

<br>
  <h1 class="text-center">REJESTRACJA</h1>
  <br>
  <div class="card container">
    <div class="card-body">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <input type="text" class="form-control" v-model = "form.firstname" autocomplete="username" placeholder="Imię"><br>
        </div>
        <div class="form-group">
          <input type="text" class="form-control" v-model = "form.lastname" autocomplete="username" placeholder="Nazwisko"><br>
        </div>
        <div class="form-group">
          <input type="email" class="form-control" v-model = "form.email" autocomplete="username" placeholder="Email"><br>
        </div>
        <div class="form-group">
          <input type="password" class="form-control" v-model="form.password" autocomplete="current-password" placeholder="Hasło"><br>
        </div>
        <div class="form-group">
          <input type="password" class="form-control" v-model="form.repeatPassword" autocomplete="current-password" placeholder="Powtórz hasło"><br>
        </div>
        <div class="form-group">
          <vue-recaptcha ref='recaptcha' sitekey='6LfhziwjAAAAAGq2sJeHReEJXKL2LSsuBg1V7Yw4' @verify='onCaptchaVerified' @expired='onCaptchaExpired'></vue-recaptcha><br>
        </div>
        <div class="form-group">
          <button class="btn btn-outline-info" type="submit" @click='register()'>Zarejestruj się</button><br>
        </div>
        <div class="form-group">
          Masz już konto? <router-link to="/login">Zaloguj się.</router-link><br>
        </div>
        
      </form>
    </div>
  </div>

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
  
  