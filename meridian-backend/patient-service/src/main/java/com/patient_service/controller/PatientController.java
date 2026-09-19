package com.patient_service.controller;


import com.patient_service.dto.PatientRequest;
import com.patient_service.entity.Patient;
import com.patient_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    //Api to create patients
    //http://localhost:8083/api/patients/createPatient
    @PostMapping("/createPatient")
    public ResponseEntity<Patient> createPatient(@RequestBody PatientRequest request) {

        Patient patient = patientService.createPatient(request);

        return ResponseEntity.ok(patient);
    }
    //Api to get Patient by Id
    //http://localhost:8083/api/patients/1
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable UUID id) {

        Patient patient = patientService.getPatient(id);

        return ResponseEntity.ok(patient);
    }
    //http://localhost:8083/api/patients/1/exists
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkPatientExists(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.existsById(id));
    }
    //Api to update the patents record using id
    //http:localhost:8083/api/patients/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient( @PathVariable UUID id,
                                            @RequestBody PatientRequest request) {
        return ResponseEntity.ok(
                patientService.updatePatient(id, request)
        );
    }
    //Api to delete the patient by id
    //http:localhost:8083/api/patients/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(
                Map.of("message", "Patient deleted successfully")
        );
    }
    //Api to get all the patients
    @GetMapping()
    public ResponseEntity<List<Patient>> getAllPatient(){
        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }
}