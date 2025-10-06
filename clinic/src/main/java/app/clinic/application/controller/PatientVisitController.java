package app.clinic.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.visit.CreatePatientVisitDTO;
import app.clinic.application.dto.visit.PatientVisitDTO;
import app.clinic.application.dto.visit.VitalSignsDTO;
import app.clinic.application.service.PatientVisitApplicationService;
import jakarta.validation.Valid;

/**
 * REST Controller for patient visit management operations.
 * Provides HTTP endpoints for managing patient visits and vital signs.
 * Accessible by nurses and doctors.
 */
@RestController
@RequestMapping("/api/patient-visits")
public class PatientVisitController {

    private final PatientVisitApplicationService patientVisitApplicationService;

    public PatientVisitController(PatientVisitApplicationService patientVisitApplicationService) {
        this.patientVisitApplicationService = patientVisitApplicationService;
    }

    /**
     * Creates a new patient visit record.
     * Accessible by nurses and doctors.
     */
    @PostMapping
    public ResponseEntity<PatientVisitDTO> createPatientVisit(@Valid @RequestBody CreatePatientVisitDTO createPatientVisitDTO) {
        PatientVisitDTO createdVisit = patientVisitApplicationService.createPatientVisit(createPatientVisitDTO);
        return new ResponseEntity<>(createdVisit, HttpStatus.CREATED);
    }

    /**
     * Records vital signs for a patient visit.
     * Accessible by nurses.
     */
    @PutMapping("/{visitId}/vital-signs")
    public ResponseEntity<VitalSignsDTO> recordVitalSigns(@PathVariable String visitId,
                                                         @Valid @RequestBody VitalSignsDTO vitalSignsDTO) {
        VitalSignsDTO recordedSigns = patientVisitApplicationService.recordVitalSigns(visitId, vitalSignsDTO);
        return ResponseEntity.ok(recordedSigns);
    }

    /**
     * Finds a patient visit by ID.
     */
    @GetMapping("/{visitId}")
    public ResponseEntity<PatientVisitDTO> findPatientVisitById(@PathVariable String visitId) {
        Optional<PatientVisitDTO> visit = patientVisitApplicationService.findPatientVisitById(visitId);
        return visit.map(v -> ResponseEntity.ok(v))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds all visits for a specific patient.
     */
    @GetMapping("/patient/{patientCedula}")
    public ResponseEntity<List<PatientVisitDTO>> findVisitsByPatientCedula(@PathVariable String patientCedula) {
        List<PatientVisitDTO> visits = patientVisitApplicationService.findVisitsByPatientCedula(patientCedula);
        return ResponseEntity.ok(visits);
    }

    /**
     * Finds all visits within a date range.
     */
    @GetMapping("/daterange/{startDate}/{endDate}")
    public ResponseEntity<List<PatientVisitDTO>> findVisitsByDateRange(@PathVariable String startDate,
                                                                      @PathVariable String endDate) {
        List<PatientVisitDTO> visits = patientVisitApplicationService.findVisitsByDateRange(startDate, endDate);
        return ResponseEntity.ok(visits);
    }

    /**
     * Updates a patient visit record.
     * Accessible by nurses and doctors.
     */
    @PutMapping("/{visitId}")
    public ResponseEntity<PatientVisitDTO> updatePatientVisit(@PathVariable String visitId,
                                                             @Valid @RequestBody CreatePatientVisitDTO updateDTO) {
        PatientVisitDTO updatedVisit = patientVisitApplicationService.updatePatientVisit(visitId, updateDTO);
        return ResponseEntity.ok(updatedVisit);
    }

    /**
     * Marks a patient visit as completed.
     * Accessible by nurses and doctors.
     */
    @PutMapping("/{visitId}/complete")
    public ResponseEntity<PatientVisitDTO> completePatientVisit(@PathVariable String visitId) {
        PatientVisitDTO completedVisit = patientVisitApplicationService.completePatientVisit(visitId);
        return ResponseEntity.ok(completedVisit);
    }

    /**
     * Validates vital signs data.
     */
    @PostMapping("/vital-signs/validate")
    public ResponseEntity<Boolean> validateVitalSigns(@Valid @RequestBody VitalSignsDTO vitalSignsDTO) {
        boolean isValid = patientVisitApplicationService.validateVitalSigns(vitalSignsDTO);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Gets visit statistics for a patient.
     */
    @GetMapping("/patient/{patientCedula}/statistics")
    public ResponseEntity<PatientVisitApplicationService.VisitStatisticsDTO> getVisitStatistics(@PathVariable String patientCedula) {
        PatientVisitApplicationService.VisitStatisticsDTO statistics = patientVisitApplicationService.getVisitStatistics(patientCedula);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Checks if a patient has any pending visits.
     */
    @GetMapping("/patient/{patientCedula}/pending")
    public ResponseEntity<Boolean> hasPendingVisits(@PathVariable String patientCedula) {
        boolean hasPending = patientVisitApplicationService.hasPendingVisits(patientCedula);
        return ResponseEntity.ok(hasPending);
    }
}