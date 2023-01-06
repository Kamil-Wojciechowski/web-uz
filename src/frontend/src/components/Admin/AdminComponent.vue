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
        <font-awesome-icon title="Dodaj" icon="fa-solid fa-plus" @click="product={}"/>
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
        <td v-else><font-awesome-icon title="Nie" icon="fa-solid fa-x"/></td>
        <td>
          <font-awesome-icon title="Edytuj" icon="fa-solid fa-edit" @click="chooseProductItem(product.id, true)"/>
          <font-awesome-icon title="Usun" icon="fa-solid fa-minus" @click="chooseProductItem(product.id, false)"/>
        </td>
      </tr>
      </tbody>
    </table>

  </div>
    <form @submit.prevent="productFormHandler()">
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
        <input type="checkbox" v-model="product.isVisible">Widoczny<br>
      </div>
      <div class="form-group">
        <input type="text" class="form-control" v-model="product.videoUrl" placeholder="Link"><br>
      </div>
      <div class="form-group">
        <input type="file" class="form-control" @change="readFile"><br>
      </div>

      <img class="form-group" :src="product.imageBase" alt="">

      <div class="form-group">
        <button class="btn btn-outline-info" type="submit">{{ (product.id > 0)? "Edytuj" : "Dodaj" }}</button><br>
      </div>

      <div v-for="error in errors" :key="error" class="text-danger">{{ error }}</div>
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
          <font-awesome-icon title="Edytuj" icon="fa-solid fa-edit" @click="chooseOrderItem(order.id)"/>
          <font-awesome-icon title="Pobierz fakture" icon="fa-solid fa-file" @click="downloadFv(order.id)"/>
          <font-awesome-icon title="Pobierz nadanie" icon="fa-solid fa-truck-fast" @click="downloadShipment(order.id)"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

  <div>
    <form @submit.prevent="editOrder">
      <div  class="form-group">
        <select v-model="order.status">
          <option v-for="status in statuses" :key="status" :value="status">{{status}}</option>
        </select>
      </div>
      <div class="form-group">
        <button class="btn btn-outline-info" type="submit">Edytuj</button><br>
      </div>
    </form>
    <div  class="form-group">
      <label>Status płatności: {{ payment.status }}</label>
    </div>
    </div>

  <div>
    <table class="table">
      <thead>
      <th scope="col">#</th>
      <th scope="col">Nazwa</th>
      <th scope="col">Rodzic</th>
      <th scope="col">Opcje </th>
      <font-awesome-icon title="Dodaj" icon="fa-solid fa-plus" @click="productTag={}"/>
      </thead>
      <tbody>
      <tr v-for="tag in productTags" :key="tag.id">
        <td>{{ tag.id }}</td>
        <td>{{ tag.name }}</td>
        <td>{{ (tag.parentId == null) ? "": tag.parentId.name }}</td>
        <td>
          <font-awesome-icon title="Edytuj" icon="fa-solid fa-edit" @click="chooseTagItem(tag.id, true)"/>
          <font-awesome-icon title="Usun" icon="fa-solid fa-minus" @click="chooseTagItem(tag.id, false)"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

  <div>
  <form @submit.prevent="tagFormHandler()">
    <div class="form-group">
      <input type="text" class="form-control" v-model="productTag.name" placeholder="Nazwa" required
             minlength="3"><br>
    </div>
    <div  class="form-group">
      <select v-model="productTag.parentId">
        <option v-for="tag in productTags" :key="tag.id" :value="tag.id">{{tag.name}}</option>
      </select>
    </div>
    <div class="form-group">
      <button class="btn btn-outline-info" type="submit">{{ (productTag.id > 0)? "Edytuj" : "Dodaj" }}</button><br>
    </div>

    <div v-for="error in errors" :key="error" class="text-danger">{{ error }}</div>
  </form>
  </div>

</template>

<script>
import Mixins from '@/mixins';

export default {
  data(){
    return {
      product: {},
      order: {},
      payment: {},
      productTag: {},
      orders: [],
      products: [],
      productTags: [],
      statuses: [
        "Nadane",
        "Wysłane",
        "Zakończone"
      ],
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
    productFormHandler() {
      console.log(this.product.id);
      if(this.product.id === undefined) {
        this.$http.post("/products", this.product).then(() => {
          this.errors = [];
          this.writeProducts();
        }).catch((error) => {
          console.log(error);
          this.errors = error.response.data.errors;
        });
      } else {
        this.$http.patch("/products/" + this.product.id, this.product).then(() => {
          this.errors = [];
          this.writeProducts();
        }).catch((error) => {
          this.errors = error.response.data.errors;
        });
      }
    },
    readFile() {
      const file = document.querySelector('input[type=file]').files[0]
      let fr = new FileReader();
      fr.readAsDataURL(file);

      fr.onloadend = () => {
        this.product.imageBase = fr.result.split(",")[1];
        console.log(this.product.imageBase);
      }
    },
    chooseProductItem(id, edit) {
      if(edit) {
        this.$http.get("/products/" + id).then((data) => {
          this.product = data.data;
          this.product.productTag = data.data.productTag.id;
          console.log(data.data);
        })
      } else {
        this.$http.delete("/products/" + id).then(() => {
          this.writeProducts();
        });
      }
    },
    chooseOrderItem(id) {
        this.$http.get("/orders/" + id).then((data) => {
          this.order = data.data;
          console.log(data.data);
        })

      this.$http.get("/payments/last/" + id).then((data) => {
        this.payment = data.data;
        console.log(data.data);
      })
    },
    editOrder() {
      console.log(this.order);
      this.$http.patch("/orders/" + this.order.id, this.order).then(() => {
        this.writeOrders();
      })
    },
    downloadFv(id) {
      this.$http.get("/pdf/invoice?id_order=" + id).then((data) => {
        console.log(data.data);
      })
    },
    downloadShipment(id) {
      this.$http.get("/pdf/label?id_order=" + id).then((data) => {
        console.log(data.data);
      })
    },
    chooseTagItem(id, edit) {
      if(edit) {
        this.$http.get("/products/tags/" + id).then((data) => {
          this.productTag = data.data;
          this.productTag.parentId = data.data.parentId.id;
          console.log(data.data);
        }).catch((error) => console.log(error))
      } else {
        this.$http.delete("/products/" + id).then(() => {
          this.writeTags();
        });
      }
    },
    tagFormHandler() {
      if(this.productTag.id === undefined) {
        this.$http.post("/products/tags/", this.productTag).then(() => {
          this.errors = [];
          this.writeTags();
        }).catch((error) => {
          console.log(error);
          this.errors = error.response.data.errors;
        });
      } else {
        this.$http.patch("/products/tags/" + this.productTag.id, this.productTag).then(() => {
          this.errors = [];
          this.writeTags();
        }).catch((error) => {
          this.errors = error.response.data.errors;
        });
      }
    },
  }
}

</script>