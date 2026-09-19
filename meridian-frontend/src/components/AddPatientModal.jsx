import "../assets/css/addDoctorModal.css";
import { useEffect, useState } from "react";
import { createPatient, updatePatient } from "../api/patientApis";
import { toast } from "react-toastify";

function AddPatientModal({
    show,
    patient: selectedPatient,
    closeModal,
    refreshPatients
}) {

    const initialPatient = {
        name: "",
        email: "",
        phone: "",
        age: "",
        gender: "",
        address: ""
    };

    const [patient, setPatient] = useState(initialPatient);
    const [loading, setLoading] = useState(false);

    useEffect(() => {

        if (selectedPatient) {

            setPatient({
                name: selectedPatient.name || "",
                email: selectedPatient.email || "",
                phone: selectedPatient.phone || "",
                age: selectedPatient.age || "",
                gender: selectedPatient.gender || "",
                address: selectedPatient.address || ""
            });

        } else {

            setPatient(initialPatient);

        }

    }, [selectedPatient]);

    if (!show) return null;

    const handleChange = (e) => {

        setPatient({
            ...patient,
            [e.target.name]: e.target.value
        });

    };

    const validateForm = () => {

        if (!patient.name.trim()) {
            toast.error("Name is required");
            return false;
        }

        if (!patient.email.trim()) {
            toast.error("Email is required");
            return false;
        }

        if (!patient.phone.trim()) {
            toast.error("Phone Number is required");
            return false;
        }

        if (!patient.age) {
            toast.error("Age is required");
            return false;
        }

        if (!patient.gender.trim()) {
            toast.error("Gender is required");
            return false;
        }

        if (!patient.address.trim()) {
            toast.error("Address is required");
            return false;
        }

        return true;
    };

    const savePatient = async (e) => {

        e.preventDefault();

        if (!validateForm()) return;

        setLoading(true);

        try {

            if (selectedPatient) {

                await updatePatient(selectedPatient.id, patient);

                toast.success("Patient Updated Successfully");

            } else {

                await createPatient(patient);

                toast.success("Patient Added Successfully");

            }

            setPatient(initialPatient);

            refreshPatients();

            closeModal();

        } catch (error) {

            console.error(error);

            toast.error("Unable to save patient");

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="modal-overlay">

            <div className="modal-box">

                <h2>
                    {selectedPatient ? "Edit Patient" : "Add New Patient"}
                </h2>

                <form onSubmit={savePatient} className="doctor-form">

                    <div className="form-grid">

                        <input
                            type="text"
                            name="name"
                            placeholder="Patient Name"
                            value={patient.name}
                            onChange={handleChange}
                        />

                        <input
                            type="email"
                            name="email"
                            placeholder="Email"
                            value={patient.email}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="phone"
                            placeholder="Phone Number"
                            value={patient.phone}
                            onChange={handleChange}
                        />

                        <input
                            type="number"
                            name="age"
                            placeholder="Age"
                            value={patient.age}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="gender"
                            placeholder="Gender"
                            value={patient.gender}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="address"
                            placeholder="Address"
                            value={patient.address}
                            onChange={handleChange}
                        />

                    </div>

                    <div className="modal-buttons">

                        <button
                            type="button"
                            className="cancel-btn"
                            onClick={() => {
                                setPatient(initialPatient);
                                closeModal();
                            }}
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            className="save-btn"
                            disabled={loading}
                        >
                            {loading
                                ? (selectedPatient ? "Updating..." : "Saving...")
                                : (selectedPatient ? "Update Patient" : "Save Patient")}
                        </button>

                    </div>

                </form>

            </div>

        </div>

    );

}

export default AddPatientModal;