<template>
<div v-if="buy" >
    <div class="modal fade" id="addressModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5">Dodaj adres</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="addAddress()">
            <div class="form-group">
                <input type="text" class="form-control" v-model="addressForm.firstname" placeholder="Imię" required><br>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" v-model="addressForm.lastname" placeholder="Nazwisko" required><br>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" v-model="addressForm.company" placeholder="Firma"><br>
            </div>
            <div class="form-group">
                <input type="number" class="form-control" v-model="addressForm.nip" placeholder="NIP"><br>
            </div>
            <div class="form-group">
                <input type="number" class="form-control" v-model="addressForm.mobileNumber" placeholder="Telefon" required><br>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" v-model="addressForm.street" placeholder="Ulica" required><br>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" v-model="addressForm.postalCode" placeholder="Kod pocztowy" required><br>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" v-model="addressForm.city" placeholder="Miasto" required><br>
            </div>
            <button type="submit" class="btn btn-outline-info">Dodaj</button>
            <div class="text-danger">{{ addressForm.error }}</div>
            <div class="text-info">{{ addressForm.message }}</div>
        </form>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="paymentModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5">Opłać zamówienie</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Status Twojej płatności: {{ paymentStatus.status }}
        <button v-if="paymentStatus.status==='Oczekiwanie'" @click="startPayment" class="btn btn-outline-info">Opłać zamówienie</button>
        <button v-else data-bs-dismiss="modal" class="btn btn-outline-info">Zamknij</button>
      </div>
    </div>
  </div>
</div>
<br><h5 class="text-center">Zawartość Twojego koszyka</h5><br>
<table class="table">
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Nazwa produktu</th>
      <th scope="col">Ilość</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="item in cart" :key="item.productId">
      <td>{{ item.productId }}</td>
      <td>{{ item.name }}</td>
      <td>{{ item.amount }}</td>
    </tr>
  </tbody>
</table>
<br>
<p>Wybierz adres dostawy: </p>
<div v-for="address in addresses" :key="address.id">
<li>{{ address.lastname }}, {{ address.firstname }}, {{ address.postalCode }}, {{ address.city }} 
<button class="btn btn-outline-info" @click="selectAddress(address.id)">Wybierz</button></li>
</div>
<p>lub <button data-bs-toggle="modal" data-bs-target="#addressModal" class="btn btn-outline-info">Dodaj nowy adres</button></p>

<p class="text-center">Aktualnie wybrany adres: {{ currentAddress.lastname }} {{ currentAddress.firstname }} {{ currentAddress.company }} {{ currentAddress.nip }} {{ currentAddress.mobileNumber }} {{ currentAddress.street }} {{ currentAddress.postalCode }} {{ currentAddress.city }}
</p>
<button v-if="currentAddress.id" @click="placeOrder" class="btn btn-outline-info">Złóż zamówienie</button><br>
<p>Status zamówienia: {{ orderStatus.orderStatus }}. Nie odświeżaj tej strony w trakcie składania zamówienia!</p>
<button v-if="orderStatus.orderStatus==='Nowe'" data-bs-toggle="modal" data-bs-target="#paymentModal" class="btn btn-outline-info">Opłać zamówienie</button>
<router-link v-if="orderStatus.orderStatus==='Opłacone'" @click="endOrder" to="/orderList" class="btn btn-outline-info">Zobacz swoje zamówienia</router-link>
<p v-if="orderStatus.orderStatus==='Opłacone'">Możesz opuścić tą stronę</p>

</div>
<div v-else class="text-center"><br><h3>Sprawdź swoje zamówienia!</h3><router-link to="/orderList" class="btn btn-outline-info">Zobacz swoje zamówienia</router-link></div>
</template>

<script>
import Mixins from '@/mixins';

export default{
    mixins: [Mixins],
    
    data(){
        return {
            buy: false,
            addresses: [],
            cart: [],
            addressForm: {
                firstname: "",
                lastname: "",
                company: "",
                nip: "",
                mobileNumber: "",
                street: "",
                postalCode: "",
                city: "",
                error: "",
                message:""
            },
            currentAddress: {},
            orderStatus: {
                orderStatus: "Oczekujące"
            },
            orderUnitStatus: {},
            payment: {},
            paymentStatus: {
                status: "Oczekiwanie"
            }
        }
    },
    async created(){
        const valid = await Mixins.methods.checkValidity(this.$route.name);

        if(!valid.isLogged) {
            return this.$router.replace({
            name: "Login"
          });
        }
    },
    beforeMount(){
        
        this.$http.get("/addresses").then(data =>{
            this.addresses = data.data;
            console.log(this.addresses);
        })
        if(localStorage.getItem("items")){
        this.cart = JSON.parse(localStorage.getItem("items"));
        if(this.cart.length>0){
          this.buy= true;
        }
      }
    },
    methods: {
        
        addAddress(){
            let prepareBody = {
                firstname: this.addressForm.firstname,
                lastname: this.addressForm.lastname,
                company: this.addressForm.company,
                nip: this.addressForm.nip,
                mobileNumber: this.addressForm.mobileNumber,
                street: this.addressForm.street,
                postalCode: this.addressForm.postalCode,
                city: this.addressForm.city,
            };
            this.$http.post("/addresses", prepareBody).then(data => {
                this.addressForm.message = data.data.message;
                this.addressForm.error = "";
                location.reload(true);
            }).catch(error => {
                this.addressForm.error = error.response.data.errors[0];
                this.addressForm.message = "";
            });
        },
        selectAddress(id){
            this.$http.get("/addresses/"+id).then(data =>
            {
                this.currentAddress = data.data;
            })
        },
        async placeOrder(){
            let prepareBody = {
                status: "Nowe",
                address: this.currentAddress.id
            };
            await this.$http.post("/orders", prepareBody).then(data => {
                this.orderStatus = data.data;
                this.currentAddress = "";
            });

            this.cart.forEach((value) => {
                let body = {
                    productId: value.productId,
                    amount: value.amount
                }
                
                this.$http.post("/orders/"+this.orderStatus.id+"/units", body).then(data =>{
                    this.orderUnitStatus = data.data; 
                    this.cart = [];
                    localStorage.removeItem("items");
                })
            })
        },
        async startPayment(){
            let body = {
                status: "Nowe",
                callbackData: "Oczekujące",
                orderId: this.orderStatus.id
            };
            await this.$http.post("/payments/", body).then(data => {
                this.payment = data.data;
            });
            let prepareBody = {
                status: "Opłacone"
            };
            await this.$http.patch("/orders/"+this.orderStatus.id, prepareBody).then(data => {
                this.orderStatus = data.data;
            });
            let bodyPayment = {
                status: "Opłacone",
                callbackData: "Opłacone",
                orderId: this.orderStatus.id
            };
            await this.$http.patch("/payments/"+this.payment.id, bodyPayment).then(data => {
                this.paymentStatus = data.data;
                alert("Płatność się powiodła");
            })
        },
        endOrder(){
            window.reload(true);
        }
    }
}
</script>