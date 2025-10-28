package app.clinic.domain.repository;

import java.util.Optional;

import app.clinic.domain.model.entities.MedicalRecord;

public interface MedicalRecordRepository {
    void save(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findByPatientIdentificationNumber(String patientId);
    boolean existsByPatientIdentificationNumber(String patientId);
}