import { useNavigate } from "react-router-dom";

function Navbar() {

    const navigate = useNavigate();

    const logout = () => {

        localStorage.removeItem("token");

       navigate("/", { replace: true });

    };

    return (

        <nav className="navbar">

            <h2>Doctor Appointment System</h2>

            <button
                className="logout-btn"
                onClick={logout}
            >
                Logout
            </button>

        </nav>

    );

}

export default Navbar;