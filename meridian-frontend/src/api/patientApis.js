import api from "./axiosConfig";

// Get all patients
export const getPatients = () => {
    return api.get("/api/patients");
};

// Get patient by ID
export const getPatient = (id) => {
    return api.get(`/api/patients/${id}`);
};

// Create patient
export const createPatient = (patient) => {
    return api.post("/api/patients/createPatient", patient);
};

// Update patient
export const updatePatient = (id, patient) => {
    return api.put(`/api/patients/${id}`, patient);
};

// Delete patient
export const deletePatient = (id) => {
    return api.delete(`/api/patients/${id}`);
};

// Check patient exists
export const checkPatientExists = (id) => {
    return api.get(`/api/patients/${id}/exists`);
};