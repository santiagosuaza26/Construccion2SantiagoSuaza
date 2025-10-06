package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing unstructured medical record data.
 * Stored as a dictionary/map structure for flexibility.
 */
public class PatientRecordData {
    private final Map<String, Object> data;

    private PatientRecordData(Map<String, Object> data) {
        this.data = new HashMap<>(data != null ? data : new HashMap<>());
    }

    public static PatientRecordData of(Map<String, Object> data) {
        return new PatientRecordData(data);
    }

    public static PatientRecordData empty() {
        return new PatientRecordData(new HashMap<>());
    }

    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public PatientRecordData put(String key, Object value) {
        Map<String, Object> newData = new HashMap<>(data);
        newData.put(key, value);
        return new PatientRecordData(newData);
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
        PatientRecordData that = (PatientRecordData) o;
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