package app.application.usecase;

import app.domain.services.AdministrativeService;

public class DeletePatientUseCase {
    private final AdministrativeService service;

    public DeletePatientUseCase(AdministrativeService service) {
        this.service = service;
    }

    public void execute(String idCard) {
        service.removePatient(idCard);
    }
}