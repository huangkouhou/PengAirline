import axios from "axios";

export default class ApiService {

    static BASE_URL = "http://localhost:8082/api";

    static saveToken(token){
        localStorage.setItem("token", token);
    }

    static getToken(){
        return localStorage.getItem("token");
    }

    //save role
    static saveRole(roles) {
        localStorage.setItem("roles", JSON.stringify(roles));
    }


}