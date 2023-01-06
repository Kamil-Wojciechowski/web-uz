<template>
  <br>
  <h3 class="text-center">Witamy w naszym sklepie!</h3>
  <br>
  <div v-for="product in products" :key="product.id" class="card cards" style="width: 80vw;">
  <h5 class="card-header">{{ product.name }}</h5>
  <div class="card-body">
    <p class="card-text">Opis produktu: {{ product.description }}</p>
    <p class="card-text">Cena: {{product.priceUnit}}</p>
    <p class="card-text">Kategoria: {{product.productTag.name}}</p>
    <p class="card-text">Dostępne sztuki: {{ product.available }}</p>
    <router-link class="btn btn-outline-info" :to="'/products/'+ product.id ">Obejrzyj</router-link>
    <button v-if="product.available != 0" @click="buy(product.id, product.available, product.name)" class="btn btn-outline-info">Kup</button>
    <p v-else >Produkt wyprzedany</p>
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
  },
  methods: {
    buy(id, available, name){
          let items = [];
            var amount = 1;
            if(localStorage.getItem("items")){
              items = JSON.parse(localStorage.getItem("items"));
              try {
                amount = (items.filter(product => product.productId == id))[0].amount;
              } catch {
                amount = 0;
              }
              if(available > amount) {
                amount += 1; 
              }
              items = (items.filter(product => product.productId != id)); 
              }
              
            items.push({'productId': id, 'amount': amount, 'name': name});
            localStorage.setItem('items', JSON.stringify(items));
            console.log(items);
            alert("Dodano do koszyka");
        }
      }
    }

</script>

