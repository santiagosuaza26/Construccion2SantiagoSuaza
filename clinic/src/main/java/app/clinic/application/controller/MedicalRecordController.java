package app.clinic.application.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.medical.CreateMedicalRecordDTO;
import app.clinic.application.dto.medical.MedicalRecordDTO;
import app.clinic.application.dto.medical.MedicalRecordEntryDTO;
import app.clinic.application.service.MedicalRecordApplicationService;
import jakarta.validation.Valid;

/**
 * REST Controller for medical record management operations.
 * Provides HTTP endpoints for creating, reading, and managing medical records.
 * Only accessible by doctors and authorized medical staff.
 */
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordApplicationService medicalRecordApplicationService;

    public MedicalRecordController(MedicalRecordApplicationService medicalRecordApplicationService) {
        this.medicalRecordApplicationService = medicalRecordApplicationService;
    }

    /**
     * Creates a new medical record entry for a patient.
     * Only accessible by doctors.
     */
    @PostMapping
    public ResponseEntity<MedicalRecordEntryDTO> createMedicalRecord(@Valid @RequestBody CreateMedicalRecordDTO createMedicalRecordDTO) {
        MedicalRecordEntryDTO createdRecord = medicalRecordApplicationService.createMedicalRecord(createMedicalRecordDTO);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    /**
     * Finds medical record for a specific patient.
     */
    @GetMapping("/patient/{patientCedula}")
    public ResponseEntity<MedicalRecordDTO> findMedicalRecordByPatientCedula(@PathVariable String patientCedula) {
        Optional<MedicalRecordDTO> medicalRecord = medicalRecordApplicationService.findMedicalRecordByPatientCedula(patientCedula);
        return medicalRecord.map(record -> ResponseEntity.ok(record))
                          .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds a specific medical record entry by patient and date.
     */
    @GetMapping("/patient/{patientCedula}/date/{recordDate}")
    public ResponseEntity<MedicalRecordEntryDTO> findMedicalRecordEntry(@PathVariable String patientCedula,
                                                                        @PathVariable String recordDate) {
        Optional<MedicalRecordEntryDTO> entry = medicalRecordApplicationService.findMedicalRecordEntry(patientCedula, recordDate);
        return entry.map(record -> ResponseEntity.ok(record))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Checks if a patient has any medical records.
     */
    @GetMapping("/patient/{patientCedula}/exists")
    public ResponseEntity<Boolean> hasMedicalRecords(@PathVariable String patientCedula) {
        boolean hasRecords = medicalRecordApplicationService.hasMedicalRecords(patientCedula);
        return ResponseEntity.ok(hasRecords);
    }

    /**
     * Gets the number of medical record entries for a patient.
     */
    @GetMapping("/patient/{patientCedula}/count")
    public ResponseEntity<Integer> getMedicalRecordCount(@PathVariable String patientCedula) {
        int count = medicalRecordApplicationService.getMedicalRecordCount(patientCedula);
        return ResponseEntity.ok(count);
    }
}