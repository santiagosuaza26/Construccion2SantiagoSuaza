package app.domain.model;

import java.util.HashMap;
import java.util.Map;

public class MedicalRecord {

    private String patientId;
    private Map<String, String> entries; // date → notes/diagnosis

    public MedicalRecord(String patientId) {
        this.patientId = patientId;
        this.entries = new HashMap<>();
    }

    public String getPatientId() {
        return patientId;
    }

    public Map<String, String> getEntries() {
        return entries;
    }

    public void addEntry(String date, String note) {
        entries.put(date, note);
    }
}
