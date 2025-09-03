package app.application.usecase;

import app.domain.model.ProcedureType;
import app.domain.services.SupportService;

public class EnsureProcedureExistsUseCase {
    private final SupportService service;

    public EnsureProcedureExistsUseCase(SupportService service) {
        this.service = service;
    }

    public ProcedureType execute(String id) {
        return service.ensureProcedureExists(id);
    }
}