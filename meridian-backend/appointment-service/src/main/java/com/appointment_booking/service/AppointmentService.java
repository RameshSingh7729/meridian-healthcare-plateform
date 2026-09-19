package com.appointment_booking.service;

import com.appointment_booking.client.DoctorServiceClient;
import com.appointment_booking.client.PatientServiceClient;
import com.appointment_booking.dto.*;
import com.appointment_booking.entity.Appointment;
import com.appointment_booking.kafka.AppointmentBookedEvent;
import com.appointment_booking.kafka.AppointmentEventProducer;
import com.appointment_booking.repository.AppointmentRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientServiceClient patientClient;
    private final DoctorServiceClient doctorClient;
    private final AppointmentEventProducer eventProducer;

    // ======================================================
    // BOOK APPOINTMENT
    // ======================================================
    @Transactional
    @CircuitBreaker(name = "doctorService", fallbackMethod = "bookAppointmentFallback")
    @Retry(name = "doctorRetry")
    @RateLimiter(name = "doctorRateLimiter", fallbackMethod = "bookAppointmentFallback")
    public AppointmentResponse bookAppointment(AppointmentRequest request) {

        Boolean patientExists = patientClient.checkPatient(request.getPatientId());

        if (patientExists == null || !patientExists) {
            throw new RuntimeException("Patient not found");
        }

        Boolean slotAvailable = doctorClient.checkSlot(
                request.getDoctorId(),
                request.getAppointmentTime().toString());

        if (slotAvailable == null || !slotAvailable) {
            throw new RuntimeException("Doctor slot not available");
        }
        DoctorResponse doctor = doctorClient.getDoctor(request.getDoctorId());
        Appointment appointment = Appointment.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .consultationFee(doctor.getConsultationFee())
                .appointmentTime(request.getAppointmentTime())
                .status("PENDING_PAYMENT")
                .createdAt(LocalDateTime.now())
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    // ======================================================
    // CONFIRM APPOINTMENT AFTER PAYMENT
    // ======================================================

    @Transactional
    public AppointmentResponse confirmAppointment(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getStatus().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("Appointment is already processed.");
        }

        doctorClient.bookSlot(
                new BookSlotRequest(
                        appointment.getDoctorId(),
                        appointment.getAppointmentTime()
                )
        );

        appointment.setStatus("BOOKED");

        Appointment savedAppointment = appointmentRepository.save(appointment);

        PatientResponse patient =
                patientClient.getPatient(savedAppointment.getPatientId());

        DoctorResponse doctor =
                doctorClient.getDoctor(savedAppointment.getDoctorId());

        AppointmentBookedEvent event = AppointmentBookedEvent.builder()
                .appointmentId(savedAppointment.getId())
                .doctorId(savedAppointment.getDoctorId())
                .patientId(savedAppointment.getPatientId())

                .patientName(patient.getName())
                .patientEmail(patient.getEmail())

                .hospitalName(doctor.getHospitalName())
                .specialization(doctor.getSpecialization())

                .appointmentTime(savedAppointment.getAppointmentTime())
                .status(savedAppointment.getStatus())
                .createdAt(savedAppointment.getCreatedAt())
                .build();

        eventProducer.publishAppointmentBooked(event);

        return mapToResponse(savedAppointment);
    }

    // ======================================================
    // GET APPOINTMENT BY ID
    // ======================================================

    public AppointmentResponse getAppointment(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        return mapToResponse(appointment);
    }

    // ======================================================
    // ALL APPOINTMENTS
    // ======================================================

    public List<AppointmentHistoryResponse> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(this::buildAppointmentHistory)
                .toList();
    }

    // ======================================================
    // HISTORY RESPONSE
    // ======================================================

    private AppointmentHistoryResponse buildAppointmentHistory(Appointment appointment) {

        DoctorResponse doctor =
                doctorClient.getDoctor(appointment.getDoctorId());

        PatientResponse patient =
                patientClient.getPatient(appointment.getPatientId());

        return AppointmentHistoryResponse.builder()
                .id(appointment.getId())

                .doctorId(appointment.getDoctorId())

                .patientId(appointment.getPatientId())

                .hospitalName(doctor.getHospitalName())
                .consultationFee(appointment.getConsultationFee())
                .specialization(doctor.getSpecialization())

                .patientName(patient.getName())
                .patientEmail(patient.getEmail())

                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .build();
    }

    // ======================================================
    // DTO MAPPER
    // ======================================================

    private AppointmentResponse mapToResponse(Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .consultationFee(appointment.getConsultationFee())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .build();
    }


    private AppointmentResponse bookAppointmentFallback(
            AppointmentRequest request,
            Exception ex) {

        throw new RuntimeException(
                "Doctor Service is temporarily unavailable. Please try again later.");
    }
}