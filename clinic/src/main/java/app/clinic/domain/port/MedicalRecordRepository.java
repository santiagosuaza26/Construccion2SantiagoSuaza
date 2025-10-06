package app.clinic.domain.port;

import java.util.Optional;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientRecord;
import app.clinic.domain.model.PatientRecordEntry;
import app.clinic.domain.model.PatientRecordKey;
import app.clinic.domain.model.PatientRecordMap;
import app.clinic.domain.model.PatientRecordMapWithData;
import app.clinic.domain.model.PatientRecordWithData;

/**
 * Port interface for medical record repository operations.
 * Defines the contract for medical record data access in the domain layer.
 * Handles both structured and unstructured medical record storage.
 */
public interface MedicalRecordRepository {

    /**
     * Saves a complete medical record map.
     */
    PatientRecordMap save(PatientRecordMap medicalRecordMap);

    /**
     * Saves a medical record map with additional data.
     */
    PatientRecordMapWithData saveWithData(PatientRecordMapWithData medicalRecordMap);

    /**
     * Finds medical record by patient cedula.
     */
    Optional<PatientRecord> findByPatientCedula(PatientCedula patientCedula);

    /**
     * Finds medical record with data by patient cedula.
     */
    Optional<PatientRecordWithData> findByPatientCedulaWithData(PatientCedula patientCedula);

    /**
     * Finds a specific record entry by composite key.
     */
    Optional<PatientRecordEntry> findEntryByKey(PatientRecordKey key);

    /**
     * Finds all medical records.
     */
    PatientRecordMap findAll();

    /**
     * Finds all medical records with data.
     */
    PatientRecordMapWithData findAllWithData();

    /**
     * Checks if a patient has any medical records.
     */
    boolean existsByPatientCedula(PatientCedula patientCedula);

    /**
     * Deletes medical record by patient cedula.
     */
    void deleteByPatientCedula(PatientCedula patientCedula);

    /**
     * Counts total number of medical records.
     */
    long count();
}