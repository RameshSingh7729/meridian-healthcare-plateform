package payment_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {

    private UUID appointmentId;

    private UUID patientId;

    private Double amount;

    private String currency;

    private String paymentMethod;
}
