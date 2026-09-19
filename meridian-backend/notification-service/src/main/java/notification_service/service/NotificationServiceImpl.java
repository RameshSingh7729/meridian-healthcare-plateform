package notification_service.service;

import lombok.RequiredArgsConstructor;
import notification_service.dto.AppointmentBookedEvent;
import notification_service.entity.Notification;
import notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final NotificationRepository repository;

    @Override
    public void processNotification(AppointmentBookedEvent event) {

        try {

            emailService.sendAppointmentConfirmation(event);

            Notification notification = Notification.builder()
                    .appointmentId(event.getAppointmentId())
                    .recipientEmail(event.getPatientEmail())
                    .notificationType(Notification.NotificationType.EMAIL)
                    .subject("Appointment Booked Successfully")
                    .message("Appointment confirmation email sent successfully.")
                    .status(Notification.NotificationStatus.SENT)
                    .sentAt(LocalDateTime.now())
                    .build();

            repository.save(notification);

            System.out.println("Notification saved.");

        } catch (Exception ex) {

            Notification notification = Notification.builder()
                    .appointmentId(event.getAppointmentId())
                    .recipientEmail(event.getPatientEmail())
                    .notificationType(Notification.NotificationType.EMAIL)
                    .subject("Appointment Booked Successfully")
                    .message(ex.getMessage())
                    .status(Notification.NotificationStatus.FAILED)
                    .sentAt(LocalDateTime.now())
                    .build();

            repository.save(notification);

            System.out.println("Email sending failed : " + ex.getMessage());
        }

    }
}