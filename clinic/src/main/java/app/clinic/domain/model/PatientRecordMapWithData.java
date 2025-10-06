package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a map of patient medical records with additional data.
 * Maps patient cedula to their complete medical record with data for NoSQL storage.
 */
public class PatientRecordMapWithData {
    private final Map<PatientCedula, PatientRecordWithData> records;

    private PatientRecordMapWithData(Map<PatientCedula, PatientRecordWithData> records) {
        this.records = new HashMap<>(records != null ? records : new HashMap<>());
    }

    public static PatientRecordMapWithData of(Map<PatientCedula, PatientRecordWithData> records) {
        return new PatientRecordMapWithData(records);
    }

    public static PatientRecordMapWithData empty() {
        return new PatientRecordMapWithData(new HashMap<>());
    }

    public Map<PatientCedula, PatientRecordWithData> getRecords() {
        return new HashMap<>(records);
    }

    public PatientRecordWithData getRecord(PatientCedula patientCedula) {
        return records.get(patientCedula);
    }

    public PatientRecordMapWithData addRecord(PatientCedula patientCedula, PatientRecordWithData record) {
        Map<PatientCedula, PatientRecordWithData> newRecords = new HashMap<>(records);
        newRecords.put(patientCedula, record);
        return new PatientRecordMapWithData(newRecords);
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
        PatientRecordMapWithData that = (PatientRecordMapWithData) o;
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