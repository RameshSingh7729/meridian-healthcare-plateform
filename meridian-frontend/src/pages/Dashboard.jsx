import "../assets/css/dashboard.css";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import DashboardCard from "../components/DashboardCard";

function Dashboard() {

    return (

        <div className="dashboard-container">

            <Navbar />

            <div className="dashboard-body">

                <Sidebar />

                <div className="dashboard-content">

                    <h1>Welcome to Doctor Appointment System</h1>

                    <div className="card-container">

                        <DashboardCard
                            title="Doctors"
                            value="0"
                        />

                        <DashboardCard
                            title="Patients"
                            value="0"
                        />

                        <DashboardCard
                            title="Appointments"
                            value="0"
                        />

                    </div>

                </div>

            </div>

        </div>

    );

}

export default Dashboard;