<template>

    <br>
    <h3 class="text-center">NOWE HASŁO</h3>
    <br>
    <div class="card content">
        <div class="card-body">
            <form @submit.prevent="onSubmit()">
                <div class="form-group">
                    <input type="password" class="form-control" v-model="form.password" placeholder="Hasło" required
                        minlength="8"><br>
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" v-model="form.confirmedPassword"
                        placeholder="Powtórz hasło" required minlength="8"><br>
                </div>
                <div class="form-group">
                    <button class="btn btn-outline-info" type="submit">Zmień hasło</button><br>
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
                password: "",
                confirmedPassword: "",
                errors: "",
                message: "",
                check: ""
            }
        };
    },
    methods: {
        onSubmit() {
            this.recovery();
        },
        recovery() {
            let prepareBody = JSON.stringify({
                password: this.form.password,
                confirmedPassword: this.form.confirmedPassword
            });

            console.log(prepareBody);
            this.axios.post("/recovery/token/" + this.$route.params.token, prepareBody)
                .then(data => {
                    console.log(data);
                    console.log(this.$route.params.token);
                    this.form.message = data.data.message;
                    this.form.error = "";
                })
                .catch(error => {
                    console.log(this.$route.params.token);
                    console.log(error);
                    this.form.error = error.response.data.message;
                    this.form.message = "";
                });
        }
    }
}
</script>
      
      