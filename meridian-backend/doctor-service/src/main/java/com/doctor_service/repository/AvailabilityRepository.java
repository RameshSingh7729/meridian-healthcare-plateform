package com.doctor_service.repository;

import com.doctor_service.entity.Doctor;
import com.doctor_service.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctor(Doctor doctor);

    List<DoctorAvailability> findByDoctorAndDate(
            Doctor doctor,
            LocalDate date
    );

    boolean existsByDoctorAndDate(
            Doctor doctor,
            LocalDate date
    );

    Optional<DoctorAvailability> findByDoctor_IdAndDateAndStartTimeAndEndTime(
            UUID doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );
}