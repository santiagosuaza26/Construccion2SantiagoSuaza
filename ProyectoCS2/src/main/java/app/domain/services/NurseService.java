package app.domain.services;

import java.time.LocalDate;
import java.util.Collections;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.VitalSigns;
import app.domain.port.ClinicalHistoryRepository;

public class NurseService {
    private final ClinicalHistoryRepository clinicalHistoryRepository;

    public NurseService(ClinicalHistoryRepository clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    public void recordVitalSigns(String patientIdCard, String nurseIdCard, VitalSigns vitalSigns) {
        ClinicalHistoryEntry entry = new ClinicalHistoryEntry(
                LocalDate.now(),
                nurseIdCard,
                "Nursing vital signs record",
                "",
                "",
                vitalSigns,
                Collections.emptyList()
        );
        clinicalHistoryRepository.saveEntry(patientIdCard, entry);
    }
}