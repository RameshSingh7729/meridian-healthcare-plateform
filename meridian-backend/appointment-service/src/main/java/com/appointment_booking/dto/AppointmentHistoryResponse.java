package com.appointment_booking.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentHistoryResponse {

    private UUID id;

    private UUID doctorId;

    private UUID patientId;

    private String patientName;

    private String patientEmail;

    private String hospitalName;

    private double consultationFee;

    private String specialization;

    private LocalDateTime appointmentTime;

    private String status;

    private LocalDateTime createdAt;

}