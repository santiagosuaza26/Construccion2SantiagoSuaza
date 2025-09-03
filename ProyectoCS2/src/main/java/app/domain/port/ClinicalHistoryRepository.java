package app.domain.port;

import java.util.List;

import app.domain.model.ClinicalHistoryEntry;

public interface ClinicalHistoryRepository {
    void saveEntry(String patientIdCard, ClinicalHistoryEntry entry);
    List<ClinicalHistoryEntry> findByPatient(String patientIdCard);
}
