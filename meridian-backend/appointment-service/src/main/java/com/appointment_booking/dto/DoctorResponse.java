package com.appointment_booking.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DoctorResponse {

    private UUID id;

    private String doctorName;

    private String hospitalName;

    private String specialization;

    private double  consultationFee;
}
