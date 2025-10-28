package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.service.UserService;

@Service
public class UpdateUserUseCase {
    private final UserService userService;

    public UpdateUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public User execute(String identificationNumber, String fullName, String email, String phone,
                       String dateOfBirth, String address, String role) {
        userService.updateUser(identificationNumber, fullName, email, phone, dateOfBirth, address, role);
        return userService.findUserById(identificationNumber);
    }
}