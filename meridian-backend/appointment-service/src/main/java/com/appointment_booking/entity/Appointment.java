package com.appointment_booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID doctorId;

    private UUID patientId;

    private LocalDateTime appointmentTime;

    private String status;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private double consultationFee;
}