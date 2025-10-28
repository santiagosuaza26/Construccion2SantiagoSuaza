package app.clinic.application.usecase;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.service.UserService;

public class AuthenticateUserUseCase {
    private final UserService userService;

    public AuthenticateUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public User execute(String username, String password) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        if (!user.getPassword().matches(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }
}