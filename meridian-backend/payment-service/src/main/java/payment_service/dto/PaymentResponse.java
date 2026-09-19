package payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private UUID paymentId;

    private UUID patientId;

    private UUID appointmentId;

    private String transactionId;

    private Double amount;

    private String paymentStatus;

    private LocalDateTime paymentTime;
}
