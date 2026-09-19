package com.doctor_service.repository;

import com.doctor_service.entity.DoctorSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Long> {

    List<DoctorSlot> findByDoctorAvailability_Doctor_IdAndAppointmentDate(
            UUID doctorId,
            LocalDate appointmentDate
    );

    Optional<DoctorSlot> findByDoctorAvailability_Doctor_IdAndAppointmentDateAndSlotTime(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime slotTime
    );

    List<DoctorSlot> findByDoctorAvailability_Id(Long availabilityId);
}