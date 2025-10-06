package app.clinic.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientVisit;
import app.clinic.domain.model.PatientVisitId;
import app.clinic.domain.port.PatientVisitRepository;

/**
 * Domain service for patient visit operations.
 * Contains business logic for patient visit management following domain-driven design principles.
 */
@Service
public class PatientVisitDomainService {

    private final PatientVisitRepository patientVisitRepository;

    public PatientVisitDomainService(PatientVisitRepository patientVisitRepository) {
        this.patientVisitRepository = patientVisitRepository;
    }

    /**
     * Records a new patient visit with validation.
     */
    public PatientVisit recordPatientVisit(PatientVisit patientVisit) {
        validatePatientVisitForRecording(patientVisit);
        return patientVisitRepository.save(patientVisit);
    }

    /**
     * Updates an existing patient visit with validation.
     */
    public PatientVisit updatePatientVisit(PatientVisit patientVisit) {
        validatePatientVisitForUpdate(patientVisit);
        return patientVisitRepository.save(patientVisit);
    }

    /**
     * Completes a patient visit.
     */
    public PatientVisit completePatientVisit(PatientVisitId patientVisitId) {
        Optional<PatientVisit> existingVisit = patientVisitRepository.findById(patientVisitId);
        if (existingVisit.isEmpty()) {
            throw new IllegalArgumentException("Patient visit to complete does not exist");
        }

        PatientVisit completedVisit = PatientVisit.of(
            existingVisit.get().getId(),
            existingVisit.get().getPatientCedula(),
            existingVisit.get().getVisitDateTime(),
            existingVisit.get().getVisitRecord(),
            true
        );

        return patientVisitRepository.save(completedVisit);
    }

    /**
     * Finds a patient visit by ID.
     */
    public Optional<PatientVisit> findPatientVisitById(PatientVisitId patientVisitId) {
        return patientVisitRepository.findById(patientVisitId);
    }

    /**
     * Finds all patient visits for a specific patient.
     */
    public List<PatientVisit> findPatientVisitsByPatient(PatientCedula patientCedula) {
        return patientVisitRepository.findByPatientCedula(patientCedula);
    }

    /**
     * Finds all patient visits.
     */
    public List<PatientVisit> findAllPatientVisits() {
        return patientVisitRepository.findAll();
    }

    /**
     * Finds all completed patient visits.
     */
    public List<PatientVisit> findAllCompletedPatientVisits() {
        return patientVisitRepository.findAllCompleted();
    }

    /**
     * Finds patient visits within a date range.
     */
    public List<PatientVisit> findVisitsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Validate input parameters
        if (startDateTime == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDateTime == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return patientVisitRepository.findByVisitDateTimeBetween(startDateTime, endDateTime);
    }

    /**
     * Deletes a patient visit by ID.
     */
    public void deletePatientVisitById(PatientVisitId patientVisitId) {
        validatePatientVisitCanBeDeleted(patientVisitId);
        patientVisitRepository.deleteById(patientVisitId);
    }

    /**
     * Validates patient visit data for recording.
     */
    private void validatePatientVisitForRecording(PatientVisit patientVisit) {
        if (patientVisit == null) {
            throw new IllegalArgumentException("Patient visit cannot be null");
        }
        if (patientVisit.getPatientCedula() == null) {
            throw new IllegalArgumentException("Patient visit must have a valid patient cedula");
        }
        if (patientVisit.getVisitDateTime() == null) {
            throw new IllegalArgumentException("Patient visit must have a valid visit date and time");
        }
        // Add validation rules for patient visit recording
        // For example, validate that the patient exists, visit time is valid, etc.
    }

    /**
     * Validates patient visit data for update.
     */
    private void validatePatientVisitForUpdate(PatientVisit patientVisit) {
        Optional<PatientVisit> existingVisit = patientVisitRepository.findById(PatientVisitId.of(patientVisit.getId()));
        if (existingVisit.isEmpty()) {
            throw new IllegalArgumentException("Patient visit to update does not exist");
        }
        // Add additional validation rules
    }

    /**
     * Validates that the patient visit can be deleted.
     */
    private void validatePatientVisitCanBeDeleted(PatientVisitId patientVisitId) {
        Optional<PatientVisit> patientVisit = patientVisitRepository.findById(patientVisitId);
        if (patientVisit.isEmpty()) {
            throw new IllegalArgumentException("Patient visit to delete does not exist");
        }
        // Add business rules for patient visit deletion
    }
}