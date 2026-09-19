import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import AddPatientModal from "../components/AddPatientModal";
import {
    getPatients,
    deletePatient
} from "../api/patientApis";
import { toast } from "react-toastify";
import "../assets/css/doctor.css";

function Patients() {

    const [patients, setPatients] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [search, setSearch] = useState("");
    const [selectedPatient, setSelectedPatient] = useState(null);

    useEffect(() => {
        loadPatients();
    }, []);

    const loadPatients = async () => {

        try {

            const response = await getPatients();

            setPatients(response.data);

        } catch (error) {

            console.error(error);

            toast.error("Unable to load patients");

        }

    };

    const handleDelete = async (id) => {

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this patient?"
        );

        if (!confirmDelete) return;

        try {

            await deletePatient(id);

            toast.success("Patient Deleted Successfully");

            loadPatients();

        } catch (error) {

            console.error(error);

            toast.error("Unable to delete patient");

        }

    };

    const filteredPatients = patients.filter((patient) => {

        const keyword = search.toLowerCase().trim();

        return (

            patient.name.toLowerCase().includes(keyword) ||

            patient.email.toLowerCase().includes(keyword) ||

            patient.phone.toLowerCase().includes(keyword)

        );

    });

    return (

        <div className="dashboard-container">

            <Navbar />

            <div className="dashboard-body">

                <Sidebar />

                <div className="doctor-container">

                    <div className="doctor-header">

                        <h2>Patient Management</h2>

                        <button
                            className="add-btn"
                            onClick={() => {

                                setSelectedPatient(null);

                                setShowModal(true);

                            }}
                        >
                            Add Patient
                        </button>

                    </div>

                    <div className="search-container">

                        <input
                            type="text"
                            placeholder="🔍 Search by Name, Email or Phone..."
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                            className="search-input"
                        />

                    </div>

                    <table className="doctor-table">

                        <thead>

                            <tr>

                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Age</th>
                                <th>Gender</th>
                                <th>Address</th>
                                <th>Actions</th>

                            </tr>

                        </thead>

                        <tbody>

                            {

                                filteredPatients.length === 0 ?

                                    (

                                        <tr>

                                            <td colSpan="7" className="no-data">

                                                No Patients Found

                                            </td>

                                        </tr>

                                    )

                                    :

                                    filteredPatients.map((patient) => (

                                        <tr key={patient.id}>

                                            <td>{patient.name}</td>
                                            <td>{patient.email}</td>
                                            <td>{patient.phone}</td>
                                            <td>{patient.age}</td>
                                            <td>{patient.gender}</td>
                                            <td>{patient.address}</td>

                                            <td>

                                                <button
                                                    className="edit-btn"
                                                    onClick={() => {

                                                        setSelectedPatient(patient);

                                                        setShowModal(true);

                                                    }}
                                                >
                                                    Edit
                                                </button>

                                                <button
                                                    className="delete-btn"
                                                    onClick={() => handleDelete(patient.id)}
                                                    style={{ marginLeft: "10px" }}
                                                >
                                                    Delete
                                                </button>

                                            </td>

                                        </tr>

                                    ))

                            }

                        </tbody>

                    </table>

                </div>

            </div>

            <AddPatientModal
                show={showModal}
                patient={selectedPatient}
                closeModal={() => {

                    setShowModal(false);

                    setSelectedPatient(null);

                }}
                refreshPatients={loadPatients}
            />

        </div>

    );

}

export default Patients;