package com.patient_service.dto;

import lombok.Data;

@Data
public class PatientRequest {

    private String name;

    private String email;

    private String phone;

    private Integer age;

    private String gender;

    private String address;
}