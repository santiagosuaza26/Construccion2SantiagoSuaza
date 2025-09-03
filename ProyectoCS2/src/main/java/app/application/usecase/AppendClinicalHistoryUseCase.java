package app.application.usecase;


import app.domain.model.ClinicalHistoryEntry;
import app.domain.services.DoctorService;

public class AppendClinicalHistoryUseCase {
    private final DoctorService service;

    public AppendClinicalHistoryUseCase(DoctorService service) {
        this.service = service;
    }

    public void execute(String patientId, ClinicalHistoryEntry entry) {
        service.appendClinicalHistory(patientId, entry);
    }
}