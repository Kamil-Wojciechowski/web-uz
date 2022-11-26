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
export default {
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
  methods: {
    onSubmit() {
      this.login();
    },
    login() {
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
          this.form.message = data.data.message;
        }).catch(error => {
          console.log(error);
          this.form.error = error.response.data.message;
        });
    }

  }
}
</script>
  
  