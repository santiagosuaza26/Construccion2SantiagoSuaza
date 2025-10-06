package app.clinic.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a map of patient record entry data.
 * Maps composite keys to patient record entry data for structured storage.
 */
public class PatientRecordEntryDataMap {
    private final Map<PatientRecordEntryKey, PatientRecordEntryData> data;

    private PatientRecordEntryDataMap(Map<PatientRecordEntryKey, PatientRecordEntryData> data) {
        this.data = new HashMap<>(data != null ? data : new HashMap<>());
    }

    public static PatientRecordEntryDataMap of(Map<PatientRecordEntryKey, PatientRecordEntryData> data) {
        return new PatientRecordEntryDataMap(data);
    }

    public static PatientRecordEntryDataMap empty() {
        return new PatientRecordEntryDataMap(new HashMap<>());
    }

    public Map<PatientRecordEntryKey, PatientRecordEntryData> getData() {
        return new HashMap<>(data);
    }

    public PatientRecordEntryData getData(PatientRecordEntryKey key) {
        return data.get(key);
    }

    public PatientRecordEntryDataMap addData(PatientRecordEntryKey key, PatientRecordEntryData entryData) {
        Map<PatientRecordEntryKey, PatientRecordEntryData> newData = new HashMap<>(data);
        newData.put(key, entryData);
        return new PatientRecordEntryDataMap(newData);
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
        PatientRecordEntryDataMap that = (PatientRecordEntryDataMap) o;
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