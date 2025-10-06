package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a map of patient record entries.
 * Maps composite keys to patient record entries for structured storage.
 */
public class PatientRecordEntryMap {
    private final Map<PatientRecordEntryKey, PatientRecordEntry> entries;

    private PatientRecordEntryMap(Map<PatientRecordEntryKey, PatientRecordEntry> entries) {
        this.entries = new HashMap<>(entries != null ? entries : new HashMap<>());
    }

    public static PatientRecordEntryMap of(Map<PatientRecordEntryKey, PatientRecordEntry> entries) {
        return new PatientRecordEntryMap(entries);
    }

    public static PatientRecordEntryMap empty() {
        return new PatientRecordEntryMap(new HashMap<>());
    }

    public Map<PatientRecordEntryKey, PatientRecordEntry> getEntries() {
        return new HashMap<>(entries);
    }

    public PatientRecordEntry getEntry(PatientRecordEntryKey key) {
        return entries.get(key);
    }

    public PatientRecordEntryMap addEntry(PatientRecordEntryKey key, PatientRecordEntry entry) {
        Map<PatientRecordEntryKey, PatientRecordEntry> newEntries = new HashMap<>(entries);
        newEntries.put(key, entry);
        return new PatientRecordEntryMap(newEntries);
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
        PatientRecordEntryMap that = (PatientRecordEntryMap) o;
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