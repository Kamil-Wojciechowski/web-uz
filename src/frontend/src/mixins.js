
import http from './config/axios';

export default {
    methods: {
      async checkValidity() {
        let response = { isLogged: false, isAdmin: false };

        await http.get("/users").then(data => {
            console.log(data);
            if(data.request.status == 200) {
                response.isLogged = true;
                response.isAdmin = data.data.authorities[0].authority == "ROLE_ADMIN"
            } else {
                response.isLogged = false;
            }
      }).catch(() => {
        response.isLogged = false;
      })
    
    return response;
    },
  }
};