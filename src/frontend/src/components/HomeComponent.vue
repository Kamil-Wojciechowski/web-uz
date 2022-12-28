<template>
  <br>
  <h3 class="text-center">Witamy w naszym sklepie!</h3>
  <br>

  <div class="card cards" style="width: 80vw;">
  <h5 class="card-header">Nazwa produktu</h5>
  <div class="card-body">
    <h5 class="card-title">Opis</h5>
    <p class="card-text">Cena</p>
    <p class="card-text">Kategoria</p>
    <p class="card-text">Dostepne sztuki:</p>
    <div class="btn-group" role="group">
      <a href="#" class="btn btn-outline-info">Obejrzyj</a>
      <a href="#" class="btn btn-outline-info">Kup</a>
    </div>
  </div>
  </div>
  
  
</template>

<script>
export default {
  data(){
    return {
      products: [],
      productTags: []
    };
  },
  beforeMount() {
    this.$http.get("/products").then(data => {
      this.products = data.data;
      console.log(this.products);
    })

    this.$http.get("/products/tags").then(data => {
      this.productTags = data.data;
      console.log(this.productTags);
    })
  }
}
</script>

