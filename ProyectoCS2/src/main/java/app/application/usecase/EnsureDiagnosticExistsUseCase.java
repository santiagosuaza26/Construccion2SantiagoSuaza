package app.application.usecase;

import app.domain.model.DiagnosticTest;
import app.domain.services.SupportService;

public class EnsureDiagnosticExistsUseCase {
    private final SupportService service;

    public EnsureDiagnosticExistsUseCase(SupportService service) {
        this.service = service;
    }

    public DiagnosticTest execute(String id) {
        return service.ensureDiagnosticExists(id);
    }
}