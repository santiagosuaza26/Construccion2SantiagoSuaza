package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.service.UserService;

@Service
public class ListUsersUseCase {
    private final UserService userService;

    public ListUsersUseCase(UserService userService) {
        this.userService = userService;
    }

    public List<User> execute() {
        return userService.getAllUsers();
    }
}