package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a map of patient record data.
 * Maps patient cedula to their complete record data for NoSQL storage.
 */
public class PatientRecordDataMap {
    private final Map<PatientCedula, PatientRecordData> records;

    private PatientRecordDataMap(Map<PatientCedula, PatientRecordData> records) {
        this.records = new HashMap<>(records != null ? records : new HashMap<>());
    }

    public static PatientRecordDataMap of(Map<PatientCedula, PatientRecordData> records) {
        return new PatientRecordDataMap(records);
    }

    public static PatientRecordDataMap empty() {
        return new PatientRecordDataMap(new HashMap<>());
    }

    public Map<PatientCedula, PatientRecordData> getRecords() {
        return new HashMap<>(records);
    }

    public PatientRecordData getRecord(PatientCedula patientCedula) {
        return records.get(patientCedula);
    }

    public PatientRecordDataMap addRecord(PatientCedula patientCedula, PatientRecordData record) {
        Map<PatientCedula, PatientRecordData> newRecords = new HashMap<>(records);
        newRecords.put(patientCedula, record);
        return new PatientRecordDataMap(newRecords);
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
        PatientRecordDataMap that = (PatientRecordDataMap) o;
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