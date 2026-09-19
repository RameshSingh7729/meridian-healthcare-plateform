package com.doctor_service.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse implements Serializable {

    private UUID id;

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

    private LocalDateTime createdAt;
}