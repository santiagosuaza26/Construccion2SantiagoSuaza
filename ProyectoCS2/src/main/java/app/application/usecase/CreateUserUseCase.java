package app.application.usecase;

import app.application.dto.UserRequest;
import app.application.dto.UserResponse;
import app.domain.model.User;
import app.domain.services.HumanResourcesService;

public class CreateUserUseCase {
    private final HumanResourcesService service;

    public CreateUserUseCase(HumanResourcesService service) {
        this.service = service;
    }

    public UserResponse execute(UserRequest request) {
        User user = service.createUser(request.toDomain());
        return UserResponse.fromDomain(user);
    }
}