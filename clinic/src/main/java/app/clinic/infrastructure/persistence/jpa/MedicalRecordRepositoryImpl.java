package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.repository.MedicalRecordRepository;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    private final MedicalRecordJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;

    public MedicalRecordRepositoryImpl(MedicalRecordJpaRepository jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<MedicalRecord> findByPatientIdentificationNumber(String patientId) {
        List<MedicalRecordJpaEntity> entities = jpaRepository.findByPatientIdOrderByConsultationDateDesc(patientId);
        if (entities.isEmpty()) {
            return Optional.empty();
        }

        MedicalRecord medicalRecord = new MedicalRecord(patientId);
        for (MedicalRecordJpaEntity entity : entities) {
            Map<String, Object> record = new HashMap<>();
            record.put("doctorIdentificationNumber", entity.getDoctorId());
            record.put("reason", entity.getReason());
            record.put("symptoms", entity.getSymptoms());
            record.put("diagnosis", entity.getDiagnosis());

            // Add medication if present
            if (entity.getPrescriptions() != null && !entity.getPrescriptions().isEmpty()) {
                try {
                    Map<String, Object> medication = objectMapper.readValue(entity.getPrescriptions(), new TypeReference<Map<String, Object>>() {});
                    record.put("medication", medication);
                } catch (JsonProcessingException e) {
                    // Handle parsing error
                }
            }

            // Add procedure if present
            if (entity.getProcedures() != null && !entity.getProcedures().isEmpty()) {
                try {
                    Map<String, Object> procedure = objectMapper.readValue(entity.getProcedures(), new TypeReference<Map<String, Object>>() {});
                    record.put("procedure", procedure);
                } catch (JsonProcessingException e) {
                    // Handle parsing error
                }
            }

            // Add diagnostic aid if present
            if (entity.getDiagnosticAids() != null && !entity.getDiagnosticAids().isEmpty()) {
                try {
                    Map<String, Object> aid = objectMapper.readValue(entity.getDiagnosticAids(), new TypeReference<Map<String, Object>>() {});
                    record.put("diagnosticAid", aid);
                } catch (JsonProcessingException e) {
                    // Handle parsing error
                }
            }

            // Add order number if present
            if (entity.getOrderNumber() != null) {
                record.put("orderNumber", entity.getOrderNumber());
            }

            medicalRecord.getRecords().put(entity.getConsultationDate(), record);
        }

        return Optional.of(medicalRecord);
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        for (Map.Entry<LocalDate, Map<String, Object>> entry : medicalRecord.getRecords().entrySet()) {
            LocalDate date = entry.getKey();
            Map<String, Object> record = entry.getValue();

            MedicalRecordJpaEntity entity = new MedicalRecordJpaEntity();
            entity.setPatientId(medicalRecord.getPatientIdentificationNumber());
            entity.setConsultationDate(date);
            entity.setDoctorId((String) record.get("doctorIdentificationNumber"));
            entity.setReason((String) record.get("reason"));
            entity.setSymptoms((String) record.get("symptoms"));
            entity.setDiagnosis((String) record.get("diagnosis"));

            // Handle medication
            if (record.containsKey("medication")) {
                try {
                    entity.setPrescriptions(objectMapper.writeValueAsString(record.get("medication")));
                } catch (JsonProcessingException e) {
                    // Handle serialization error
                }
            }

            // Handle procedure
            if (record.containsKey("procedure")) {
                try {
                    entity.setProcedures(objectMapper.writeValueAsString(record.get("procedure")));
                } catch (JsonProcessingException e) {
                    // Handle serialization error
                }
            }

            // Handle diagnostic aid
            if (record.containsKey("diagnosticAid")) {
                try {
                    entity.setDiagnosticAids(objectMapper.writeValueAsString(record.get("diagnosticAid")));
                } catch (JsonProcessingException e) {
                    // Handle serialization error
                }
            }

            // Handle order number
            if (record.containsKey("orderNumber")) {
                entity.setOrderNumber((String) record.get("orderNumber"));
            }

            jpaRepository.save(entity);
        }
    }

    @Override
    public boolean existsByPatientIdentificationNumber(String patientId) {
        return jpaRepository.existsByPatientId(patientId);
    }
}