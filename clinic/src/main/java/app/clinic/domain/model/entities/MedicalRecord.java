package app.clinic.domain.model.entities;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MedicalRecord {
    private final String patientIdentificationNumber;
    private final Map<LocalDate, Map<String, Object>> records;

    public MedicalRecord(String patientIdentificationNumber) {
        this.patientIdentificationNumber = patientIdentificationNumber;
        this.records = new HashMap<>();
    }

    public String getPatientIdentificationNumber() {
        return patientIdentificationNumber;
    }

    public Map<LocalDate, Map<String, Object>> getRecords() {
        return records;
    }

    public void addRecord(LocalDate date, String doctorIdentificationNumber, String reason, String symptoms, String diagnosis) {
        Map<String, Object> record = new HashMap<>();
        record.put("doctorIdentificationNumber", doctorIdentificationNumber);
        record.put("reason", reason);
        record.put("symptoms", symptoms);
        record.put("diagnosis", diagnosis);
        records.put(date, record);
    }

    public void addMedicationToRecord(LocalDate date, String orderNumber, String medicationId, String dosage, String duration) {
        Map<String, Object> record = records.get(date);
        if (record == null) {
            record = new HashMap<>();
            records.put(date, record);
        }
        Map<String, Object> medication = new HashMap<>();
        medication.put("orderNumber", orderNumber);
        medication.put("medicationId", medicationId);
        medication.put("dosage", dosage);
        medication.put("duration", duration);
        record.put("medication", medication);
    }

    public void addProcedureToRecord(LocalDate date, String orderNumber, String procedureId, String quantity, String frequency, boolean requiresSpecialist, String specialistId) {
        Map<String, Object> record = records.get(date);
        if (record == null) {
            record = new HashMap<>();
            records.put(date, record);
        }
        Map<String, Object> procedure = new HashMap<>();
        procedure.put("orderNumber", orderNumber);
        procedure.put("procedureId", procedureId);
        procedure.put("quantity", quantity);
        procedure.put("frequency", frequency);
        procedure.put("requiresSpecialist", requiresSpecialist);
        procedure.put("specialistId", specialistId);
        record.put("procedure", procedure);
    }

    public void addDiagnosticAidToRecord(LocalDate date, String orderNumber, String diagnosticAidId, String quantity, boolean requiresSpecialist, String specialistId) {
        Map<String, Object> record = records.get(date);
        if (record == null) {
            record = new HashMap<>();
            records.put(date, record);
        }
        Map<String, Object> aid = new HashMap<>();
        aid.put("orderNumber", orderNumber);
        aid.put("diagnosticAidId", diagnosticAidId);
        aid.put("quantity", quantity);
        aid.put("requiresSpecialist", requiresSpecialist);
        aid.put("specialistId", specialistId);
        record.put("diagnosticAid", aid);
    }

    public void addOrderToRecord(LocalDate date, String orderNumber, String diagnosis) {
        Map<String, Object> record = records.get(date);
        if (record == null) {
            record = new HashMap<>();
            records.put(date, record);
        }
        record.put("orderNumber", orderNumber);
        if (diagnosis != null && !diagnosis.trim().isEmpty()) {
            record.put("diagnosis", diagnosis);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return patientIdentificationNumber.equals(that.patientIdentificationNumber);
    }

    @Override
    public int hashCode() {
        return patientIdentificationNumber.hashCode();
    }

    @Override
    public String toString() {
        return "MedicalRecord for " + patientIdentificationNumber;
    }
}