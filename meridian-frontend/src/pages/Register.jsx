import "../assets/css/register.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../api/authApi";

function Register() {

    const navigate = useNavigate();

    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState({
        name: "",
        email: "",
        phoneNumber: "",
        password: ""
    });

    const handleChange = (e) => {

        const { name, value } = e.target;

        setFormData({
            ...formData,
            [name]: value
        });

    };

    const handleRegister = async (e) => {

        e.preventDefault();

        // Prevent multiple clicks
        if (loading) return;

        setLoading(true);

        console.log("Register button clicked");
        console.log(formData);

        try {

            const response = await register(formData);

            console.log(response);

            alert(response.data);

            // Remove old token if any
            localStorage.removeItem("token");

            // Clear form
            setFormData({
                name: "",
                email: "",
                phoneNumber: "",
                password: ""
            });

            // Redirect to Login
            navigate("/");

        } catch (error) {

            console.error(error);

            if (error.response) {

                alert(error.response.data);

            } else {

                alert("Registration Failed");

            }

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="register-container">

            <div className="register-card">

                <h1>Doctor Appointment System</h1>

                <h2>Register</h2>

                <form onSubmit={handleRegister}>

                    <div className="form-group">

                        <label>Name</label>

                        <input
                            type="text"
                            name="name"
                            placeholder="Enter Name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                        />

                    </div>

                    <div className="form-group">

                        <label>Email</label>

                        <input
                            type="email"
                            name="email"
                            placeholder="Enter Email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />

                    </div>

                    <div className="form-group">

                        <label>Phone Number</label>

                        <input
                            type="text"
                            name="phoneNumber"
                            placeholder="Enter Phone Number"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            required
                        />

                    </div>

                    <div className="form-group">

                        <label>Password</label>

                        <input
                            type="password"
                            name="password"
                            placeholder="Enter Password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />

                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? "Registering..." : "Register"}
                    </button>

                </form>

                <p>

                    Already have an account?

                    <span
                        className="login-link"
                        onClick={() => navigate("/")}
                    >
                        Login
                    </span>

                </p>

            </div>

        </div>

    );

}

export default Register;