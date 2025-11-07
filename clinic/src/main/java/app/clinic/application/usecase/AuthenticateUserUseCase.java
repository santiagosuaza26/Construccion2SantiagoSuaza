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
            throw new IllegalArgumentException("Nombre de usuario o contraseña inválidos");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de usuario o contraseña inválidos");
        }

        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Nombre de usuario o contraseña inválidos");
        }
        // For development: simple password comparison (passwords are stored in plain text)
        if (!user.getPassword().getValue().equals(password)) {
            throw new IllegalArgumentException("Nombre de usuario o contraseña inválidos");
        }
        return user;
    }

    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        }
        return userService.findUserByUsername(username);
    }
}