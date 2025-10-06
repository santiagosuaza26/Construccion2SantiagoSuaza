package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a complete patient medical record with additional data.
 * Maps record dates to medical record entries with data for structured storage.
 */
public class PatientRecordWithData {
    private final Map<PatientRecordDate, PatientRecordEntryWithData> records;

    private PatientRecordWithData(Map<PatientRecordDate, PatientRecordEntryWithData> records) {
        this.records = new HashMap<>(records != null ? records : new HashMap<>());
    }

    public static PatientRecordWithData of(Map<PatientRecordDate, PatientRecordEntryWithData> records) {
        return new PatientRecordWithData(records);
    }

    public static PatientRecordWithData empty() {
        return new PatientRecordWithData(new HashMap<>());
    }

    public Map<PatientRecordDate, PatientRecordEntryWithData> getRecords() {
        return new HashMap<>(records);
    }

    public PatientRecordEntryWithData getRecord(PatientRecordDate date) {
        return records.get(date);
    }

    public PatientRecordWithData addRecord(PatientRecordDate date, PatientRecordEntryWithData entry) {
        Map<PatientRecordDate, PatientRecordEntryWithData> newRecords = new HashMap<>(records);
        newRecords.put(date, entry);
        return new PatientRecordWithData(newRecords);
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
        PatientRecordWithData that = (PatientRecordWithData) o;
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