package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.service.UserService;

@Service
public class CreateUserUseCase {
    private final UserService userService;

    public CreateUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public User execute(String fullName, String identificationNumber, String email, String phone,
                       String dateOfBirth, String address, String role, String username, String password) {
        return userService.createUser(fullName, identificationNumber, email, phone,
                                    dateOfBirth, address, role, username, password);
    }
}