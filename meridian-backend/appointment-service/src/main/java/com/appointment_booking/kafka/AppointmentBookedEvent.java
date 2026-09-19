package com.appointment_booking.kafka;

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

    // NEW FIELDS
    private String patientName;
    private String patientEmail;

    private String hospitalName;
    private String specialization;

    private LocalDateTime appointmentTime;
    private String status;
    private LocalDateTime createdAt;
}