package payment_service.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_service.client.AppointmentServiceClient;
import payment_service.dto.*;
import payment_service.entity.Payment;
import payment_service.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentServiceClient appointmentClient;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key-id}")
    private String razorpayKey;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    // =====================================================
    // CREATE RAZORPAY ORDER
    // =====================================================

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) throws Exception {

        AppointmentResponse appointment =
                appointmentClient.getAppointment(request.getAppointmentId());

        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        if (!appointment.getStatus().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("Appointment is already processed.");
        }

        JSONObject orderRequest = new JSONObject();

        double amount = appointment.getConsultationFee();

        orderRequest.put("amount", (int)(amount * 100));
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "APT-" + request.getAppointmentId());

        Order order = razorpayClient.orders.create(orderRequest);

        return CreateOrderResponse.builder()
                .orderId(order.get("id"))
                .amount(order.get("amount"))
                .currency(order.get("currency"))
                .key(razorpayKey)
                .build();
    }

    // =====================================================
    // VERIFY PAYMENT
    // =====================================================

    @Override
    @Transactional
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) throws Exception {

        AppointmentResponse appointment =
                appointmentClient.getAppointment(request.getAppointmentId());

        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        if (!appointment.getStatus().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("Appointment is already processed.");
        }

        paymentRepository.findByAppointmentId(request.getAppointmentId())
                .ifPresent(payment -> {
                    throw new RuntimeException("Payment already exists.");
                });

        String payload = request.getRazorpayOrderId()
                + "|"
                + request.getRazorpayPaymentId();

        boolean valid = Utils.verifySignature(
                payload,
                request.getRazorpaySignature(),
                razorpaySecret
        );

        if (!valid) {
            throw new RuntimeException("Invalid Razorpay Signature");
        }

        String transactionId =
                "TXN-" + UUID.randomUUID().toString().replace("-", "");

        Payment payment = Payment.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .amount(appointment.getConsultationFee())
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .transactionId(transactionId)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .paymentTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .razorpayOrderId(request.getRazorpayOrderId())
                .razorpayPaymentId(request.getRazorpayPaymentId())
                .razorpaySignature(request.getRazorpaySignature())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        appointmentClient.confirmAppointment(request.getAppointmentId());

        return mapToResponse(savedPayment);
    }

    // =====================================================
    // LEGACY PAYMENT API
    // =====================================================

    @Override
    @Transactional
    public PaymentResponse makePayment(PaymentRequest request) {

        AppointmentResponse appointment =
                appointmentClient.getAppointment(request.getAppointmentId());

        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        if (!appointment.getStatus().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("Appointment is already processed.");
        }

        paymentRepository.findByAppointmentId(request.getAppointmentId())
                .ifPresent(payment -> {
                    throw new RuntimeException("Payment already exists.");
                });

        String transactionId =
                "TXN-" + UUID.randomUUID().toString().replace("-", "");

        Payment payment = Payment.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .transactionId(transactionId)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .paymentTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        appointmentClient.confirmAppointment(request.getAppointmentId());

        return mapToResponse(savedPayment);
    }

    // =====================================================
    // GET PAYMENT
    // =====================================================

    @Override
    public PaymentResponse getPayment(UUID paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return mapToResponse(payment);
    }

    // =====================================================
    // GET PAYMENT BY APPOINTMENT
    // =====================================================

    @Override
    public PaymentResponse getPaymentByAppointment(UUID appointmentId) {

        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return mapToResponse(payment);
    }

    // =====================================================
    // GET PAYMENTS BY PATIENT
    // =====================================================

    @Override
    public List<PaymentResponse> getPaymentsByPatient(UUID patientId) {

        return paymentRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // DTO MAPPER
    // =====================================================

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .appointmentId(payment.getAppointmentId())
                .patientId(payment.getPatientId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .paymentStatus(payment.getPaymentStatus().name())
                .paymentTime(payment.getPaymentTime())
                .build();
    }
}