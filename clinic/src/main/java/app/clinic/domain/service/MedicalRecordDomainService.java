package app.clinic.domain.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientRecord;
import app.clinic.domain.model.PatientRecordDate;
import app.clinic.domain.model.PatientRecordEntry;
import app.clinic.domain.model.PatientRecordKey;
import app.clinic.domain.model.PatientRecordMap;
import app.clinic.domain.port.MedicalRecordRepository;

/**
 * Domain service for medical record operations.
 * Contains business logic for medical record management following domain-driven design principles.
 */
@Service
public class MedicalRecordDomainService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordDomainService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * Creates a new medical record entry for a patient.
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