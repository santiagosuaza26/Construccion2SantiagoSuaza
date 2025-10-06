package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a complete patient medical record.
 * Maps record dates to medical record entries for unstructured storage.
 */
public class PatientRecord {
    private final Map<PatientRecordDate, PatientRecordEntry> records;

    private PatientRecord(Map<PatientRecordDate, PatientRecordEntry> records) {
        this.records = new HashMap<>(records != null ? records : new HashMap<>());
    }

    public static PatientRecord of(Map<PatientRecordDate, PatientRecordEntry> records) {
        return new PatientRecord(records);
    }

    public static PatientRecord empty() {
        return new PatientRecord(new HashMap<>());
    }

    public Map<PatientRecordDate, PatientRecordEntry> getRecords() {
        return new HashMap<>(records);
    }

    public PatientRecordEntry getRecord(PatientRecordDate date) {
        return records.get(date);
    }

    public PatientRecord addRecord(PatientRecordDate date, PatientRecordEntry entry) {
        Map<PatientRecordDate, PatientRecordEntry> newRecords = new HashMap<>(records);
        newRecords.put(date, entry);
        return new PatientRecord(newRecords);
    }

    public PatientRecord removeRecord(PatientRecordDate date) {
        Map<PatientRecordDate, PatientRecordEntry> newRecords = new HashMap<>(records);
        newRecords.remove(date);
        return new PatientRecord(newRecords);
    }

    public boolean hasRecord(PatientRecordDate date) {
        return records.containsKey(date);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecord that = (PatientRecord) o;
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