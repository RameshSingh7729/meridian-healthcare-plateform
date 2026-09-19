import "../assets/css/addDoctorModal.css";
import { useEffect, useState } from "react";
import { createDoctor, updateDoctor } from "../api/doctorApi";
import { toast } from "react-toastify";

function AddDoctorModal({
    show,
    doctor: selectedDoctor,
    closeModal,
    refreshDoctors
}) {

    const initialDoctor = {
        userId: "",
        hospitalName: "",
        specialization: "",
        experienceYears: "",
        consultationFee: "",
        addressLine: "",
        area: "",
        city: "",
        state: "",
        country: "",
        pincode: "",
        image: ""
    };

    const [doctor, setDoctor] = useState(initialDoctor);
    const [loading, setLoading] = useState(false);

    // Populate form when editing
    useEffect(() => {

        if (selectedDoctor) {

            setDoctor({
                userId: selectedDoctor.userId || "",
                hospitalName: selectedDoctor.hospitalName || "",
                specialization: selectedDoctor.specialization || "",
                experienceYears: selectedDoctor.experienceYears || "",
                consultationFee: selectedDoctor.consultationFee || "",
                addressLine: selectedDoctor.addressLine || "",
                area: selectedDoctor.area || "",
                city: selectedDoctor.city || "",
                state: selectedDoctor.state || "",
                country: selectedDoctor.country || "",
                pincode: selectedDoctor.pincode || "",
                image: selectedDoctor.image || ""
            });

        } else {

            setDoctor(initialDoctor);

        }

    }, [selectedDoctor]);

    if (!show) return null;

    const handleChange = (e) => {
        setDoctor({
            ...doctor,
            [e.target.name]: e.target.value
        });
    };

    const validateForm = () => {

        if (!doctor.userId.trim()) {
            toast.error("User ID is required");
            return false;
        }

        if (!doctor.hospitalName.trim()) {
            toast.error("Hospital Name is required");
            return false;
        }

        if (!doctor.specialization.trim()) {
            toast.error("Specialization is required");
            return false;
        }

        if (!doctor.experienceYears) {
            toast.error("Experience is required");
            return false;
        }

        if (!doctor.consultationFee) {
            toast.error("Consultation Fee is required");
            return false;
        }

        if (!doctor.city.trim()) {
            toast.error("City is required");
            return false;
        }

        if (!doctor.state.trim()) {
            toast.error("State is required");
            return false;
        }

        if (!doctor.country.trim()) {
            toast.error("Country is required");
            return false;
        }

        return true;
    };

    const saveDoctor = async (e) => {

        e.preventDefault();

        if (!validateForm()) return;

        setLoading(true);

        try {

            if (selectedDoctor) {

                await updateDoctor(selectedDoctor.id, doctor);

                toast.success("Doctor Updated Successfully");

            } else {

                await createDoctor(doctor);

                toast.success("Doctor Added Successfully");

            }

            setDoctor(initialDoctor);

            refreshDoctors();

            closeModal();

        } catch (error) {

            console.error(error);

            toast.error("Unable to save doctor");

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="modal-overlay">

            <div className="modal-box">

                <h2>
                    {selectedDoctor ? "Edit Doctor" : "Add New Doctor"}
                </h2>

                <form onSubmit={saveDoctor} className="doctor-form">

                    <div className="form-grid">

                        <input
                            type="text"
                            name="userId"
                            placeholder="User ID (UUID)"
                            value={doctor.userId}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="hospitalName"
                            placeholder="Hospital Name"
                            value={doctor.hospitalName}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="specialization"
                            placeholder="Specialization"
                            value={doctor.specialization}
                            onChange={handleChange}
                        />

                        <input
                            type="number"
                            name="experienceYears"
                            placeholder="Experience (Years)"
                            value={doctor.experienceYears}
                            onChange={handleChange}
                        />

                        <input
                            type="number"
                            name="consultationFee"
                            placeholder="Consultation Fee"
                            value={doctor.consultationFee}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="addressLine"
                            placeholder="Address Line"
                            value={doctor.addressLine}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="area"
                            placeholder="Area"
                            value={doctor.area}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="city"
                            placeholder="City"
                            value={doctor.city}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="state"
                            placeholder="State"
                            value={doctor.state}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="country"
                            placeholder="Country"
                            value={doctor.country}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="pincode"
                            placeholder="Pincode"
                            value={doctor.pincode}
                            onChange={handleChange}
                        />

                        <input
                            type="text"
                            name="image"
                            placeholder="Image URL"
                            value={doctor.image}
                            onChange={handleChange}
                        />

                    </div>

                    <div className="modal-buttons">

                        <button
                            type="button"
                            className="cancel-btn"
                            onClick={() => {
                                setDoctor(initialDoctor);
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
                                ? (selectedDoctor ? "Updating..." : "Saving...")
                                : (selectedDoctor ? "Update Doctor" : "Save Doctor")}
                        </button>

                    </div>

                </form>

            </div>

        </div>

    );

}

export default AddDoctorModal;