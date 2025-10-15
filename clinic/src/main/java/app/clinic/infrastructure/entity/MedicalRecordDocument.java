package app.clinic.infrastructure.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import app.clinic.domain.model.ConsultationReason;
import app.clinic.domain.model.Diagnosis;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientRecord;
import app.clinic.domain.model.PatientRecordDate;
import app.clinic.domain.model.PatientRecordEntry;
import app.clinic.domain.model.PatientRecordMap;
import app.clinic.domain.model.Symptoms;

/**
 * MongoDB document entity for medical records.
 * Maps domain objects to MongoDB document structure for unstructured clinical history storage.
 */
@Document(collection = "clinical_records")
public class MedicalRecordDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String patientNationalId;

    private List<RecordEntry> records;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructors
    public MedicalRecordDocument() {
        this.records = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public MedicalRecordDocument(String patientNationalId, List<RecordEntry> records) {
        this.patientNationalId = patientNationalId;
        this.records = records != null ? records : new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Static factory methods
    public static MedicalRecordDocument fromDomain(PatientRecordMap patientRecordMap) {
        Map<PatientCedula, PatientRecord> records = patientRecordMap.getRecords();

        if (records.isEmpty()) {
            return new MedicalRecordDocument();
        }

        // For simplicity, we'll handle the first patient record
        // In a real scenario, you might want to handle multiple patients differently
        Map.Entry<PatientCedula, PatientRecord> entry = records.entrySet().iterator().next();
        PatientCedula patientCedula = entry.getKey();
        PatientRecord patientRecord = entry.getValue();

        List<RecordEntry> recordEntries = new ArrayList<>();
        patientRecord.getRecords().forEach((date, recordEntry) -> {
            recordEntries.add(RecordEntry.fromDomain(date, recordEntry));
        });

        MedicalRecordDocument document = new MedicalRecordDocument(patientCedula.getValue(), recordEntries);
        return document;
    }

    public static MedicalRecordDocument fromDomainWithPatientId(String patientNationalId, PatientRecord patientRecord) {
        List<RecordEntry> recordEntries = new ArrayList<>();
        patientRecord.getRecords().forEach((date, recordEntry) -> {
            recordEntries.add(RecordEntry.fromDomain(date, recordEntry));
        });

        MedicalRecordDocument document = new MedicalRecordDocument(patientNationalId, recordEntries);
        return document;
    }

    // Conversion methods
    public PatientRecordMap toDomainMap() {
        Map<PatientCedula, PatientRecord> domainRecords = new HashMap<>();

        if (this.records != null && !this.records.isEmpty()) {
            Map<PatientRecordDate, PatientRecordEntry> patientRecords = new HashMap<>();

            for (RecordEntry entry : this.records) {
                PatientRecordDate date = PatientRecordDate.of(LocalDate.parse(entry.getDate()));
                PatientRecordEntry domainEntry = entry.toDomain();
                patientRecords.put(date, domainEntry);
            }

            PatientRecord patientRecord = PatientRecord.of(patientRecords);
            PatientCedula patientCedula = PatientCedula.of(this.patientNationalId);
            domainRecords.put(patientCedula, patientRecord);
        }

        return PatientRecordMap.of(domainRecords);
    }

    public PatientRecord toDomainRecord() {
        Map<PatientRecordDate, PatientRecordEntry> patientRecords = new HashMap<>();

        if (this.records != null) {
            for (RecordEntry entry : this.records) {
                PatientRecordDate date = PatientRecordDate.of(LocalDate.parse(entry.getDate()));
                PatientRecordEntry domainEntry = entry.toDomain();
                patientRecords.put(date, domainEntry);
            }
        }

        return PatientRecord.of(patientRecords);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientNationalId() {
        return patientNationalId;
    }

    public void setPatientNationalId(String patientNationalId) {
        this.patientNationalId = patientNationalId;
    }

    public List<RecordEntry> getRecords() {
        return records;
    }

    public void setRecords(List<RecordEntry> records) {
        this.records = records;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Embedded document for individual record entries.
     */
    public static class RecordEntry {
        private String date;
        private String doctorNationalId;
        private String consultationReason;
        private String symptoms;
        private String diagnosis;
        private Map<String, Object> additionalData;

        // Constructors
        public RecordEntry() {}

        public RecordEntry(String date, String doctorNationalId, String consultationReason,
                          String symptoms, String diagnosis, Map<String, Object> additionalData) {
            this.date = date;
            this.doctorNationalId = doctorNationalId;
            this.consultationReason = consultationReason;
            this.symptoms = symptoms;
            this.diagnosis = diagnosis;
            this.additionalData = additionalData != null ? additionalData : new HashMap<>();
        }

        // Static factory method
        public static RecordEntry fromDomain(PatientRecordDate date, PatientRecordEntry entry) {
            Map<String, Object> additionalData = new HashMap<>();
            // Add any additional unstructured data here
            additionalData.put("observations", "");
            additionalData.put("vitalSigns", Map.of());
            additionalData.put("medications", new ArrayList<>());
            additionalData.put("procedures", new ArrayList<>());
            additionalData.put("diagnosticAids", new ArrayList<>());

            return new RecordEntry(
                date.getValue().toString(),
                entry.getDoctorCedula().getValue(),
                entry.getConsultationReason().getValue(),
                entry.getSymptoms().getValue(),
                entry.getDiagnosis().getValue(),
                additionalData
            );
        }

        // Conversion method
        public PatientRecordEntry toDomain() {
            DoctorCedula doctorCedula = DoctorCedula.of(this.doctorNationalId);
            ConsultationReason consultationReason = ConsultationReason.of(this.consultationReason);
            Symptoms symptoms = Symptoms.of(this.symptoms);
            Diagnosis diagnosis = Diagnosis.of(this.diagnosis);

            return PatientRecordEntry.of(doctorCedula, consultationReason, symptoms, diagnosis);
        }

        // Getters and Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDoctorNationalId() {
            return doctorNationalId;
        }

        public void setDoctorNationalId(String doctorNationalId) {
            this.doctorNationalId = doctorNationalId;
        }

        public String getConsultationReason() {
            return consultationReason;
        }

        public void setConsultationReason(String consultationReason) {
            this.consultationReason = consultationReason;
        }

        public String getSymptoms() {
            return symptoms;
        }

        public void setSymptoms(String symptoms) {
            this.symptoms = symptoms;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public Map<String, Object> getAdditionalData() {
            return additionalData;
        }

        public void setAdditionalData(Map<String, Object> additionalData) {
            this.additionalData = additionalData;
        }
    }
}