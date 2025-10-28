package app.clinic.application.usecase;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.service.UserService;

public class GetUserProfileUseCase {
    private final UserService userService;

    public GetUserProfileUseCase(UserService userService) {
        this.userService = userService;
    }

    public User execute(String userId) {
        return userService.findUserById(userId);
    }
}