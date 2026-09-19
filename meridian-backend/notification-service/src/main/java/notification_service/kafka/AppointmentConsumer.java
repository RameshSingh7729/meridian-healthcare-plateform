package notification_service.kafka;

import lombok.RequiredArgsConstructor;
import notification_service.dto.AppointmentBookedEvent;
import notification_service.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "appointment-booked-topic",
            groupId = "notification-group"
    )
    public void consume(AppointmentBookedEvent event) {

        System.out.println("==================================");
        System.out.println("Kafka Event Received");
        System.out.println(event);
        System.out.println("==================================");

        notificationService.processNotification(event);

    }
}