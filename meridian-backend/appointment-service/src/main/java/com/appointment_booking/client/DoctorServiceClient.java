package com.appointment_booking.client;

import com.appointment_booking.dto.BookSlotRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import com.appointment_booking.dto.DoctorResponse;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DOCTOR-SERVICE")
public interface DoctorServiceClient {

    @GetMapping("/api/doctors/slots/check")
    Boolean checkSlot(
            @RequestParam("doctorId") UUID doctorId,
            @RequestParam("appointmentTime") String appointmentTime
    );


    @PutMapping("/api/doctors/slots/book")
    void bookSlot(@RequestBody BookSlotRequest request);

    @PutMapping("/api/doctors/slots/release")
    void releaseSlot(
            @RequestParam("doctorId") UUID doctorId,
            @RequestParam("appointmentTime") LocalDateTime appointmentTime
    );

    @GetMapping("/api/doctors/{id}")
    DoctorResponse getDoctor(@PathVariable("id") UUID id);
}