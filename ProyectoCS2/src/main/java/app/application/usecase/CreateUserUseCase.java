package app.application.usecase;

import com.clinic.application.dto.UserRequest;
import com.clinic.application.dto.UserResponse;
import com.clinic.domain.model.User;
import com.clinic.domain.service.HumanResourcesService;

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