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

    //get role from the localStorage
    static getRoles(){
        const roles = localStorage.getItem("roles");
        return roles ? JSON.parse(roles) : null;
    }

    //check if the user has a specific role
    static hasRole(role){
        const roles = this.getRoles();
        return roles ? roles.includes(role) : false;
    }

    //check if the user is an admin
    static isAdmin(){
        return this.hasRole('ADMIN');
    }

    static isCustomer(){
        return this.hasRole('CUSTOMER');
    }

    static isPilot(){
        return this.hasRole('PILOT');
    }

    static logout(){
        localStorage.removeItem("token");
        localStorage.removeItem("roles");
    }

    static isAuthenticated(){
        const token = this.getToken();
        return !!token;
    }

    static getHeader(){
        const token = this.getToken();
        return {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    }

}