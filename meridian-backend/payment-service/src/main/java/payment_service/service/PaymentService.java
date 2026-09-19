package payment_service.service;

import payment_service.dto.*;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponse makePayment(PaymentRequest request);

    PaymentResponse getPayment(UUID paymentId);

    PaymentResponse getPaymentByAppointment(UUID appointmentId);

    List<PaymentResponse> getPaymentsByPatient(UUID patientId);

    CreateOrderResponse createOrder(CreateOrderRequest request) throws Exception;

    PaymentResponse verifyPayment(VerifyPaymentRequest request) throws Exception;
}
