package payment_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VerifyPaymentRequest {

    private UUID appointmentId;

    private UUID patientId;

    private String currency;

    private String paymentMethod;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpaySignature;
}