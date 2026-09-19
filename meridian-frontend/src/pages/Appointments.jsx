import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import {getPatients,getDoctors,getDoctorSlots,bookAppointment , getAppointments} from "../api/appointmentApi";
import {createOrder,verifyPayment} from "../api/paymentApi";

import { toast } from "react-toastify";
import "../assets/css/appointment.css";

function Appointments() {

    const today = new Date().toISOString().split("T")[0];

    const [patients, setPatients] = useState([]);
    const [doctors, setDoctors] = useState([]);
    const [slots, setSlots] = useState([]);

    const [patientId, setPatientId] = useState("");
    const [doctorId, setDoctorId] = useState("");
    const [date, setDate] = useState(today);
    const [selectedSlot, setSelectedSlot] = useState("");
    const [appointments, setAppointments] = useState([]);

    useEffect(() => {

        loadPatients();
        loadDoctors();
        loadAppointments();

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

    const loadDoctors = async () => {

        try {

            const response = await getDoctors();

            setDoctors(response.data.content);

        } catch (error) {

            console.error(error);

            toast.error("Unable to load doctors");

        }

    };

    const loadSlots = async () => {

        if (!doctorId) {

            toast.warning("Please select a doctor");

            return;

        }

        try {

            const response = await getDoctorSlots(doctorId, date);

            setSlots(response.data);

        } catch (error) {

            console.error(error);

            toast.error("Unable to load slots");

        }

    };

    const loadAppointments = async () => {

    try {

        const response = await getAppointments();

        setAppointments(response.data);

    } catch (error) {

        console.log(error);

    }

};

    const handleBookAppointment = async () => {

    if (!patientId) {
        toast.error("Please select a patient");
        return;
    }

    if (!doctorId) {
        toast.error("Please select a doctor");
        return;
    }

    if (!selectedSlot) {
        toast.error("Please select a slot");
        return;
    }

    try {

        const appointment = {
            doctorId: doctorId,
            patientId: patientId,
            appointmentTime: `${date}T${selectedSlot}`
        };

        console.log("Booking Request:", appointment);

        const response = await bookAppointment(appointment);

        console.log("Booking Response:", response.data);

        toast.success("Appointment booked successfully!");

        // Refresh available slots
        await loadSlots();

        // Clear selected slot
        setSelectedSlot("");

        await loadAppointments();

       } catch (error) {

        console.error(error);

        if (error.response) {
            toast.error(error.response.data.message || "Booking failed");
        } else {
            toast.error("Unable to connect to server");
        }

    }

    };
  const handlePayment = async (appointment) => {

    try {

        // Create Razorpay Order
        const orderResponse = await createOrder({

            appointmentId: appointment.id,
            patientId: appointment.patientId,

        });

        const order = orderResponse.data;

        const options = {

            key: order.key,

            amount: order.amount,

            currency: order.currency,

            name: "Doctor Appointment System",

            description: "Appointment Payment",

            order_id: order.orderId,

            handler: async function (response) {

                try {

                    await verifyPayment({

                        appointmentId: appointment.id,

                        patientId: appointment.patientId,

                        currency: "INR",

                        paymentMethod: "ONLINE",

                        razorpayOrderId: response.razorpay_order_id,

                        razorpayPaymentId: response.razorpay_payment_id,

                        razorpaySignature: response.razorpay_signature

                    });

                    toast.success("Payment Successful");

                    await loadAppointments();

                } catch (error) {

                    console.log(error);

                    toast.error(
                        error.response?.data?.message ||
                        "Payment Verification Failed"
                    );
                }

            },

            prefill: {

                name: appointment.patientName,

                email: appointment.patientEmail

            },

            theme: {

                color: "#3399cc"

            }

        };

        const razorpay = new window.Razorpay(options);

        razorpay.open();

    } catch (error) {

        console.log(error);

        toast.error(
            error.response?.data?.message ||
            "Unable to create payment order"
        );

    }

};

    return (

        <div className="dashboard-container">

            <Navbar />

            <div className="dashboard-body">

                <Sidebar />

                <div className="appointment-container">

                    <h2>Appointment Management</h2>

                    <div className="appointment-form">

                        <select
                            value={patientId}
                            onChange={(e) => setPatientId(e.target.value)}
                        >
                            <option value="">Select Patient</option>

                            {

                                patients.map(patient => (

                                    <option
                                        key={patient.id}
                                        value={patient.id}
                                    >
                                        {patient.name}
                                    </option>

                                ))

                            }

                        </select>

                        <select
                            value={doctorId}
                            onChange={(e) => setDoctorId(e.target.value)}
                        >
                            <option value="">Select Doctor</option>

                            {

                                doctors.map(doctor => (

                                    <option
                                        key={doctor.id}
                                        value={doctor.id}
                                    >
                                        {doctor.hospitalName} ({doctor.specialization})
                                    </option>

                                ))

                            }

                        </select>

                        <input
                            type="date"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
                        />

                        <button onClick={loadSlots}>
                            Load Slots
                        </button>

                    </div>

                   <div className="slot-grid">

    {slots.map((slot) => (

        <button
            key={slot.slotId}
            className={
                selectedSlot === slot.slotTime
                    ? "slot selected"
                    : "slot"
            }
            onClick={() => setSelectedSlot(slot.slotTime)}
        >
            {slot.slotTime}
        </button>

    ))}

</div>
{selectedSlot && (

    <div className="selected-slot">

        <strong>Selected Slot :</strong> {selectedSlot}

    </div>

)}

                    <button className="book-btn" onClick={handleBookAppointment}>
                        Book Appointment
                    </button>


<h2>Appointment History</h2>

<table className="appointment-table">

    <thead>

    <tr>
    <th>Patient Name</th>
    <th>Patient Email</th>
    <th>Hospital</th>
    <th>Consultation Fees</th>
    <th>Specialization</th>
    <th>Appointment Time</th>
    <th>Status</th>
    <th>Action</th>
</tr>

    </thead>

    <tbody>

{appointments.map((appointment) => (

<tr key={appointment.id}>

    <td>{appointment.patientName}</td>

    <td>{appointment.patientEmail}</td>

    <td>{appointment.hospitalName}</td>

    <td>₹{appointment.consultationFee}</td>

    <td>{appointment.specialization}</td>

    <td>
       {new Date(appointment.appointmentTime)
                 .toLocaleString("en-IN", {dateStyle: "medium",timeStyle: "short"})}
    </td>

    <td>{appointment.status}</td>

<td>

{appointment.status === "PENDING_PAYMENT" ?
(<button className="book-btn" onClick={() => handlePayment(appointment)}>Pay Now</button>)

:(<span>Paid</span>)}

</td>

</tr>

))}

</tbody>

</table>
                </div>

            </div>

        </div>

    );

}

export default Appointments;