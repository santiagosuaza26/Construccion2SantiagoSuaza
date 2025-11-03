package app.clinic.infrastructure.persistence.mongodb;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.clinic.infrastructure.persistence.jpa.MedicalRecordJpaEntity;
import app.clinic.infrastructure.persistence.jpa.MedicalRecordJpaRepository;
import jakarta.annotation.PostConstruct;

@Service
@Profile("mongodb")
public class MigrationService {

    private final MedicalRecordJpaRepository jpaRepository;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public MigrationService(MedicalRecordJpaRepository jpaRepository,
                           MongoTemplate mongoTemplate,
                           ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void migrateMedicalRecords() {
        System.out.println("Starting migration of medical records from JPA to MongoDB...");

        List<MedicalRecordJpaEntity> jpaEntities = jpaRepository.findAll();

        for (MedicalRecordJpaEntity jpaEntity : jpaEntities) {
            try {
                MedicalRecordDocument document = convertToDocument(jpaEntity);
                mongoTemplate.save(document);
                System.out.println("Migrated medical record for patient: " + jpaEntity.getPatientId());
            } catch (Exception e) {
                System.err.println("Error migrating medical record for patient " + jpaEntity.getPatientId() + ": " + e.getMessage());
            }
        }

        System.out.println("Migration completed. Migrated " + jpaEntities.size() + " medical records.");
    }

    private MedicalRecordDocument convertToDocument(MedicalRecordJpaEntity jpaEntity) {
        Map<LocalDate, Map<String, Object>> records = new HashMap<>();

        Map<String, Object> record = new HashMap<>();
        record.put("doctorIdentificationNumber", jpaEntity.getDoctorId());
        record.put("reason", jpaEntity.getReason());
        record.put("symptoms", jpaEntity.getSymptoms());
        record.put("diagnosis", jpaEntity.getDiagnosis());

        // Convert prescriptions
        if (jpaEntity.getPrescriptions() != null && !jpaEntity.getPrescriptions().isEmpty()) {
            try {
                Map<String, Object> medication = objectMapper.readValue(jpaEntity.getPrescriptions(),
                    new TypeReference<Map<String, Object>>() {});
                record.put("medication", medication);
            } catch (JsonProcessingException e) {
                System.err.println("Error parsing prescriptions for patient " + jpaEntity.getPatientId());
            }
        }

        // Convert procedures
        if (jpaEntity.getProcedures() != null && !jpaEntity.getProcedures().isEmpty()) {
            try {
                Map<String, Object> procedure = objectMapper.readValue(jpaEntity.getProcedures(),
                    new TypeReference<Map<String, Object>>() {});
                record.put("procedure", procedure);
            } catch (JsonProcessingException e) {
                System.err.println("Error parsing procedures for patient " + jpaEntity.getPatientId());
            }
        }

        // Convert diagnostic aids
        if (jpaEntity.getDiagnosticAids() != null && !jpaEntity.getDiagnosticAids().isEmpty()) {
            try {
                Map<String, Object> aid = objectMapper.readValue(jpaEntity.getDiagnosticAids(),
                    new TypeReference<Map<String, Object>>() {});
                record.put("diagnosticAid", aid);
            } catch (JsonProcessingException e) {
                System.err.println("Error parsing diagnostic aids for patient " + jpaEntity.getPatientId());
            }
        }

        // Add order number if present
        if (jpaEntity.getOrderNumber() != null) {
            record.put("orderNumber", jpaEntity.getOrderNumber());
        }

        records.put(jpaEntity.getConsultationDate(), record);

        return new MedicalRecordDocument(jpaEntity.getPatientId(), records);
    }
}