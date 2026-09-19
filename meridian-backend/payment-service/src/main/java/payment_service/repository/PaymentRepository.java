package payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment_service.entity.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByAppointmentId(UUID appointmentId);

    List<Payment> findByPatientId(UUID patientId);
}
