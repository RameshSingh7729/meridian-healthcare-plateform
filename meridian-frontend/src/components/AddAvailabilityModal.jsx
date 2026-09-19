import "../assets/css/addDoctorModal.css";
import { useState } from "react";
import { toast } from "react-toastify";
import { addAvailability } from "../api/doctorApi";

function AddAvailabilityModal({
    show,
    doctorId,
    closeModal
}) {

    const initialAvailability = {
        date: "",
        startTime: "",
        endTime: "",
        slotDuration: 30
    };

    const [availability, setAvailability] = useState(initialAvailability);
    const [loading, setLoading] = useState(false);

    if (!show) return null;

    const handleChange = (e) => {

        setAvailability({
            ...availability,
            [e.target.name]: e.target.value
        });

    };

    const validateForm = () => {

        if (!availability.date) {
            toast.error("Please select Date");
            return false;
        }

        if (!availability.startTime) {
            toast.error("Please select Start Time");
            return false;
        }

        if (!availability.endTime) {
            toast.error("Please select End Time");
            return false;
        }

        if (!availability.slotDuration) {
            toast.error("Please enter Slot Duration");
            return false;
        }

        return true;
    };

    const saveAvailability = async (e) => {

        e.preventDefault();

        if (!validateForm()) return;

        setLoading(true);

        try {

            await addAvailability(doctorId, {
                date: availability.date,
                startTime: availability.startTime + ":00",
                endTime: availability.endTime + ":00",
                slotDuration: Number(availability.slotDuration)
            });

            toast.success("Availability Added Successfully");

            setAvailability(initialAvailability);

            closeModal();

        } catch (error) {

            console.error(error);

            toast.error(
                error.response?.data?.message ||
                "Unable to add availability"
            );

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="modal-overlay">

            <div className="modal-box">

                <h2>Add Doctor Availability</h2>

                <form
                    onSubmit={saveAvailability}
                    className="doctor-form"
                >

                    <div className="form-grid">

                        <div>
                            <label>Date</label>

                            <input
                                type="date"
                                name="date"
                                value={availability.date}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Start Time</label>

                            <input
                                type="time"
                                name="startTime"
                                value={availability.startTime}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>End Time</label>

                            <input
                                type="time"
                                name="endTime"
                                value={availability.endTime}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Slot Duration (Minutes)</label>

                            <select
                                name="slotDuration"
                                value={availability.slotDuration}
                                onChange={handleChange}
                            >
                                <option value={15}>15</option>
                                <option value={20}>20</option>
                                <option value={30}>30</option>
                                <option value={45}>45</option>
                                <option value={60}>60</option>
                            </select>
                        </div>

                    </div>

                    <div className="modal-buttons">

                        <button
                            type="button"
                            className="cancel-btn"
                            onClick={() => {

                                setAvailability(initialAvailability);

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
                                ? "Generating..."
                                : "Generate Slots"}
                        </button>

                    </div>

                </form>

            </div>

        </div>

    );

}

export default AddAvailabilityModal;