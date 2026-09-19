import api from "./axiosConfig";

export const createOrder = (data) => {
    return api.post("/api/payments/create-order", data);
};

export const verifyPayment = (data) => {
    return api.post("/api/payments/verify", data);
};