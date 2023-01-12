<template>
  <div>
    <h4>Produkty</h4>
    <table class="table">
      <thead>
        <th scope="col">#</th>
        <th scope="col">Nazwa</th>
        <th scope="col">Opis</th>
        <th scope="col">Ilość</th>
        <th scope="col">Sprzedane</th>
        <th scope="col">Pozostało</th>
        <th scope="col">Kwota za sztukę</th>
        <th scope="col">Widoczne dla klienta</th>
        <th scope="col">Opcje</th>
        <font-awesome-icon title="Dodaj" class="navicon" icon="fa-solid fa-plus" data-bs-toggle="modal" data-bs-target="#productModal" @click="product={}"/>
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
          <font-awesome-icon title="Edytuj" class="navicon" icon="fa-solid fa-edit" data-bs-toggle="modal" data-bs-target="#productModal" @click="chooseProductItem(product.id, true)"/>
          <font-awesome-icon title="Usuń" class="navicon" icon="fa-solid fa-minus" @click="chooseProductItem(product.id, false)"/>
        </td>
      </tr>
      </tbody>
    </table>

  </div>

<div class="modal fade" id="productModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5">Produkt</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      <form @submit.prevent="productFormHandler()">
      <div class="form-group">
        Nazwa produktu:
        <input type="text" class="form-control" v-model="product.name" placeholder="Nazwa" required
               minlength="3"><br>
      </div>
      <div  class="form-group">
        Kategoria produktu:
        <select v-model="product.productTag">
          <option v-for="tag in productTags" :key="tag.id" :value="tag.id">{{tag.name}}</option>
        </select>
      </div>
      <div class="form-group">
        Opis produktu:
        <input type="text" class="form-control" v-model="product.description" placeholder="Opis" required minlength="3"><br>
      </div>
      <div class="form-group">
        Ilość:
        <input type="number" class="form-control" v-model="product.amount" placeholder="Sztuki" required><br>
      </div>
      <div class="form-group">
        Cena za sztukę:
        <input type="number" class="form-control" v-model="product.priceUnit" placeholder="Cena za sztuke" step=".01" required><br>
      </div>
      <div class="form-group">
        <input type="checkbox" v-model="product.isVisible">Widoczny<br>
      </div>
      <div class="form-group">
        Link do wideo promocyjnego:
        <input type="text" class="form-control" v-model="product.videoUrl" placeholder="Link"><br>
      </div>
      <div class="form-group">
        Zdjęcie: 
        <input type="file" class="form-control" @change="readFile"><br>
      </div>

      <img class="form-group" :src="product.imageBase" alt="">

      <div class="form-group">
        <button class="btn btn-outline-info" type="submit">{{ (product.id > 0)? "Edytuj" : "Dodaj" }}</button><br>
      </div>

      <div v-for="error in errors" :key="error" class="text-danger">{{ error }}</div>
    </form>
      </div>
    </div>
  </div>
</div>
    
  <div>
  </div>

  <div>
    <h4>Zamówienia</h4>
    <table class="table">
      <thead>
      <th scope="col">#</th>
      <th scope="col">Status</th>
      <th scope="col">Użytkownik</th>
      <th scope="col">Opcje</th>
      </thead>
      <tbody>
      <tr v-for="order in orders" :key="order.id">
        <td>{{ order.id }}</td>
        <td>{{ order.orderStatus }}</td>
        <td>{{ order.addressId.userId.firstname }} {{ order.addressId.userId.lastname }}</td>
        <td>
          <font-awesome-icon class="navicon" title="Edytuj" data-bs-toggle="modal" data-bs-target="#orderModal" icon="fa-solid fa-edit" @click="chooseOrderItem(order.id)"/>
          <font-awesome-icon class="navicon" title="Pobierz fakturę" icon="fa-solid fa-file" @click="downloadFv(order.id)"/>
          <font-awesome-icon class="navicon" title="Pobierz etykietę" icon="fa-solid fa-truck-fast" @click="downloadShipment(order.id)"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <div>
    
<div class="modal fade" id="orderModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5">Zamówienie</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <label>Status płatności: {{ payment.status }}. Pamiętaj o wysyłaniu tylko opłaconych zamówień!</label>
        <form @submit.prevent="editOrder">
      <div  class="form-group">
        Nowy status:
        <select v-model="order.status">
          <option v-for="status in statuses" :key="status" :value="status">{{status}}</option>
        </select>
      </div>
      <div class="form-group">
        <button class="btn btn-outline-info" type="submit">Edytuj</button><br>
      </div>
    </form>
      </div>
    </div>
  </div>
</div>
    </div>

  <div>
    <h4>Kategorie</h4>
    <table class="table">
      <thead>
      <th scope="col">#</th>
      <th scope="col">Nazwa</th>
      <th scope="col">Rodzic</th>
      <th scope="col">Opcje </th>
      <font-awesome-icon title="Dodaj" class="navicon" data-bs-toggle="modal" data-bs-target="#categoryModal" icon="fa-solid fa-plus" @click="productTag={}"/>
      </thead>
      <tbody>
      <tr v-for="tag in productTags" :key="tag.id">
        <td>{{ tag.id }}</td>
        <td>{{ tag.name }}</td>
        <td>{{ (tag.parentId == null) ? "": tag.parentId.name }}</td>
        <td>
          <font-awesome-icon title="Edytuj" class="navicon" data-bs-toggle="modal" data-bs-target="#categoryModal" icon="fa-solid fa-edit" @click="chooseTagItem(tag.id, true)"/>
          <font-awesome-icon title="Usun" class="navicon" icon="fa-solid fa-minus" @click="chooseTagItem(tag.id, false)"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

  <div class="modal fade" id="categoryModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5">Kategoria</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="tagFormHandler()">
    <div class="form-group">
      Nazwa:
      <input type="text" class="form-control" v-model="productTag.name" placeholder="Nazwa" required
             minlength="3"><br>
    </div>
    <div  class="form-group">
      Rodzic:
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
    </div>
  </div>
</div>

  <div>
  
  </div>

</template>

<script>
import Mixins from '@/mixins';
import download from 'downloadjs';

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
        "Anulowane",
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
      if(this.product.id === undefined) {
        this.$http.post("/products", this.product).then(() => {
          this.errors = [];
          location.reload(true);
        }).catch((error) => {
          console.log(error);
          this.errors = error.response.data.errors;
        });
      } else {
        this.$http.patch("/products/" + this.product.id, this.product).then(() => {
          this.errors = [];
          location.reload(true);
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
      this.$http.get("/pdf/invoice?id_order=" + id, { responseType: 'blob' }).then((data) => {
        download(data.data, "faktura_"+id+".pdf", "application/pdf");
      })
    },
    downloadShipment(id) {
      this.$http.get("/pdf/label?id_order=" + id, { responseType: 'blob' }).then((data) => {
        download(data.data, "etykieta_"+id+".pdf", "application/pdf");
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
        this.$http.delete("/products/tags/" + id).then(() => {
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