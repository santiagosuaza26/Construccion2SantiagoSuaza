package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a map of patient record entries with additional data.
 * Maps composite keys to patient record entries with data for structured storage.
 */
public class PatientRecordEntryMapWithData {
    private final Map<PatientRecordEntryKey, PatientRecordEntryWithData> entries;

    private PatientRecordEntryMapWithData(Map<PatientRecordEntryKey, PatientRecordEntryWithData> entries) {
        this.entries = new HashMap<>(entries != null ? entries : new HashMap<>());
    }

    public static PatientRecordEntryMapWithData of(Map<PatientRecordEntryKey, PatientRecordEntryWithData> entries) {
        return new PatientRecordEntryMapWithData(entries);
    }

    public static PatientRecordEntryMapWithData empty() {
        return new PatientRecordEntryMapWithData(new HashMap<>());
    }

    public Map<PatientRecordEntryKey, PatientRecordEntryWithData> getEntries() {
        return new HashMap<>(entries);
    }

    public PatientRecordEntryWithData getEntry(PatientRecordEntryKey key) {
        return entries.get(key);
    }

    public PatientRecordEntryMapWithData addEntry(PatientRecordEntryKey key, PatientRecordEntryWithData entry) {
        Map<PatientRecordEntryKey, PatientRecordEntryWithData> newEntries = new HashMap<>(entries);
        newEntries.put(key, entry);
        return new PatientRecordEntryMapWithData(newEntries);
    }

    public boolean hasEntry(PatientRecordEntryKey key) {
        return entries.containsKey(key);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordEntryMapWithData that = (PatientRecordEntryMapWithData) o;
        return Objects.equals(entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entries);
    }

    @Override
    public String toString() {
        return entries.toString();
    }
}