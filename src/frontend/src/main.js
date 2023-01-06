import { createApp } from 'vue';
import App from './App.vue';
import router from './router.js'
import VueAxios from 'vue-axios'
import http from './config/axios';

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* import specific icons */
import { faRightToBracket, faUserPlus, faRightFromBracket, faCartShopping, faCheck, faCross, faEye ,faPlus, faMinus, faEdit, faUser} from '@fortawesome/free-solid-svg-icons'

library.add(faRightToBracket, faUserPlus, faRightFromBracket, faCartShopping, faCheck, faCross, faEye, faPlus, faMinus, faEdit, faUser)

createApp(App).use(VueAxios, http).use(router).component('font-awesome-icon', FontAwesomeIcon).mount('#app')