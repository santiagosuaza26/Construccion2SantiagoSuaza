package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.CreateUserUseCase;
import app.clinic.application.usecase.DeleteUserUseCase;
import app.clinic.application.usecase.UpdateUserUseCase;
import app.clinic.infrastructure.dto.UserDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase,
                         UpdateUserUseCase updateUserUseCase,
                         DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        var user = createUserUseCase.execute(
            request.fullName, request.identificationNumber, request.email,
            request.phone, request.dateOfBirth, request.address,
            request.role, request.username, request.password
        );

        var dto = new UserDTO(
            user.getIdentificationNumber().getValue(),
            user.getFullName(),
            user.getEmail().getValue(),
            user.getPhone().getValue(),
            user.getDateOfBirth().toString(),
            user.getAddress().getValue(),
            user.getRole().toString(),
            user.getCredentials().getUsername().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        var user = updateUserUseCase.execute(
            id, request.fullName, request.email, request.phone,
            request.dateOfBirth, request.address, request.role
        );

        var dto = new UserDTO(
            user.getIdentificationNumber().getValue(),
            user.getFullName(),
            user.getEmail().getValue(),
            user.getPhone().getValue(),
            user.getDateOfBirth().toString(),
            user.getAddress().getValue(),
            user.getRole().toString(),
            user.getCredentials().getUsername().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    public static class CreateUserRequest {
        public String fullName;
        public String identificationNumber;
        public String email;
        public String phone;
        public String dateOfBirth;
        public String address;
        public String role;
        public String username;
        public String password;
    }

    public static class UpdateUserRequest {
        public String fullName;
        public String email;
        public String phone;
        public String dateOfBirth;
        public String address;
        public String role;
    }
}