<template>
    <br>
    <h3 class="text-center">{{ product.name }}</h3>
    
    <div class="card cards" style="width: 30vw;">
      <img class="card-img-top" :src="product.imageBase" alt=""> 
    <div class="card-body">
      <p class="card-text">Opis produktu: {{ product.description }}</p>
      <p class="card-text">Cena: {{product.priceUnit}}</p>
      <p class="card-text">Kategoria: {{product.productTag.name}}</p>
      <p class="card-text">Dostępne sztuki: {{ product.available }}</p>
      <a class= "text-info " :href="product.videoUrl">Zobacz film promocyjny</a><br><br>
      <button v-if="product.available != 0" @click="buy(product.id, product.available, product.name)" class="btn btn-outline-info">Kup</button>
      <p v-else >Produkt wyprzedany</p>
    </div>
    </div>
    

      
    
    
  </template>
  
  <script>

  export default {
    data(){
      return {
        product: {},
      };
    },
    beforeMount() {
      this.$http.get("/products/" + this.$route.params.productId).then(data => {
        this.product = data.data;
        console.log(this.product);
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
  
  