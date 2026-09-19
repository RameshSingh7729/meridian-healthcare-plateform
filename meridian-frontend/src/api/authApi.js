import api from "./axiosConfig";

export const login = (email, password) => {

    return api.post("/api/auth/login", {
        email,
        password
    });

};
export const register = (user) => {

    return api.post("/api/auth/register", user);

};