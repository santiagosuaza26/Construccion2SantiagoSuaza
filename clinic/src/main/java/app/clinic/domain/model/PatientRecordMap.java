package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a map of patient medical records.
 * Maps patient cedula to their complete medical record for NoSQL storage.
 */
public class PatientRecordMap {
    private final Map<PatientCedula, PatientRecord> records;

    private PatientRecordMap(Map<PatientCedula, PatientRecord> records) {
        this.records = new HashMap<>(records != null ? records : new HashMap<>());
    }

    public static PatientRecordMap of(Map<PatientCedula, PatientRecord> records) {
        return new PatientRecordMap(records);
    }

    public static PatientRecordMap empty() {
        return new PatientRecordMap(new HashMap<>());
    }

    public Map<PatientCedula, PatientRecord> getRecords() {
        return new HashMap<>(records);
    }

    public PatientRecord getRecord(PatientCedula patientCedula) {
        return records.get(patientCedula);
    }

    public PatientRecordMap addRecord(PatientCedula patientCedula, PatientRecord patientRecord) {
        Map<PatientCedula, PatientRecord> newRecords = new HashMap<>(records);
        newRecords.put(patientCedula, patientRecord);
        return new PatientRecordMap(newRecords);
    }

    public boolean hasRecord(PatientCedula patientCedula) {
        return records.containsKey(patientCedula);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordMap that = (PatientRecordMap) o;
        return Objects.equals(records, that.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }

    @Override
    public String toString() {
        return records.toString();
    }
}