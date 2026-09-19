package com.appointment_booking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentRequest {

    private UUID doctorId;
    private UUID patientId;
    private LocalDateTime appointmentTime;
}