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
      orders: [],
      products: [],
      productTags: []
    };
  },
  async created() {
    const data = await Mixins.methods.checkValidity();

    if(!data.isAdmin) {
      return this.$router.replace({
        name: "Home"
      });
    }
  },
  beforeMount() {
    this.$http.get("/products").then(data => {
      this.products = data.data;
    })

    this.$http.get("/products/tags").then(data => {
      this.productTags = data.data;
    })


    this.$http.get("/orders").then(data => {
      this.orders = data.data;
    })
  }
}

</script>