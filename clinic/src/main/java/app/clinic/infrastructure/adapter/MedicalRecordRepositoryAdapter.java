package app.clinic.infrastructure.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientRecord;
import app.clinic.domain.model.PatientRecordEntry;
import app.clinic.domain.model.PatientRecordKey;
import app.clinic.domain.model.PatientRecordMap;
import app.clinic.domain.port.MedicalRecordRepository;
import app.clinic.infrastructure.entity.MedicalRecordDocument;
import app.clinic.infrastructure.repository.MedicalRecordMongoRepository;

/**
 * Adapter that implements the domain's medical record repository port.
 * Connects the domain layer to MongoDB infrastructure for clinical history storage.
 */
@Component
public class MedicalRecordRepositoryAdapter implements MedicalRecordRepository {

    private final MedicalRecordMongoRepository mongoRepository;

    public MedicalRecordRepositoryAdapter(MedicalRecordMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PatientRecordMap save(PatientRecordMap medicalRecordMap) {
        MedicalRecordDocument document = MedicalRecordDocument.fromDomain(medicalRecordMap);
        MedicalRecordDocument savedDocument = mongoRepository.save(document);
        return savedDocument.toDomainMap();
    }

    @Override
    public Optional<PatientRecord> findByPatientCedula(PatientCedula patientCedula) {
        Optional<MedicalRecordDocument> document = mongoRepository.findByPatientNationalId(patientCedula.getValue());

        if (document.isPresent()) {
            PatientRecord patientRecord = document.get().toDomainRecord();
            return Optional.of(patientRecord);
        }

        return Optional.empty();
    }

    @Override
    public Optional<PatientRecordEntry> findEntryByKey(PatientRecordKey key) {
        // This is a simplified implementation
        // In a real scenario, you might need to implement more complex logic
        Optional<PatientRecord> patientRecord = findByPatientCedula(key.getPatientCedula());

        if (patientRecord.isPresent()) {
            return Optional.ofNullable(patientRecord.get().getRecord(key.getRecordDate()));
        }

        return Optional.empty();
    }

    @Override
    public PatientRecordMap findAll() {
        java.util.List<MedicalRecordDocument> documents = mongoRepository.findAll();
        java.util.Map<PatientCedula, PatientRecord> allRecords = new java.util.HashMap<>();

        for (MedicalRecordDocument document : documents) {
            PatientRecordMap map = document.toDomainMap();
            allRecords.putAll(map.getRecords());
        }

        return PatientRecordMap.of(allRecords);
    }

    @Override
    public boolean existsByPatientCedula(PatientCedula patientCedula) {
        return mongoRepository.existsByPatientNationalId(patientCedula.getValue());
    }

    @Override
    public void deleteByPatientCedula(PatientCedula patientCedula) {
        mongoRepository.deleteByPatientNationalId(patientCedula.getValue());
    }

    @Override
    public long count() {
        return mongoRepository.count();
    }

    // Additional methods required by the interface - simplified implementations
    @Override
    public app.clinic.domain.model.PatientRecordMapWithData saveWithData(app.clinic.domain.model.PatientRecordMapWithData medicalRecordMap) {
        // Simplified implementation - just return empty for now
        return app.clinic.domain.model.PatientRecordMapWithData.empty();
    }

    @Override
    public java.util.Optional<app.clinic.domain.model.PatientRecordWithData> findByPatientCedulaWithData(PatientCedula patientCedula) {
        // Simplified implementation - return empty for now
        return java.util.Optional.empty();
    }

    @Override
    public app.clinic.domain.model.PatientRecordMapWithData findAllWithData() {
        // Simplified implementation - return empty for now
        return app.clinic.domain.model.PatientRecordMapWithData.empty();
    }
}