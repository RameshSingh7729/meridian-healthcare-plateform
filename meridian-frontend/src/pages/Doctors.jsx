import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import AddDoctorModal from "../components/AddDoctorModal";
import AddAvailabilityModal from "../components/AddAvailabilityModal";
import { useNavigate } from "react-router-dom";
import { getDoctors, deleteDoctor } from "../api/doctorApi";
import { toast } from "react-toastify";
import "../assets/css/doctor.css";

function Doctors() {

    const [doctors, setDoctors] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [search, setSearch] = useState("");
    const [selectedDoctor, setSelectedDoctor] = useState(null);
    const [showAvailabilityModal, setShowAvailabilityModal] = useState(false);
    const [selectedDoctorId, setSelectedDoctorId] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        loadDoctors();
    }, [page]);

    const loadDoctors = async () => {

        try {

            const response = await getDoctors(page, 10);

            setDoctors(response.data.content);
            setTotalPages(response.data.totalPages);

        } catch (error) {

            console.error(error);

            toast.error("Unable to load doctors");

        }

    };

    const handleDelete = async (id) => {

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this doctor?"
        );

        if (!confirmDelete) return;

        try {

            await deleteDoctor(id);

            toast.success("Doctor Deleted Successfully");

            loadDoctors();

        } catch (error) {

            console.error(error);

            toast.error("Unable to delete doctor");

        }

    };

    const filteredDoctors = doctors.filter((doctor) => {

        const keyword = search.toLowerCase().trim();

        return (

            doctor.hospitalName.toLowerCase().includes(keyword) ||

            doctor.specialization.toLowerCase().includes(keyword) ||

            doctor.city.toLowerCase().includes(keyword)

        );

    });

    return (

        <div className="dashboard-container">

            <Navbar />

            <div className="dashboard-body">

                <Sidebar />

                <div className="doctor-container">

                    <div className="doctor-header">

                        <h2>Doctor Management</h2>

                        <button
                            className="add-btn"
                            onClick={() => {
                                setSelectedDoctor(null);
                                setShowModal(true);
                            }}
                        >
                            Add Doctor
                        </button>

                    </div>

                    <div className="search-container">

                        <input
                            type="text"
                            placeholder="🔍 Search by Hospital, Specialization or City..."
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                            className="search-input"
                        />

                    </div>

                    <table className="doctor-table">

                        <thead>

                            <tr>

                                <th>Hospital</th>
                                <th>Specialization</th>
                                <th>Experience</th>
                                <th>Consultation Fee</th>
                                <th>City</th>
                                <th>Actions</th>

                            </tr>

                        </thead>

                        <tbody>

                            {

                                filteredDoctors.length === 0 ?

                                    (

                                        <tr>

                                            <td colSpan="6" className="no-data">

                                                No Doctors Found

                                            </td>

                                        </tr>

                                    )

                                    :

                                    filteredDoctors.map((doctor) => (

                                        <tr key={doctor.id}>

                                            <td>{doctor.hospitalName}</td>

                                            <td>{doctor.specialization}</td>

                                            <td>{doctor.experienceYears} Years</td>

                                            <td>₹ {doctor.consultationFee}</td>

                                            <td>{doctor.city}</td>

                                            <td>

    <div className="action-buttons">

        <button
            className="edit-btn"
            onClick={() => {
                setSelectedDoctor(doctor);
                setShowModal(true);
            }}
        >
            Edit
        </button>

        <button
            className="delete-btn"
            onClick={() => handleDelete(doctor.id)}
        >
            Delete
        </button>

        <button
            className="availability-btn"
            onClick={() => {
                setSelectedDoctorId(doctor.id);
                setShowAvailabilityModal(true);
            }}
        >
            Availability
        </button>

        <button
            className="view-btn"
            onClick={() => navigate(`/doctor-slots/${doctor.id}`)}
        >
            View Slots
        </button>

    </div>

</td>
                                        </tr>

                                    ))

                            }

                        </tbody>

                    </table>

                    <div className="pagination">

                        <button
                            disabled={page === 0}
                            onClick={() => setPage(page - 1)}
                        >
                            Previous
                        </button>

                        <span>

                            Page {page + 1} of {totalPages}

                        </span>

                        <button
                            disabled={page + 1 >= totalPages}
                            onClick={() => setPage(page + 1)}
                        >
                            Next
                        </button>

                    </div>

                </div>

            </div>

            <AddDoctorModal
                show={showModal}
                doctor={selectedDoctor}
                closeModal={() => {

                    setShowModal(false);

                    setSelectedDoctor(null);

                }}
                refreshDoctors={loadDoctors}
            />

             <AddAvailabilityModal
                 show={showAvailabilityModal}
                 doctorId={selectedDoctorId}
                 closeModal={() => {
                 setShowAvailabilityModal(false);
                 setSelectedDoctorId(null);
                }}
                />

        </div>

    );

}

export default Doctors;