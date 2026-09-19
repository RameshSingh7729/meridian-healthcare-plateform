package com.doctor_service.controller;

import com.doctor_service.dto.BookingRequest;
import com.doctor_service.dto.DoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.dto.SlotResponse;
import com.doctor_service.entity.Doctor;
import com.doctor_service.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    //Api to create the Doctor
    //http://localhost:8082/api/doctors
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @RequestBody DoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.createDoctor(request)
        );
   }
    //Api to find the doctor based on the id
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                doctorService.getDoctor(id)
        );
    }
    //Api for getting doctors data in page format
    @GetMapping
    public ResponseEntity<Page<DoctorResponse>> getDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(doctorService.getAllDoctors(page, size));
    }
    //Api for searching doctor based on the specialization
    @GetMapping("/search")
    public List<Doctor> searchDoctors(@RequestParam String specialization) {

       return doctorService.searchDoctors(specialization);
    }
    //Api to check slot is available or not
    //http://localhost:8082/api/doctors/{doctorId/slots?date=YYYY-MM-DD
    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<SlotResponse>> getAvailableSlots(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
               doctorService.getAvailableSlots(doctorId, date)
       );
    }

    @GetMapping("/slots/check")
    public ResponseEntity<Boolean> checkSlotAvailability(
            @RequestParam UUID doctorId,
            @RequestParam LocalDateTime appointmentTime) {

        return ResponseEntity.ok(
                doctorService.checkSlotAvailability(doctorId, appointmentTime)
        );
    }
    // API to book appointment slot
    //http://localhost:8082/api/doctors/slots/book
    @PutMapping("/slots/book")
    public ResponseEntity<?> bookAppointment(@RequestBody BookingRequest request) {

       doctorService.bookSlot(request.getDoctorId(), request.getAppointmentTime());

       return ResponseEntity.ok().body(
               Map.of(
                       "message", "Appointment booked successfully",
                       "doctorId", request.getDoctorId(),
                       "appointmentTime", request.getAppointmentTime()
                )
        );
    }

    // API to update doctor
    // http://localhost:8082/api/doctors/{id}

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable UUID id,
            @RequestBody DoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, request)
        );

    }

    // API to delete doctor
    // http://localhost:8082/api/doctors/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(
                Map.of("message", "Doctor deleted successfully")
        );
    }
}
