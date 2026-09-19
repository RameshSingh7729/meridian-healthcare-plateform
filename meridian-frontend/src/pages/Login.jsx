import "../assets/css/login.css";
import { useState } from "react";
import { login } from "../api/authApi";
import { useNavigate } from "react-router-dom";
function Login() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    // Form submit handler
   const handleLogin = async (e) => {

    e.preventDefault();

    console.log("Login button clicked");

    try {
        console.log("Calling API...");

        const response = await login(email, password);

        console.log(response);

        localStorage.setItem("token", response.data.token);

        navigate("/dashboard");

    } catch (error) {

    console.error(error);

    if (error.response) {
        alert(error.response.data?.message || "Invalid Email or Password");
    } else {
        alert("Unable to connect to the server.");
    }

}

    
};
    return (
        <div className="login-container">

            <div className="login-card">

                <h1>Doctor Appointment System</h1>

                <h2>Login</h2>

                <form onSubmit={handleLogin}>

                    <div className="form-group">

                        <label>Email</label>

                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />

                    </div>

                    <div className="form-group">

                        <label>Password</label>

                        <input
                            type="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />

                    </div>

                    <button type="submit">
                        Login
                    </button>

                </form>

                <p>
                  Don't have an account?
               <span
               className="register-link"
               onClick={() => navigate("/register")}
              >
               Register
              </span>
               </p>

            </div>

        </div>
    );
}

export default Login;