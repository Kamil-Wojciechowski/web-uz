<template>

    <br>
    <br>
    <div class="card content">
        <div class="card-body">

        </div>
    </div>
</template>
      
<script>
export default {
    data() {
      return {
        token: this.$route.params.token
      }
    },
  created() {
    this.$http.post("/register/" + this.token).then(() => {
      alert("Aktywowany");
      return this.$router.replace({
        name: "Home"
      });
    }).catch(() => {
        alert("Token wygasł lub nie istnieje!");
        return this.$router.replace({
          name: "Home"
        });
      });
    }
}
</script>
      
      