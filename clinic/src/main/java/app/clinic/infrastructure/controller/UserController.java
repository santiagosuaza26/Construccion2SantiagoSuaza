package app.clinic.infrastructure.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.CreateUserUseCase;
import app.clinic.application.usecase.DeleteUserUseCase;
import app.clinic.application.usecase.ListUsersUseCase;
import app.clinic.application.usecase.UpdateUserUseCase;
import app.clinic.domain.model.DomainException;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.RoleBasedAccessService;
import app.clinic.infrastructure.dto.UserDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final RoleBasedAccessService roleBasedAccessService;

    public UserController(CreateUserUseCase createUserUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          DeleteUserUseCase deleteUserUseCase,
                          ListUsersUseCase listUsersUseCase,
                          RoleBasedAccessService roleBasedAccessService) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    private Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            String roleString = authentication.getAuthorities().iterator().next().getAuthority();
            if (roleString.startsWith("ROLE_")) {
                roleString = roleString.substring(5); // Remove "ROLE_" prefix
            }
            return Role.valueOf(roleString);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        // Validar que solo RRHH puede crear usuarios
        Role currentRole = getCurrentUserRole();
        if (currentRole != Role.RECURSOS_HUMANOS) {
            throw new DomainException("Solo Recursos Humanos puede crear usuarios");
        }

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
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request) {
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
        // Validar que solo RRHH puede eliminar usuarios
        Role currentRole = getCurrentUserRole();
        if (currentRole != Role.RECURSOS_HUMANOS) {
            throw new DomainException("Solo Recursos Humanos puede eliminar usuarios");
        }

        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/can-view-patients")
    public ResponseEntity<Map<String, Boolean>> canViewPatients() {
        Role userRole = getCurrentUserRole();
        boolean canView = false;

        if (userRole != null) {
            try {
                roleBasedAccessService.checkAccess(userRole, "patient");
                canView = true;
            } catch (DomainException e) {
                canView = false;
            }
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("canView", canView);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/can-manage-users")
    public ResponseEntity<Map<String, Boolean>> canManageUsers() {
        Role userRole = getCurrentUserRole();
        boolean canManage = false;

        if (userRole != null) {
            try {
                roleBasedAccessService.checkAccess(userRole, "user");
                canManage = true;
            } catch (DomainException e) {
                canManage = false;
            }
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("canManage", canManage);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('RECURSOS_HUMANOS')")
    public ResponseEntity<List<UserDTO>> listUsers() {
        var users = listUsersUseCase.execute();

        var userDTOs = users.stream().map(user -> new UserDTO(
            user.getIdentificationNumber().getValue(),
            user.getFullName(),
            user.getEmail().getValue(),
            user.getPhone().getValue(),
            user.getDateOfBirth().toString(),
            user.getAddress().getValue(),
            user.getRole().toString(),
            user.getCredentials().getUsername().getValue()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/can-register-patients")
    public ResponseEntity<Map<String, Boolean>> canRegisterPatients() {
        Role userRole = getCurrentUserRole();
        boolean canRegister = false;

        if (userRole != null) {
            try {
                roleBasedAccessService.checkAccess(userRole, "patient");
                canRegister = true;
            } catch (DomainException e) {
                canRegister = false;
            }
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("canRegister", canRegister);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RECURSOS_HUMANOS')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        var user = listUsersUseCase.execute().stream()
            .filter(u -> u.getIdentificationNumber().getValue().equals(id))
            .findFirst()
            .orElseThrow(() -> new DomainException("Usuario no encontrado"));

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