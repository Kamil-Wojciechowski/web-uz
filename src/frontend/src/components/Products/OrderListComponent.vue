<template>
<div class="modal fade" id="paymentModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5">Opłać zamówienie</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Status Twojej płatności: {{ paymentStatus.status }}
        <button v-if="paymentStatus.status==='Oczekiwanie'" @click="startPayment(paymentStatus.id)" class="btn btn-outline-info">Opłać zamówienie</button>
        <button v-else data-bs-dismiss="modal" class="btn btn-outline-info">Zamknij</button>
      </div>
    </div>
  </div>
</div>
    <div class="accordion accordion-flush" id="accordionExample" >
  <div class="accordion-item" v-for="order in orders" :key="order.id">
    <h2 class="accordion-header">
      <button class="accordion-button" @click="chooseOrder(order.id)" type="button" data-bs-toggle="collapse" :data-bs-target="'#collapse'+order.id" aria-expanded="true" aria-controls="collapseOne">
        Zamówienie: {{order.id}}
      </button>
    </h2>
    <div :id="'collapse'+order.id" class="accordion-collapse collapse" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
      <div class="accordion-body">
        <li v-for="orderUnit in orderUnits" :key="orderUnit.id">
        {{ orderUnit.productId.name }}, ilość: {{ orderUnit.amount }}
        </li>
        <p>Status zamówienia: {{order.orderStatus}}</p>
        <button v-if="order.orderStatus==='Nowe'" data-bs-toggle="modal" data-bs-target="#paymentModal" class="btn btn-outline-info">Opłać zamówienie</button>
        <button v-if="order.orderStatus==='Nowe'" @click="cancelOrder(order.id)" class="btn btn-outline-info">Anuluj zamówienie</button>
        <button v-if="order.orderStatus==='Opłacone'||order.orderStatus==='Wysłane'||order.orderStatus==='Zakończone'" @click="downloadInvoice(order.id)" class="btn btn-outline-info">Pobierz fakturę PDF</button>
        </div>
    </div>
  </div>
</div>
    </template>
    
    <script>
    import Mixins from '@/mixins';
    import download from 'downloadjs';
    
    export default{
        mixins: [Mixins],
        
        data(){
            return {
                orders: [],
                orderUnits: [],
                payment: {},
                paymentStatus: {
                    id: "",
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
            this.$http.get("/orders").then(data =>{
                this.orders = data.data;
            })
          },
          methods: {
            chooseOrder(id){
                this.$http.get("/orders/"+id+"/units").then(data =>{
                this.orderUnits = data.data;
                this.paymentStatus.id = id;
                console.log(this.paymentStatus.id)
            })
            },
            async startPayment(id){
                console.log(id);
            let body = {
                status: "Nowe",
                callbackData: "Oczekujące",
                orderId: id
            };
            await this.$http.post("/payments/", body).then(data => {
                this.payment = data.data;
            });
            let prepareBody = {
                status: "Opłacone"
            };
            await this.$http.patch("/orders/"+id, prepareBody).then(data => {
                this.orderStatus = data.data;
            });
            let bodyPayment = {
                status: "Opłacone",
                callbackData: "Opłacone",
                orderId: id
            };
            await this.$http.patch("/payments/"+this.payment.id, bodyPayment).then(data => {
                this.paymentStatus = data.data;
                alert("Płatność się powiodła");
                location.reload(true);
            })
        },
        downloadInvoice(id) {
            this.$http.get("/pdf/invoice?id_order=" + id, { responseType: 'blob' }).then((data) => {
                download(data.data, "faktura_"+id+".pdf", "application/pdf");
            })
        },
        cancelOrder(id){
            let prepareBody = {
                status: "Anulowane"
            };
            this.$http.patch("/orders/"+id, prepareBody).then(data => {
                this.orderStatus = data.data;
                location.reload(true);
            });
        }
        }
        }
        
        

    </script>