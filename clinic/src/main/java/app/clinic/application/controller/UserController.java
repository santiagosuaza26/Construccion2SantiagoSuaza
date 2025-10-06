package app.clinic.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.user.CreateUserDTO;
import app.clinic.application.dto.user.UpdateUserDTO;
import app.clinic.application.dto.user.UserDTO;
import app.clinic.application.service.UserApplicationService;
import jakarta.validation.Valid;

/**
 * REST Controller for user management operations.
 * Provides HTTP endpoints for creating, reading, updating, and deleting users.
 * Handles role-based access control and user permissions.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    /**
     * Creates a new user.
     * Only accessible by Human Resources role.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        UserDTO createdUser = userApplicationService.createUser(createUserDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Updates an existing user.
     * Only accessible by Human Resources role.
     */
    @PutMapping("/{cedula}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String cedula,
                                             @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        // Ensure the cedula in path matches the one in request body
        if (!cedula.equals(updateUserDTO.getCedula())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDTO updatedUser = userApplicationService.updateUser(updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Finds a user by their cedula.
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<UserDTO> findUserByCedula(@PathVariable String cedula) {
        return userApplicationService.findUserByCedula(cedula)
                .map(user -> ResponseEntity.ok(user))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds a user by their username.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username) {
        return userApplicationService.findUserByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds a user by their ID.
     */
    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String userId) {
        return userApplicationService.findUserById(userId)
                .map(user -> ResponseEntity.ok(user))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds all users with a specific role.
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> findUsersByRole(@PathVariable String role) {
        List<UserDTO> users = userApplicationService.findUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Finds all active users.
     */
    @GetMapping("/active")
    public ResponseEntity<List<UserDTO>> findAllActiveUsers() {
        List<UserDTO> users = userApplicationService.findAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Finds all users.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = userApplicationService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Deletes a user by their cedula.
     * Only accessible by Human Resources role.
     */
    @DeleteMapping("/cedula/{cedula}")
    public ResponseEntity<Void> deleteUserByCedula(@PathVariable String cedula) {
        userApplicationService.deleteUserByCedula(cedula);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a user by their ID.
     * Only accessible by Human Resources role.
     */
    @DeleteMapping("/id/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String userId) {
        userApplicationService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Activates a user.
     * Only accessible by Human Resources role.
     */
    @PutMapping("/{cedula}/activate")
    public ResponseEntity<UserDTO> activateUser(@PathVariable String cedula) {
        UserDTO activatedUser = userApplicationService.activateUser(cedula);
        return ResponseEntity.ok(activatedUser);
    }

    /**
     * Deactivates a user.
     * Only accessible by Human Resources role.
     */
    @PutMapping("/{cedula}/deactivate")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable String cedula) {
        UserDTO deactivatedUser = userApplicationService.deactivateUser(cedula);
        return ResponseEntity.ok(deactivatedUser);
    }

    /**
     * Checks if a user can view patient information.
     */
    @GetMapping("/{cedula}/can-view-patients")
    public ResponseEntity<Boolean> canViewPatientInfo(@PathVariable String cedula) {
        boolean canView = userApplicationService.canViewPatientInfo(cedula);
        return ResponseEntity.ok(canView);
    }

    /**
     * Checks if a user can manage other users.
     */
    @GetMapping("/{cedula}/can-manage-users")
    public ResponseEntity<Boolean> canManageUsers(@PathVariable String cedula) {
        boolean canManage = userApplicationService.canManageUsers(cedula);
        return ResponseEntity.ok(canManage);
    }

    /**
     * Checks if a user can register patients.
     */
    @GetMapping("/{cedula}/can-register-patients")
    public ResponseEntity<Boolean> canRegisterPatients(@PathVariable String cedula) {
        boolean canRegister = userApplicationService.canRegisterPatients(cedula);
        return ResponseEntity.ok(canRegister);
    }
}