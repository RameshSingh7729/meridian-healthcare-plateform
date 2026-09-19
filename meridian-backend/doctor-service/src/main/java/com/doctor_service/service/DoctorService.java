package com.doctor_service.service;

import com.doctor_service.dto.DoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.dto.SlotCheckResponse;
import com.doctor_service.dto.SlotResponse;
import com.doctor_service.entity.Doctor;
import com.doctor_service.entity.DoctorSlot;
import com.doctor_service.entity.SlotStatus;
import com.doctor_service.exception.ResourceNotFoundException;
import com.doctor_service.repository.DoctorRepository;
import com.doctor_service.repository.DoctorSlotRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorSlotRepository doctorSlotRepository;


    @CacheEvict(value = "doctors", allEntries = true)
    public DoctorResponse createDoctor(DoctorRequest request) {

        Doctor doctor = Doctor.builder()
                .userId(request.getUserId())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .hospitalName(request.getHospitalName())
                .addressLine(request.getAddressLine())
                .area(request.getArea())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .pincode(request.getPincode())
                .createdAt(LocalDateTime.now())
                .build();

        Doctor saved = doctorRepository.save(doctor);

        return mapToResponse(saved);
    }

    @CachePut(value = "doctors", key = "#id")
    @CacheEvict(value = "doctors", allEntries = true, beforeInvocation = false)
    public DoctorResponse updateDoctor(UUID id, DoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        doctor.setUserId(request.getUserId());
        doctor.setHospitalName(request.getHospitalName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setAddressLine(request.getAddressLine());
        doctor.setArea(request.getArea());
        doctor.setCity(request.getCity());
        doctor.setState(request.getState());
        doctor.setCountry(request.getCountry());
        doctor.setPincode(request.getPincode());

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return mapToResponse(updatedDoctor);
    }
    @Caching(evict = {
            @CacheEvict(value = "doctorById", key = "#id"),
            @CacheEvict(value = "doctorSearch", allEntries = true)
    })
    public void deleteDoctor(UUID id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        doctorRepository.delete(doctor);
    }

    @Cacheable(value = "doctors", key = "#id")
    public DoctorResponse getDoctor(UUID id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return mapToResponse(doctor);
    }
    public Page<DoctorResponse> getAllDoctors(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Doctor> doctors = doctorRepository.findAll(pageable);

        return doctors.map(this::mapToResponse);
    }

    private DoctorResponse mapToResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .hospitalName(doctor.getHospitalName())
                .addressLine(doctor.getAddressLine())
                .area(doctor.getArea())
                .city(doctor.getCity())
                .state(doctor.getState())
                .country(doctor.getCountry())
                .pincode(doctor.getPincode())
                .createdAt(doctor.getCreatedAt())
                .build();
    }
    @Cacheable(value = "doctors", key = "#specialization")
    public List<Doctor> searchDoctors(String specialization) {

        System.out.println("Fetching from DB...");

        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
    }
    //logic to get available slots
    public List<SlotResponse> getAvailableSlots(UUID doctorId, LocalDate date) {

        List<DoctorSlot> slots =
               doctorSlotRepository.findByDoctorAvailability_Doctor_IdAndAppointmentDate(doctorId, date);

       return slots.stream()
                .filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE)
                .map(slot -> new SlotResponse(
                        slot.getId(),
                        slot.getAppointmentDate(),
                        slot.getSlotTime(),
                        slot.getStatus().name()
                ))
                .toList();
    }
   //Logic to check specific slot is available or not if yes return ture if no return false
   public boolean checkSlotAvailability(UUID doctorId, LocalDateTime appointmentTime) {

       DoctorSlot slot = doctorSlotRepository
               .findByDoctorAvailability_Doctor_IdAndAppointmentDateAndSlotTime(
                       doctorId,
                       appointmentTime.toLocalDate(),
                       appointmentTime.toLocalTime()
               )
               .orElseThrow(() -> new RuntimeException("Slot not found"));

       return slot.getStatus() == SlotStatus.AVAILABLE;
   }

    //Logic to book slot
    public void bookSlot(UUID doctorId, LocalDateTime appointmentTime) {
        LocalDate appointmentDate = appointmentTime.toLocalDate();
        LocalTime slotTime = appointmentTime.toLocalTime();

        DoctorSlot slot = doctorSlotRepository
                .findByDoctorAvailability_Doctor_IdAndAppointmentDateAndSlotTime(doctorId, appointmentDate, slotTime)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() == SlotStatus.BOOKED) {
            throw new RuntimeException("Slot already booked");
        }

        slot.setStatus(SlotStatus.BOOKED);
        doctorSlotRepository.save(slot);
    }

}