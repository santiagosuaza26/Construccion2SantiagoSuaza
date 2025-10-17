package app.clinic.domain.service;


import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientRecord;
import app.clinic.domain.model.PatientRecordDate;
import app.clinic.domain.model.PatientRecordEntry;
import app.clinic.domain.model.PatientRecordKey;
import app.clinic.domain.model.PatientRecordMap;
import app.clinic.domain.port.MedicalRecordRepository;

/**
 * Domain service for medical record operations in the clinic management system.
 *
 * This service manages patient medical histories using a MongoDB document-based approach
 * that provides flexible storage for complex medical data structures. The service implements:
 *
 * - **Document-based storage**: Uses MongoDB for flexible schema medical records
 * - **Temporal record management**: Handles chronological medical entries with dates
 * - **Immutable record patterns**: Creates new document versions instead of updates
 * - **Composite key navigation**: Supports lookup by patient + date combinations
 * - **Audit trail maintenance**: Preserves complete medical history for compliance
 *
 * Architecture Pattern:
 * - PatientRecordMap: Container for all patient records in the system
 * - PatientRecord: Individual patient medical history collection
 * - PatientRecordEntry: Single dated medical record entry (consultation, procedure, etc.)
 * - PatientRecordKey: Composite key for precise record entry location
 *
 * Business Rules:
 * - Each patient can have only one medical record document
 * - Record entries are immutable once created (new entries for modifications)
 * - Empty records are not allowed (must contain at least one entry)
 * - Date-based ordering maintained for chronological medical history
 *
 * Pure domain service without external framework dependencies.
 *
 * @author Clinic Development Team
 * @version 2.0.0
 */
public class MedicalRecordDomainService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordDomainService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * Creates a new comprehensive medical record for a patient.
     *
     * This method initializes a patient's complete medical history in the system.
     * The record must contain at least one entry (consultation, procedure, diagnosis, etc.)
     * and will be stored as a MongoDB document for flexible schema support.
     *
     * Process Flow:
     * 1. Validates record content and patient eligibility
     * 2. Retrieves current system-wide record collection
     * 3. Adds new patient record to the collection
     * 4. Persists updated collection to MongoDB
     *
     * @param patientCedula Unique patient identifier
     * @param record Complete medical record with initial entries
     * @return Updated system-wide record collection
     * @throws IllegalArgumentException if patient already has records or record is invalid
     *
     * @see PatientRecord for record structure requirements
     * @see PatientRecordMap for collection management
     */
    public PatientRecordMap createMedicalRecord(PatientCedula patientCedula, PatientRecord record) {
        validateMedicalRecordForCreation(patientCedula, record);
        PatientRecordMap existingRecords = medicalRecordRepository.findAll();
        PatientRecordMap updatedRecords = existingRecords.addRecord(patientCedula, record);
        return medicalRecordRepository.save(updatedRecords);
    }

    /**
     * Adds a medical record entry to an existing patient record.
     */
    public PatientRecordMap addRecordEntry(PatientCedula patientCedula, PatientRecordDate date,
                                        PatientRecordEntry entry) {
        PatientRecordMap existingRecords = medicalRecordRepository.findAll();
        PatientRecord existingRecord = existingRecords.getRecord(patientCedula);

        if (existingRecord == null) {
            throw new IllegalArgumentException("Patient record does not exist");
        }

        PatientRecord updatedRecord = existingRecord.addRecord(date, entry);
        PatientRecordMap updatedRecords = existingRecords.addRecord(patientCedula, updatedRecord);
        return medicalRecordRepository.save(updatedRecords);
    }

    /**
     * Finds medical record by patient cedula.
     */
    public PatientRecord findByPatientCedula(PatientCedula patientCedula) {
        return medicalRecordRepository.findByPatientCedula(patientCedula).orElse(PatientRecord.empty());
    }

    /**
     * Finds a specific record entry by composite key.
     */
    public PatientRecordEntry findEntryByKey(PatientRecordKey key) {
        return medicalRecordRepository.findEntryByKey(key).orElse(null);
    }

    /**
     * Finds all medical records.
     */
    public PatientRecordMap findAll() {
        return medicalRecordRepository.findAll();
    }

    /**
     * Deletes medical record by patient cedula.
     */
    public void deleteByPatientCedula(PatientCedula patientCedula) {
        validateMedicalRecordCanBeDeleted(patientCedula);
        medicalRecordRepository.deleteByPatientCedula(patientCedula);
    }

    /**
     * Removes a specific record entry from a patient's medical record.
     */
    public PatientRecordMap removeRecordEntry(PatientCedula patientCedula, PatientRecordDate date) {
        PatientRecordMap existingRecords = medicalRecordRepository.findAll();
        PatientRecord existingRecord = existingRecords.getRecord(patientCedula);

        if (existingRecord == null) {
            throw new IllegalArgumentException("Patient record does not exist");
        }

        if (!existingRecord.hasRecord(date)) {
            throw new IllegalArgumentException("Record entry does not exist for the specified date");
        }

        PatientRecord updatedRecord = existingRecord.removeRecord(date);
        PatientRecordMap updatedRecords = existingRecords.addRecord(patientCedula, updatedRecord);
        return medicalRecordRepository.save(updatedRecords);
    }

    /**
     * Validates medical record data for creation.
     */
    private void validateMedicalRecordForCreation(PatientCedula patientCedula, PatientRecord record) {
        if (medicalRecordRepository.existsByPatientCedula(patientCedula)) {
            throw new IllegalArgumentException("Medical record already exists for this patient");
        }
        if (record == null) {
            throw new IllegalArgumentException("Patient record cannot be null");
        }
        if (record.isEmpty()) {
            throw new IllegalArgumentException("Patient record cannot be empty");
        }
        // Add additional validation rules for medical record creation
    }

    /**
     * Validates that the medical record can be deleted.
     */
    private void validateMedicalRecordCanBeDeleted(PatientCedula patientCedula) {
        if (!medicalRecordRepository.existsByPatientCedula(patientCedula)) {
            throw new IllegalArgumentException("Medical record to delete does not exist");
        }
        // Add additional business rules for medical record deletion if needed
    }
}