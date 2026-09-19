import { useNavigate } from "react-router-dom";

function Sidebar() {

    const navigate = useNavigate();

    return (

        <div className="sidebar">

            <ul>

                <li onClick={() => navigate("/dashboard")}>

                    Dashboard

                </li>

                <li
                     onClick={() => {
                            alert("Doctors clicked");
                                navigate("/doctors");
                              }}
                            >
                            Doctors
                    </li>

                <li
                    onClick={() => navigate("/patients")}
                        >
                            Patients
                    </li>

               <li onClick={() => navigate("/appointments")}>
                 Appointments
                </li>

            </ul>

        </div>

    );

}

export default Sidebar;