package app.application.usecase;

import app.domain.model.Medication;
import app.domain.services.SupportService;

public class EnsureMedicationExistsUseCase {
    private final SupportService service;

    public EnsureMedicationExistsUseCase(SupportService service) {
        this.service = service;
    }

    public Medication execute(String id) {
        return service.ensureMedicationExists(id);
    }
}
