package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing data for patient record entries.
 * Contains structured data for medications, procedures, and diagnostic aids.
 */
public class PatientRecordEntryData {
    private final Map<String, Object> data;

    private PatientRecordEntryData(Map<String, Object> data) {
        this.data = new HashMap<>(data != null ? data : new HashMap<>());
    }

    public static PatientRecordEntryData of(Map<String, Object> data) {
        return new PatientRecordEntryData(data);
    }

    public static PatientRecordEntryData empty() {
        return new PatientRecordEntryData(new HashMap<>());
    }

    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public PatientRecordEntryData put(String key, Object value) {
        Map<String, Object> newData = new HashMap<>(data);
        newData.put(key, value);
        return new PatientRecordEntryData(newData);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordEntryData that = (PatientRecordEntryData) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}