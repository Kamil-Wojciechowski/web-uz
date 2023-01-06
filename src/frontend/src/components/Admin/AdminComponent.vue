<template>
  <div>
    <table class="table">
      <thead>
        <th scope="col">#</th>
        <th scope="col">Nazwa</th>
        <th scope="col">Opis</th>
        <th scope="col">Ilosc</th>
        <th scope="col">Ilość wykupionych</th>
        <th scope="col">Pozostało</th>
        <th scope="col">Kwota za sztuke</th>
        <th scope="col">Widocznę dla klienta</th>
        <th scope="col">Opcje </th>
        <font-awesome-icon title="Dodaj" icon="fa-solid fa-plus"/>
      </thead>
      <tbody>
      <tr v-for="product in products" :key="product.id">
        <td>{{ product.id }}</td>
        <td>{{ product.name }}</td>
        <td>{{ product.description }}</td>
        <td>{{ product.amount }}</td>
        <td>{{ product.amountBought }}</td>
        <td>{{ product.available }}</td>
        <td>{{ product.priceUnit }}</td>
        <td v-if="product.isVisible"><font-awesome-icon title="Tak" icon="fa-solid fa-check"/></td>
        <td v-else><font-awesome-icon title="Nie" icon="fa-solid fa-cross"/></td>
        <td>
          <font-awesome-icon title="Pokaz" icon="fa-solid fa-eye"/>
          <font-awesome-icon title="Edytuj" icon="fa-solid fa-edit"/>
          <font-awesome-icon title="Usun" icon="fa-solid fa-minus"/>
        </td>
      </tr>
      </tbody>
    </table>

  </div>
    <form @submit.prevent="addProduct()">
      <div class="form-group">
        <input type="text" class="form-control" v-model="product.name" placeholder="Nazwa" required
               minlength="3"><br>
      </div>
      <div  class="form-group">
        <select v-model="product.productTag">
          <option v-for="tag in productTags" :key="tag.id" :value="tag.id">{{tag.name}}</option>
        </select>
      </div>
      <div class="form-group">
        <input type="text" class="form-control" v-model="product.description" placeholder="Opis" required
               minlength="3"><br>
      </div>
      <div class="form-group">
        <input type="number" class="form-control" v-model="product.amount" placeholder="Sztuki" required><br>
      </div>
      <div class="form-group">
        <input type="number" class="form-control" v-model="product.priceUnit" placeholder="Cena za sztuke" step=".01" required><br>
      </div>
      <div class="form-group">
        <input type="checkbox" class="form-control" v-model="product.isVisible" placeholder="Widoczny">Widoczny<br>
      </div>
      <div class="form-group">
        <input type="text" class="form-control" v-model="product.videoUrl" placeholder="Link"><br>
      </div>
      <div class="form-group">
        <input type="file" class="form-control" ref="file" @change="readFile"><br>
      </div>
      <div class="form-group">
        <button class="btn btn-outline-info" type="submit">Dodaj</button><br>
      </div>

      <div class="text-danger">{{ errors }}</div>
    </form>
  <div>

  </div>

  <div>
    <table class="table">
      <thead>
      <th scope="col">#</th>
      <th scope="col">Status</th>
      <th scope="col">Opcje </th>
      </thead>
      <tbody>
      <tr v-for="order in orders" :key="order.id">
        <td>{{ order.id }}</td>
        <td>{{ order.orderStatus }}</td>
        <td>
          <font-awesome-icon title="Pokaz" icon="fa-solid fa-eye"/>
          <font-awesome-icon title="Edytuj" icon="fa-solid fa-edit"/>
          <font-awesome-icon title="Usun" icon="fa-solid fa-minus"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <div>
    <table class="table">
      <thead>
      <th scope="col">#</th>
      <th scope="col">Nazwa</th>
      <th scope="col">Rodzic</th>
      <th scope="col">Opcje </th>
      <font-awesome-icon title="Dodaj" icon="fa-solid fa-plus"/>
      </thead>
      <tbody>
      <tr v-for="tag in productTags" :key="tag.id">
        <td>{{ tag.id }}</td>
        <td>{{ tag.name }}</td>
        <td>{{ (tag.parentId == null) ? "": tag.parentId.name }}</td>
        <td>
          <font-awesome-icon title="Pokaz" icon="fa-solid fa-eye"/>
          <font-awesome-icon title="Edytuj" icon="fa-solid fa-edit"/>
          <font-awesome-icon title="Usun" icon="fa-solid fa-minus"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import Mixins from '@/mixins';

export default {
  data(){
    return {
      product: {},
      orders: [],
      products: [],
      productTags: [],
      errors: []
    };
  },
  async created() {
    const data = await Mixins.methods.checkValidity();

    if(!data.isAdmin || !data.isLogged) {
      return this.$router.replace({
        name: "Home"
      });
    }
  },
  beforeMount() {
    this.writeProducts();
    this.writeTags();
    this.writeOrders();
  },
  methods: {
    writeProducts() {
      this.products = [];

      this.$http.get("/products").then(data => {
        this.products = data.data;
      })
    },
    writeTags() {
      this.productTags = [];

      this.$http.get("/products/tags").then(data => {
        this.productTags = data.data;
      })
    },
    writeOrders() {
      this.orders = [];

      this.$http.get("/orders").then(data => {
        this.orders = data.data;
      })
    },
    addProduct() {
      console.log(this.product);

      this.$http.post("/products", this.product).then((data) => {
        this.writeProducts();
      }).catch((error) => {
        console.log(error);
      });



    },
    readFile() {
      const file = document.querySelector('input[type=file]').files[0]
      let fr = new FileReader();
      fr.readAsDataURL(file);

      fr.onloadend = () => {
        this.product.imageBase = fr.result;
      }
    }
  }
}

</script>