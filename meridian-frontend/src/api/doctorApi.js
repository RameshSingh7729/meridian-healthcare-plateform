import api from "./axiosConfig";

// Get all doctors
export const getDoctors = (page = 0, size = 10) => {
    return api.get(`/api/doctors?page=${page}&size=${size}`);
};

// Get doctor by id
export const getDoctor = (id) => {
    return api.get(`/api/doctors/${id}`);
};

// Search doctors
export const searchDoctors = (specialization) => {
    return api.get(`/api/doctors/search?specialization=${specialization}`);
};

// Create doctor
export const createDoctor = (doctor) => {
    return api.post("/api/doctors", doctor);
};

// Update doctor
export const updateDoctor = (id, doctor) => {
    return api.put(`/api/doctors/${id}`, doctor);
};

// Delete doctor
export const deleteDoctor = (id) => {
    return api.delete(`/api/doctors/${id}`);
};

// Add availability
export const addAvailability = (doctorId, availability) => {
    return api.post(
        `/api/doctors/${doctorId}/availability`,
        availability
    );
};

// View slots
export const getDoctorSlots = (doctorId, date) => {
    return api.get(
        `/api/doctors/${doctorId}/slots?date=${date}`
    );
};

// Book slot
export const bookSlot = (doctorId, appointmentTime) => {
    return api.put("/api/doctors/slots/book", {
        doctorId,
        appointmentTime
    });
};