import axios from 'axios';

const token = `Bearer ${localStorage.getItem('token')}`;

var headers = {
    'Content-Type': 'application/json',
        'Accept': 'application/json',
    "Access-Control-Allow-Origin": "*"};

if(token == 'Bearer ') {
    headers = headers + {
        "Authorization": token
    }
}

export default axios.create({
    baseURL: "http://localhost:8080/api/v1",
    headers: headers
});