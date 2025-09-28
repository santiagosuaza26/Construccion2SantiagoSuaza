package app.infrastructure.adapter.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

/**
 * Documento MongoDB para historia clínica NoSQL
 *
 * Según requerimientos:
 * - Clave = cédula paciente
 * - Subclave = fecha de atención
 * - Datos: motivo, diagnóstico, tratamientos, medicamentos, etc.
 */
@Document(collection = "clinical_history")
public class ClinicalHistoryDocument {

    @Id
    private String id;

    @Field("patient_id_card")
    private String patientIdCard;

    @Field("patient_name")
    private String patientName;

    @Field("entries")
    private List<ClinicalHistoryEntryDocument> entries;

    public ClinicalHistoryDocument() {}

    public ClinicalHistoryDocument(String patientIdCard, String patientName, List<ClinicalHistoryEntryDocument> entries) {
        this.patientIdCard = patientIdCard;
        this.patientName = patientName;
        this.entries = entries;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientIdCard() { return patientIdCard; }
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public List<ClinicalHistoryEntryDocument> getEntries() { return entries; }
    public void setEntries(List<ClinicalHistoryEntryDocument> entries) { this.entries = entries; }

    /**
     * Clase interna para representar cada entrada de historia clínica
     */
    public static class ClinicalHistoryEntryDocument {
        @Field("date")
        private LocalDate date;

        @Field("doctor_id_card")
        private String doctorIdCard;

        @Field("doctor_name")
        private String doctorName;

        @Field("reason")
        private String reason;

        @Field("symptoms")
        private String symptoms;

        @Field("diagnosis")
        private String diagnosis;

        @Field("vital_signs")
        private VitalSignsDocument vitalSigns;

        @Field("related_order_numbers")
        private List<String> relatedOrderNumbers;

        @Field("observations")
        private String observations;

        @Field("treatment")
        private String treatment;

        @Field("medications")
        private List<String> medications;

        public ClinicalHistoryEntryDocument() {}

        public ClinicalHistoryEntryDocument(LocalDate date, String doctorIdCard, String doctorName,
                                          String reason, String symptoms, String diagnosis,
                                          VitalSignsDocument vitalSigns, List<String> relatedOrderNumbers,
                                          String observations, String treatment, List<String> medications) {
            this.date = date;
            this.doctorIdCard = doctorIdCard;
            this.doctorName = doctorName;
            this.reason = reason;
            this.symptoms = symptoms;
            this.diagnosis = diagnosis;
            this.vitalSigns = vitalSigns;
            this.relatedOrderNumbers = relatedOrderNumbers;
            this.observations = observations;
            this.treatment = treatment;
            this.medications = medications;
        }

        // Getters y Setters
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public String getDoctorIdCard() { return doctorIdCard; }
        public void setDoctorIdCard(String doctorIdCard) { this.doctorIdCard = doctorIdCard; }

        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public String getSymptoms() { return symptoms; }
        public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

        public VitalSignsDocument getVitalSigns() { return vitalSigns; }
        public void setVitalSigns(VitalSignsDocument vitalSigns) { this.vitalSigns = vitalSigns; }

        public List<String> getRelatedOrderNumbers() { return relatedOrderNumbers; }
        public void setRelatedOrderNumbers(List<String> relatedOrderNumbers) { this.relatedOrderNumbers = relatedOrderNumbers; }

        public String getObservations() { return observations; }
        public void setObservations(String observations) { this.observations = observations; }

        public String getTreatment() { return treatment; }
        public void setTreatment(String treatment) { this.treatment = treatment; }

        public List<String> getMedications() { return medications; }
        public void setMedications(List<String> medications) { this.medications = medications; }
    }

    /**
     * Documento embebido para signos vitales
     */
    public static class VitalSignsDocument {
        @Field("blood_pressure")
        private String bloodPressure;

        @Field("temperature")
        private Double temperature;

        @Field("pulse")
        private Integer pulse;

        @Field("oxygen_level")
        private Integer oxygenLevel;

        @Field("weight")
        private Double weight;

        @Field("height")
        private Double height;

        public VitalSignsDocument() {}

        public VitalSignsDocument(String bloodPressure, Double temperature, Integer pulse,
                                Integer oxygenLevel, Double weight, Double height) {
            this.bloodPressure = bloodPressure;
            this.temperature = temperature;
            this.pulse = pulse;
            this.oxygenLevel = oxygenLevel;
            this.weight = weight;
            this.height = height;
        }

        // Getters y Setters
        public String getBloodPressure() { return bloodPressure; }
        public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }

        public Integer getPulse() { return pulse; }
        public void setPulse(Integer pulse) { this.pulse = pulse; }

        public Integer getOxygenLevel() { return oxygenLevel; }
        public void setOxygenLevel(Integer oxygenLevel) { this.oxygenLevel = oxygenLevel; }

        public Double getWeight() { return weight; }
        public void setWeight(Double weight) { this.weight = weight; }

        public Double getHeight() { return height; }
        public void setHeight(Double height) { this.height = height; }
    }
}