package notification_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private UUID appointmentId;

    @Column(nullable = false)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    public enum NotificationType {
        EMAIL,
        SMS,
        PUSH
    }

    @Column(nullable = false)
    private String subject;


    @Column(length = 5000)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED
    }

    @Column(nullable = false)
    private LocalDateTime sentAt;
}