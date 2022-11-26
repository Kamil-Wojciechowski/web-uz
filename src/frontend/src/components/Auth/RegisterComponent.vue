<template>

  <br>
  <h3 class="text-center">REJESTRACJA</h3>
  <br>
  <div class="card content">
    <div class="card-body">
      <form @submit.prevent="onSubmit()">
        <div class="form-group">
          <input type="text" class="form-control" v-model="form.firstname" placeholder="Imię" required
            minlength="3"><br>
        </div>
        <div class="form-group">
          <input type="text" class="form-control" v-model="form.lastname" placeholder="Nazwisko" required
            minlength="3"><br>
        </div>
        <div class="form-group">
          <input type="email" class="form-control" v-model="form.email" placeholder="Email" required minlength="8"><br>
        </div>
        <div class="form-group">
          <input type="password" class="form-control" v-model="form.password" placeholder="Hasło" required
            minlength="8"><br>
        </div>
        <div class="form-group">
          <input type="password" class="form-control" v-model="form.repeatPassword" placeholder="Powtórz hasło" required
            minlength="8"><br>
        </div>
        <div class="form-group">
          <vue-recaptcha ref='recaptcha' sitekey='6LfhziwjAAAAAGq2sJeHReEJXKL2LSsuBg1V7Yw4' @verify='onCaptchaVerified'
            @expired='onCaptchaExpired'></vue-recaptcha><br>
        </div>
        <div class="form-group">
          <button class="btn btn-outline-info" type="submit">Zarejestruj się</button><br>
        </div>
        <div class="form-group">
          <br>Masz już konto? <router-link to="/login">Zaloguj się.</router-link><br>
        </div>
        <div class="text-danger">{{ form.error }}</div>
        <div class="text-info">{{ form.message }}</div>
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
  data() {
    return {
      form: {
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        repeatPassword: "",
        recaptchaToken: "",
        error: "",
        message: ""
      }
    };
  },
  methods: {
    onSubmit() {
      this.register();
    },
    onEvent() {
      this.$refs.recaptcha.execute();
    },
    register() {
      let prepareBody = JSON.stringify({
        firstname: this.form.firstname,
        lastname: this.form.lastname,
        email: this.form.email,
        password: this.form.password,
        confirmedPassword: this.form.repeatPassword,
        recaptchaToken: this.form.recaptchaToken
      });

      this.axios.post("/register", prepareBody)
        .then(data => {
          console.log(data);
          this.form.message = data.data.message;
        })
        .catch(error => {
          console.log(error);
          this.form.error = error.response.data.message;
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
  
  