package app.clinic.infrastructure.persistence.mongodb;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.repository.MedicalRecordRepository;

@Repository
@Profile("mongodb")
public class MedicalRecordMongoRepository implements MedicalRecordRepository {

    private final MongoTemplate mongoTemplate;

    public MedicalRecordMongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        MedicalRecordDocument document = new MedicalRecordDocument(
            medicalRecord.getPatientIdentificationNumber(),
            medicalRecord.getRecords()
        );
        mongoTemplate.save(document);
    }

    @Override
    public Optional<MedicalRecord> findByPatientIdentificationNumber(String patientId) {
        Query query = new Query(Criteria.where("_id").is(patientId));
        MedicalRecordDocument document = mongoTemplate.findOne(query, MedicalRecordDocument.class);

        if (document == null) {
            return Optional.empty();
        }

        MedicalRecord medicalRecord = new MedicalRecord(document.getPatientId());
        medicalRecord.getRecords().putAll(document.getRecords());
        return Optional.of(medicalRecord);
    }

    @Override
    public boolean existsByPatientIdentificationNumber(String patientId) {
        Query query = new Query(Criteria.where("_id").is(patientId));
        return mongoTemplate.exists(query, MedicalRecordDocument.class);
    }
}