<template>
    <br>
    <h3 class="text-center">ODZYSKIWANIE</h3>
    <br>
    <div class="card content">
        <div class="card-body">
            <form @submit.prevent="onSubmit()">
                <div class="form-group">
                    <input type="email" class="form-control" v-model="form.email" placeholder="Email" required><br>
                </div>
                <div class="form-group">
                    <button class="btn btn-outline-info" type="submit">Przypomnij hasło</button><br>
                </div>
                <div class="form-group">
                    <span id="message"></span>
                </div>
                <div class="form-group">
                    <br>Nie masz konta?
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
                error: "",
                message: ""
            }
        };
    },
    methods: {
        onSubmit() {
            this.recovery();
        },
        recovery() {
            let prepareBody = JSON.stringify({
                email: this.form.email
            });
            this.axios.post("/recovery/" + this.form.email, prepareBody)
                .then(data => {
                    console.log(data);
                    this.form.message = data.data.message;
                    this.form.error = "";
                })
                .catch(error => {
                    this.form.error = error.response.data.message;
                    this.form.message = "";
                });
        }
    }
}
</script>
      
      