package com.appointment_booking.controller;

import com.appointment_booking.dto.AppointmentHistoryResponse;
import com.appointment_booking.dto.AppointmentRequest;
import com.appointment_booking.dto.AppointmentResponse;
import com.appointment_booking.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    //http://localahost:8084/api/appointments/book
    @PostMapping("/book")
    public AppointmentResponse bookAppointment(
            @RequestBody AppointmentRequest request) {

        return appointmentService.bookAppointment(request);
    }
    //Method to load all booked Appointments
    //http://localahost:8084/api/appointments
    @GetMapping
    public ResponseEntity<List<AppointmentHistoryResponse>> getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointment(
            @PathVariable UUID appointmentId) {

        return ResponseEntity.ok(
                appointmentService.getAppointment(appointmentId)
        );
    }

    @PutMapping("/{appointmentId}/confirm")
    public ResponseEntity<AppointmentResponse> confirmAppointment(
            @PathVariable UUID appointmentId) {

        return ResponseEntity.ok(
                appointmentService.confirmAppointment(appointmentId)
        );
    }
}

