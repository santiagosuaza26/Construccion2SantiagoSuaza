package app.application.usecase;

import app.domain.model.VitalSigns;
import app.domain.services.NurseService;

public class RecordVitalSignsUseCase {
    private final NurseService service;

    public RecordVitalSignsUseCase(NurseService service) {
        this.service = service;
    }

    public void execute(String patientId, String nurseId, VitalSigns signs) {
        service.recordVitalSigns(patientId, nurseId, signs);
    }
}