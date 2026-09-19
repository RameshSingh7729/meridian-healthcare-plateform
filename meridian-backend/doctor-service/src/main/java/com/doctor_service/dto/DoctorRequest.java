package com.doctor_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DoctorRequest {

    private UUID userId;

    private String specialization;

    private Integer experienceYears;

    private Double consultationFee;

    private String hospitalName;

    private String addressLine;

    private String area;

    private String city;

    private String state;

    private String country;

    private String pincode;

    private String image;
}