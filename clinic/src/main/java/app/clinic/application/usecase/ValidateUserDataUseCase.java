package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.UserValidationService;

@Service
public class ValidateUserDataUseCase {
    private final UserValidationService userValidationService;

    public ValidateUserDataUseCase(UserValidationService userValidationService) {
        this.userValidationService = userValidationService;
    }

    public void execute(String fullName, String identificationNumber, String email, String phone,
                       String dateOfBirth, String address, String role, String username, String password) {
        userValidationService.validateUserData(fullName, identificationNumber, email, phone,
                                             dateOfBirth, address, role, username, password);
    }
}