package com.doctor_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingRequest {

    private UUID doctorId;

    private LocalDateTime appointmentTime;
}