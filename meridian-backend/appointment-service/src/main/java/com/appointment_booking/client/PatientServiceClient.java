package com.appointment_booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.appointment_booking.dto.PatientResponse;

import java.util.UUID;

@FeignClient(name = "PATIENT-SERVICE")
public interface PatientServiceClient {

    @GetMapping("/api/patients/{id}/exists")
    Boolean checkPatient(@PathVariable("id") UUID id);

    @GetMapping("/api/patients/{id}")
    PatientResponse getPatient(@PathVariable("id") UUID id);
}