<template>
  <h2>Logowanie</h2>
  <form @submit.prevent="onSubmit">
    <p>Your email is: {{form.email}}</p>
    <input type="email" v-model = "form.email" autocomplete="username" placeholder="Login"><br>
    <input type="password" v-model="form.password" autocomplete="current-password" placeholder="Hasło"><br>
    <button type="submit" @click='onSubmit()'>Zaloguj</button><br>Zapomniałeś hasła? <br>
    Nie masz konta? <br>
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
    onSubmit(){
      // this.form.email.$touch();
      return this.login();
    },
    login(){
      console.log(this);
      this.$http.post("/login", {
        email: this.form.email,
        password: this.form.password
      }).then(response =>{
          localStorage.setItem("token", response.data.token);
          this.$http.headers.Authorization = "Bearer "+response.data.token;
          console.log(response);
      })
    }

  }
}
</script>

