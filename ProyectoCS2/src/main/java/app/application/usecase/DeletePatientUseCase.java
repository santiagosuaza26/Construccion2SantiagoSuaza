package app.application.usecase;

import com.clinic.domain.service.AdministrativeService;

public class DeletePatientUseCase {
    private final AdministrativeService service;

    public DeletePatientUseCase(AdministrativeService service) {
        this.service = service;
    }

    public void execute(String idCard) {
        service.removePatient(idCard);
    }
}