package com.patient_service.service;

import com.patient_service.dto.PatientRequest;
import com.patient_service.dto.PatientResponse;
import com.patient_service.entity.Patient;
import com.patient_service.exception.ResourceNotFoundException;
import com.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient createPatient(PatientRequest request) {

        Patient patient = Patient.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .age(request.getAge())
                .gender(request.getGender())
                .address(request.getAddress())
                .createdAt(LocalDateTime.now())
                .build();

        return patientRepository.save(patient);
    }

    public Patient getPatient(UUID id) {

        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    private PatientResponse mapToResponse(Patient patient) {

        return PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .age(patient.getAge())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .createdAt(patient.getCreatedAt())
                .build();
    }

    public PatientResponse updatePatient(UUID id, PatientRequest request){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setGender(request.getGender());
        patient.setAge(request.getAge());
        patient.setAddress(request.getAddress());



        Patient updatedPatient = patientRepository.save(patient);
        return  mapToResponse(updatedPatient);
    }

    public boolean existsById(UUID id) {
        return patientRepository.existsById(id);
    }

    public void deletePatient(UUID id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        patientRepository.delete(patient);
    }
    //Method to find the all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}