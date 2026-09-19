package com.patient_service.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private Integer age;

    private String gender;

    private String address;

    private LocalDateTime createdAt;

}