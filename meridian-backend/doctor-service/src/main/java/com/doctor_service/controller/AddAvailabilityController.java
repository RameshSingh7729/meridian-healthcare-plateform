package com.doctor_service.controller;

import com.doctor_service.dto.AvailabilityRequest;
import com.doctor_service.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class AddAvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping("/{doctorId}/availability")
    public ResponseEntity<String> addAvailability(
            @PathVariable UUID doctorId,
            @RequestBody AvailabilityRequest request) {

        availabilityService.addAvailability(doctorId, request);
        return ResponseEntity.ok("Availability added and slots generated");
    }
}