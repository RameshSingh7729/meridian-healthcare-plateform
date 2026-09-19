package payment_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderRequest {

    private UUID appointmentId;

    private UUID patientId;
}