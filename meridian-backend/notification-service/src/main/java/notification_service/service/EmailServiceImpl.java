package notification_service.service;

import lombok.RequiredArgsConstructor;
import notification_service.dto.AppointmentBookedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAppointmentConfirmation(AppointmentBookedEvent event) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("rs4300512@gmail.com");

        message.setTo(event.getPatientEmail());

        message.setSubject("Appointment Booked Successfully");

        message.setText(
                "Dear " + event.getPatientName() + ",\n\n" +

                        "Your appointment has been booked successfully.\n\n" +

                        "Hospital : " + event.getHospitalName() + "\n" +

                        "Specialization : " + event.getSpecialization() + "\n" +

                        "Appointment Time : " + event.getAppointmentTime() + "\n\n" +

                        "Status : " + event.getStatus() + "\n\n" +

                        "Thank you for using our Doctor Appointment System."
        );

        mailSender.send(message);

        System.out.println("Email sent successfully.");
    }
}