package com.doctor_service.service;

import com.doctor_service.dto.AvailabilityRequest;
import com.doctor_service.entity.Doctor;
import com.doctor_service.entity.DoctorAvailability;
import com.doctor_service.entity.DoctorSlot;
import com.doctor_service.entity.SlotStatus;
import com.doctor_service.repository.AvailabilityRepository;
import com.doctor_service.repository.DoctorRepository;
import com.doctor_service.repository.DoctorSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final DoctorRepository doctorRepository;
    private final AvailabilityRepository availabilityRepository;
    private final DoctorSlotRepository doctorSlotRepository;

    @Transactional
    public void addAvailability(UUID doctorId, AvailabilityRequest request) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found with id : " + doctorId));

        validateRequest(request);

        boolean exists = availabilityRepository
                .findByDoctor_IdAndDateAndStartTimeAndEndTime(
                        doctorId,
                        request.getDate(),
                        request.getStartTime(),
                        request.getEndTime()
                )
                .isPresent();

        if (exists) {
            throw new RuntimeException("Availability already exists.");
        }

        DoctorAvailability availability = DoctorAvailability.builder()
                .doctor(doctor)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotDuration(request.getSlotDuration())
                .createdAt(LocalDateTime.now())
                .build();

        DoctorAvailability savedAvailability =
                availabilityRepository.save(availability);

        generateSlots(savedAvailability);
    }

    private void generateSlots(DoctorAvailability availability) {

        LocalTime currentTime = availability.getStartTime();

        LocalTime endTime = availability.getEndTime();

        Integer duration = availability.getSlotDuration();

        List<DoctorSlot> slots = new ArrayList<>();

        while (currentTime.plusMinutes(duration).compareTo(endTime) <= 0) {

            boolean exists = doctorSlotRepository
                    .findByDoctorAvailability_Doctor_IdAndAppointmentDateAndSlotTime(
                            availability.getDoctor().getId(),
                            availability.getDate(),
                            currentTime
                    )
                    .isPresent();

            if (!exists) {

                DoctorSlot slot = new DoctorSlot();

                slot.setDoctorAvailability(availability);

                slot.setAppointmentDate(availability.getDate());

                slot.setSlotTime(currentTime);

                slot.setStatus(SlotStatus.AVAILABLE);

                slots.add(slot);
            }

            currentTime = currentTime.plusMinutes(duration);
        }

        doctorSlotRepository.saveAll(slots);
    }

    private void validateRequest(AvailabilityRequest request) {

        if (request.getDate() == null)
            throw new RuntimeException("Date is required");

        if (request.getStartTime() == null)
            throw new RuntimeException("Start time is required");

        if (request.getEndTime() == null)
            throw new RuntimeException("End time is required");

        if (request.getSlotDuration() == null || request.getSlotDuration() <= 0)
            throw new RuntimeException("Invalid slot duration");

        if (!request.getStartTime().isBefore(request.getEndTime()))
            throw new RuntimeException("Start time must be before End time");

        long totalMinutes =
                java.time.Duration.between(
                                request.getStartTime(),
                                request.getEndTime())
                        .toMinutes();

        if (request.getSlotDuration() > totalMinutes)
            throw new RuntimeException("Slot duration exceeds availability.");
    }
}