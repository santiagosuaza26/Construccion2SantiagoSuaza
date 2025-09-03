package app.application.usecase;

import app.domain.services.HumanResourcesService;

public class DeleteUserUseCase {
    private final HumanResourcesService service;

    public DeleteUserUseCase(HumanResourcesService service) {
        this.service = service;
    }

    public void execute(String idCard) {
        service.deleteUser(idCard);
    }
}