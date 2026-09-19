package com.appointment_booking.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PatientResponse {

    private UUID id;

    private String name;

    private String email;

}
