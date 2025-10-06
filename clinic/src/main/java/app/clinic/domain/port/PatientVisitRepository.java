package app.clinic.domain.port;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientVisit;
import app.clinic.domain.model.PatientVisitId;

/**
 * Port interface for patient visit repository operations.
 * Defines the contract for patient visit data access in the domain layer.
 */
public interface PatientVisitRepository {

    /**
     * Saves a new patient visit or updates an existing one.
     */
    PatientVisit save(PatientVisit patientVisit);

    /**
     * Finds a patient visit by its unique identifier.
     */
    Optional<PatientVisit> findById(PatientVisitId patientVisitId);

    /**
     * Finds all patient visits for a specific patient.
     */
    List<PatientVisit> findByPatientCedula(PatientCedula patientCedula);

    /**
     * Finds all patient visits.
     */
    List<PatientVisit> findAll();

    /**
     * Finds all completed patient visits.
     */
    List<PatientVisit> findAllCompleted();

    /**
     * Checks if a patient visit exists with the given ID.
     */
    boolean existsById(PatientVisitId patientVisitId);

    /**
     * Deletes a patient visit by its identifier.
     */
    void deleteById(PatientVisitId patientVisitId);

    /**
     * Counts total number of patient visits.
     */
    long count();

    /**
     * Counts patient visits by patient.
     */
    long countByPatient(PatientCedula patientCedula);

    /**
     * Counts completed patient visits.
     */
    long countCompleted();

    /**
     * Finds patient visits within a date range.
     */
    List<PatientVisit> findByVisitDateTimeBetween(java.time.LocalDateTime startDateTime, java.time.LocalDateTime endDateTime);
}