package notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentBookedEvent {

    private UUID appointmentId;
    private UUID doctorId;
    private UUID patientId;

    // Patient Details
    private String patientName;
    private String patientEmail;

    // Doctor Details
    private String hospitalName;
    private String specialization;

    // Appointment Details
    private LocalDateTime appointmentTime;
    private String status;
    private LocalDateTime createdAt;
}