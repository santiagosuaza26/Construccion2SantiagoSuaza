package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.UserService;

@Service
public class DeleteUserUseCase {
    private final UserService userService;

    public DeleteUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public void execute(String identificationNumber) {
        userService.deleteUser(identificationNumber);
    }
}