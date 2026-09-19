import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import { getDoctorSlots } from "../api/doctorApi";
import { toast } from "react-toastify";
import "../assets/css/doctorSlots.css";

function DoctorSlots() {

    const { doctorId } = useParams();
    const navigate = useNavigate();

    const today = new Date().toISOString().split("T")[0];

    const [date, setDate] = useState(today);
    const [slots, setSlots] = useState([]);

    useEffect(() => {
        loadSlots();
    }, []);

    const loadSlots = async () => {

        try {

            const response = await getDoctorSlots(doctorId, date);

            setSlots(response.data);

        } catch (error) {

            console.error(error);

            toast.error("Unable to load doctor slots");

        }

    };

    const searchSlots = async () => {

        try {

            const response = await getDoctorSlots(doctorId, date);

            setSlots(response.data);

        } catch (error) {

            console.error(error);

            toast.error("Unable to load doctor slots");

        }

    };

    return (

        <div className="dashboard-container">

            <Navbar />

            <div className="dashboard-body">

                <Sidebar />

                <div className="slot-container">

                    <div className="slot-header">

                        <h2>Doctor Slots</h2>

                        <button
                            className="back-btn"
                            onClick={() => navigate("/doctors")}
                        >
                            Back
                        </button>

                    </div>

                    <div className="slot-search">

                        <input
                            type="date"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
                        />

                        <button
                            className="search-btn"
                            onClick={searchSlots}
                        >
                            Search
                        </button>

                    </div>

                    <table className="slot-table">

                        <thead>

                            <tr>

                                <th>Slot ID</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Status</th>

                            </tr>

                        </thead>

                        <tbody>

                            {
                                slots.length === 0 ?

                                    <tr>

                                        <td colSpan="4" className="no-data">

                                            No Slots Available

                                        </td>

                                    </tr>

                                    :

                                    slots.map(slot => (

                                        <tr key={slot.slotId}>

                                            <td>{slot.slotId}</td>

                                            <td>{slot.appointmentDate}</td>

                                            <td>{slot.slotTime}</td>

                                            <td>

                                                <span
                                                    className={
                                                        slot.status === "AVAILABLE"
                                                            ? "available"
                                                            : "booked"
                                                    }
                                                >
                                                    {slot.status}
                                                </span>

                                            </td>

                                        </tr>

                                    ))

                            }

                        </tbody>

                    </table>

                </div>

            </div>

        </div>

    );

}

export default DoctorSlots;