package app.clinic.application.usecase;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.service.UserService;

public class AuthenticateUserUseCase {
    private final UserService userService;

    public AuthenticateUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public User execute(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        // For development: simple password comparison (passwords are stored in plain text)
        if (!user.getPassword().getValue().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }
}