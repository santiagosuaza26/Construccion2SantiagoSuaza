package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a map of patient record entry data with additional data.
 * Maps composite keys to patient record entry data with data for structured storage.
 */
public class PatientRecordEntryDataMapWithData {
    private final Map<PatientRecordEntryKey, PatientRecordEntryData> data;

    private PatientRecordEntryDataMapWithData(Map<PatientRecordEntryKey, PatientRecordEntryData> data) {
        this.data = new HashMap<>(data != null ? data : new HashMap<>());
    }

    public static PatientRecordEntryDataMapWithData of(Map<PatientRecordEntryKey, PatientRecordEntryData> data) {
        return new PatientRecordEntryDataMapWithData(data);
    }

    public static PatientRecordEntryDataMapWithData empty() {
        return new PatientRecordEntryDataMapWithData(new HashMap<>());
    }

    public Map<PatientRecordEntryKey, PatientRecordEntryData> getData() {
        return new HashMap<>(data);
    }

    public PatientRecordEntryData getData(PatientRecordEntryKey key) {
        return data.get(key);
    }

    public PatientRecordEntryDataMapWithData addData(PatientRecordEntryKey key, PatientRecordEntryData entryData) {
        Map<PatientRecordEntryKey, PatientRecordEntryData> newData = new HashMap<>(data);
        newData.put(key, entryData);
        return new PatientRecordEntryDataMapWithData(newData);
    }

    public boolean hasData(PatientRecordEntryKey key) {
        return data.containsKey(key);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordEntryDataMapWithData that = (PatientRecordEntryDataMapWithData) o;
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