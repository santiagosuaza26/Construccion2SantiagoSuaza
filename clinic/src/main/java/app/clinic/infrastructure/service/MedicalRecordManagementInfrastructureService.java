package app.clinic.infrastructure.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientRecord;
import app.clinic.domain.model.PatientRecordEntry;
import app.clinic.domain.model.PatientRecordKey;
import app.clinic.domain.model.PatientRecordMap;
import app.clinic.domain.model.PatientRecordMapWithData;
import app.clinic.domain.model.PatientRecordWithData;
import app.clinic.domain.port.MedicalRecordRepository;

/**
 * Infrastructure service for medical record management operations.
 * Provides business logic implementation for medical record operations.
 */
@Service
@Transactional
public class MedicalRecordManagementInfrastructureService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordManagementInfrastructureService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * Saves a medical record map.
     */
    public PatientRecordMap saveMedicalRecordMap(PatientRecordMap medicalRecordMap) {
        return medicalRecordRepository.save(medicalRecordMap);
    }

    /**
     * Saves a medical record map with additional data.
     */
    public PatientRecordMapWithData saveMedicalRecordMapWithData(PatientRecordMapWithData medicalRecordMap) {
        return medicalRecordRepository.saveWithData(medicalRecordMap);
    }

    /**
     * Finds medical record by patient cedula.
     */
    @Transactional(readOnly = true)
    public Optional<PatientRecord> findMedicalRecordByPatient(PatientCedula patientCedula) {
        return medicalRecordRepository.findByPatientCedula(patientCedula);
    }

    /**
     * Finds medical record with data by patient cedula.
     */
    @Transactional(readOnly = true)
    public Optional<PatientRecordWithData> findMedicalRecordWithDataByPatient(PatientCedula patientCedula) {
        return medicalRecordRepository.findByPatientCedulaWithData(patientCedula);
    }

    /**
     * Finds a specific record entry by key.
     */
    @Transactional(readOnly = true)
    public Optional<PatientRecordEntry> findRecordEntryByKey(PatientRecordKey key) {
        return medicalRecordRepository.findEntryByKey(key);
    }

    /**
     * Finds all medical records.
     */
    @Transactional(readOnly = true)
    public PatientRecordMap findAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    /**
     * Finds all medical records with data.
     */
    @Transactional(readOnly = true)
    public PatientRecordMapWithData findAllMedicalRecordsWithData() {
        return medicalRecordRepository.findAllWithData();
    }

    /**
     * Checks if a patient has medical records.
     */
    @Transactional(readOnly = true)
    public boolean hasMedicalRecords(PatientCedula patientCedula) {
        return medicalRecordRepository.existsByPatientCedula(patientCedula);
    }

    /**
     * Deletes medical record by patient cedula.
     */
    public void deleteMedicalRecordByPatient(PatientCedula patientCedula) {
        medicalRecordRepository.deleteByPatientCedula(patientCedula);
    }

    /**
     * Counts total number of medical records.
     */
    @Transactional(readOnly = true)
    public long countMedicalRecords() {
        return medicalRecordRepository.count();
    }
}