package com.appointment_booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AppointmentResponse {

    private UUID id;
    private UUID doctorId;
    private UUID patientId;
    private double consultationFee;
    private LocalDateTime appointmentTime;
    private String status;
    private LocalDateTime createdAt;

}