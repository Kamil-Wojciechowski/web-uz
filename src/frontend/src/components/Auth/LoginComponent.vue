<template>
  <br>
  <h3 class="text-center">LOGOWANIE</h3>
  <br>
  <div class="card content">
    <div class="card-body">
      <form @submit.prevent="onSubmit()">
        <div class="form-group">
          <input type="email" class="form-control" v-model="form.email" placeholder="Email" required><br>
        </div>
        <div class="form-group">
          <input type="password" class="form-control" v-model="form.password" placeholder="Hasło" required><br>
        </div>
        <div class="form-group">
          <button class="btn btn-outline-info" type="submit">Zaloguj</button><br>
        </div>
        <div class="form-group">
          <br>Zapomniałeś hasła?
          <router-link to="/recovery">Przypomnij.</router-link>
        </div>
        <div class="form-group">
          Nie masz konta?
          <router-link to="/register">Zarejestruj się.</router-link><br>
        </div>
        <div class="text-danger">{{ form.error }}</div>
        <div class="text-info">{{ form.message }}</div>
      </form>
    </div>
  </div>
</template>
  
<script>
import Mixins from '@/mixins';

export default {
  mixins: [Mixins],
  data() {
    return {
      form: {
        email: "",
        password: "",
        error: "",
        message: ""
      }
    };
  },
  async beforeMount() {
    const valid = await Mixins.methods.checkValidity(this.$route.name);

    if(valid.isLogged) {
      this.redirect();
    }
  },
  methods: {
    onSubmit() {
      this.login();
    },
    login() {
      let prepareBody = {
        email: this.form.email,
        password: this.form.password
      };

      let formBody = [];
      for (var property in prepareBody) {
        var encodedKey = encodeURIComponent(property);
        var encodedValue = encodeURIComponent(prepareBody[property]);
        formBody.push(encodedKey + "=" + encodedValue);
      }
      formBody = formBody.join("&");

      this.$http.post("/login", formBody, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        }
      })
        .then(data => {
          this.form.message = data.data.message;
          this.form.error = "";
          localStorage.setItem("token", data.data.access_token);
          localStorage.setItem("refreshToken", data.data.refresh_token);

          location.reload(true);
        }).catch(error => {
          this.form.error = error.response.data.message;
          this.form.message = "";
        });
    },
    redirect() {
      return this.$router.replace({
            name: "Home"
          });
    }

  }
}
</script>
  
  