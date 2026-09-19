import api from "./axiosConfig";

// Get all patients
export const getPatients = () => {
    return api.get("/api/patients");
};

// Get all doctors
export const getDoctors = () => {
    return api.get("/api/doctors?page=0&size=100");
};

// Get doctor slots
export const getDoctorSlots = (doctorId, date) => {
    return api.get(`/api/doctors/${doctorId}/slots?date=${date}`);
};

// Book appointment
export const bookAppointment = (appointment) => {
    return api.post("/api/appointments/book", appointment);
};

//Get all Booked Appointments
export const getAppointments = () =>
    api.get("/api/appointments");
