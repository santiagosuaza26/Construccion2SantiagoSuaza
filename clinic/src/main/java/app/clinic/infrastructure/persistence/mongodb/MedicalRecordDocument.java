package app.clinic.infrastructure.persistence.mongodb;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medical_records")
public class MedicalRecordDocument {
    @Id
    private String patientId; // Cédula como clave principal

    private Map<LocalDate, Map<String, Object>> records;

    public MedicalRecordDocument() {}

    public MedicalRecordDocument(String patientId, Map<LocalDate, Map<String, Object>> records) {
        this.patientId = patientId;
        this.records = records;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Map<LocalDate, Map<String, Object>> getRecords() {
        return records;
    }

    public void setRecords(Map<LocalDate, Map<String, Object>> records) {
        this.records = records;
    }
}