package payment_service.dto;



import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentResponse {

    private UUID id;

    private UUID doctorId;

    private UUID patientId;

    private double consultationFee;

    private LocalDateTime appointmentTime;

    private String status;

    private LocalDateTime createdAt;
}
