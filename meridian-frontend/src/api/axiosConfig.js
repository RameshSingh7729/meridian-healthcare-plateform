import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json"
    }
});

api.interceptors.request.use(

    (config) => {

        const token = localStorage.getItem("token");

        console.log("TOKEN =", token);

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        console.log("Authorization Header =", config.headers.Authorization);

        return config;
    },

    (error) => Promise.reject(error)

);

export default api;