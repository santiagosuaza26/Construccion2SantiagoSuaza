package app.clinic.domain.port;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientId;
import app.clinic.domain.model.PatientUsername;

/**
 * Port interface for patient repository operations.
 * Defines the contract for patient data access in the domain layer.
 */
public interface PatientRepository {

    /**
     * Saves a new patient or updates an existing one.
     */
    Patient save(Patient patient);

    /**
     * Finds a patient by their unique identifier.
     */
    Optional<Patient> findById(PatientId patientId);

    /**
     * Finds a patient by their cedula.
     */
    Optional<Patient> findByCedula(PatientCedula cedula);

    /**
     * Finds a patient by their username.
     */
    Optional<Patient> findByUsername(PatientUsername username);

    /**
     * Finds all patients.
     */
    List<Patient> findAll();

    /**
     * Checks if a patient exists with the given cedula.
     */
    boolean existsByCedula(PatientCedula cedula);

    /**
     * Checks if a patient exists with the given username.
     */
    boolean existsByUsername(PatientUsername username);

    /**
     * Deletes a patient by their identifier.
     */
    void deleteById(PatientId patientId);

    /**
     * Deletes a patient by their cedula.
     */
    void deleteByCedula(PatientCedula cedula);

    /**
     * Counts total number of patients.
     */
    long count();
}