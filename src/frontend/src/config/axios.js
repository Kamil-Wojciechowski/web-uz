import axios from 'axios';

const token = `Bearer ${localStorage.getItem('token')}`;

var headers;

if(token != 'Bearer ') {
    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        "Access-Control-Allow-Origin": "*",
        "Authorization": token};
} else {
    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        "Access-Control-Allow-Origin": "*"};
}

console.log(headers);

export default axios.create({
    baseURL: "http://localhost:8080/api/v1",
    headers: headers
});