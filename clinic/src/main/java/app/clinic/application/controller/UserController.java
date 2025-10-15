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
import app.clinic.domain.exception.EntityNotFoundException;
import app.clinic.domain.exception.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * REST Controller for user management operations.
  * Provides HTTP endpoints for creating, reading, updating, and deleting users.
  * Handles role-based access control and user permissions.
  *
  * @author Sistema de Clínica
  * @version 2.0
  */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users in the clinic system")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // HTTP Status Code Constants
    private static final HttpStatus STATUS_CREATED = HttpStatus.CREATED;
    private static final HttpStatus STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus STATUS_NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus STATUS_CONFLICT = HttpStatus.CONFLICT;


    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    /**
     * Creates a new user.
     * Only accessible by Human Resources role.
     *
     * @param createUserDTO The user data to create
     * @return ResponseEntity with created user and location header
     * @throws UserAlreadyExistsException if user already exists
     */
    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user in the system with the provided information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            log.info("Creating new user with cedula: {}", createUserDTO.getCedula());
            UserDTO createdUser = userApplicationService.createUser(createUserDTO);

            if (createdUser == null) {
                log.error("UserApplicationService returned null for created user");
                return ResponseEntity.status(STATUS_BAD_REQUEST).body(null);
            }
            log.info("User created successfully with ID: {}", createdUser.getCedula());
            return ResponseEntity
                    .status(STATUS_CREATED)
                    .body(createdUser);

        } catch (UserAlreadyExistsException e) {
            log.warn("Attempted to create user that already exists: {}", createUserDTO.getCedula());
            return ResponseEntity
                    .status(STATUS_CONFLICT)
                    .body(null);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(STATUS_BAD_REQUEST)
                    .body(null);
        }
    }

    /**
     * Updates an existing user.
     * Only accessible by Human Resources role.
     *
     * @param cedula The cedula of the user to update
     * @param updateUserDTO The updated user data
     * @return ResponseEntity with updated user
     * @throws EntityNotFoundException if user not found
     */
    @PutMapping("/{cedula}")
    @Operation(
        summary = "Update an existing user",
        description = "Updates user information for the specified cedula"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Cedula mismatch or invalid data"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {

        // Ensure the cedula in path matches the one in request body
        if (!cedula.equals(updateUserDTO.getCedula())) {
            log.warn("Cedula mismatch: path={}, body={}", cedula, updateUserDTO.getCedula());
            return ResponseEntity
                    .status(STATUS_BAD_REQUEST)
                    .body(null);
        }

        try {
            log.info("Updating user with cedula: {}", cedula);
            UserDTO updatedUser = userApplicationService.updateUser(updateUserDTO);

            log.info("User updated successfully: {}", cedula);
            return ResponseEntity.ok(updatedUser);

        } catch (EntityNotFoundException e) {
            log.warn("User not found for update: {}", cedula);
            return ResponseEntity
                    .status(STATUS_NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            log.error("Error updating user {}: {}", cedula, e.getMessage(), e);
            return ResponseEntity
                    .status(STATUS_BAD_REQUEST)
                    .body(null);
        }
    }

    /**
     * Finds a user by their cedula.
     *
     * @param cedula The cedula to search for
     * @return ResponseEntity with user data or 404 if not found
     */
    @GetMapping("/cedula/{cedula}")
    @Operation(
        summary = "Find user by cedula",
        description = "Retrieves user information using their cedula number"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> findUserByCedula(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula) {

        return userApplicationService.findUserByCedula(cedula)
                .map(user -> {
                    log.debug("User found by cedula: {}", cedula);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for
     * @return ResponseEntity with user data or 404 if not found
     */
    @GetMapping("/username/{username}")
    @Operation(
        summary = "Find user by username",
        description = "Retrieves user information using their username"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> findUserByUsername(
            @Parameter(description = "Username", required = true) @PathVariable String username) {

        return userApplicationService.findUserByUsername(username)
                .map(user -> {
                    log.debug("User found by username: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId The user ID to search for
     * @return ResponseEntity with user data or 404 if not found
     */
    @GetMapping("/id/{userId}")
    @Operation(
        summary = "Find user by ID",
        description = "Retrieves user information using their system ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> findUserById(
            @Parameter(description = "User ID", required = true) @PathVariable String userId) {

        return userApplicationService.findUserById(userId)
                .map(user -> {
                    log.debug("User found by ID: {}", userId);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Finds all users with a specific role with pagination support.
     *
     * @param role The role to filter by
     * @param page Page number (0-based, default: 0)
     * @param size Page size (default: 20, max: 100)
     * @param sortBy Field to sort by (default: cedula)
     * @param sortDir Sort direction (asc/desc, default: asc)
     * @return ResponseEntity with paginated user list
     */
    @GetMapping("/role/{role}")
    @Operation(
        summary = "Find users by role",
        description = "Retrieves all users with a specific role with pagination support"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public ResponseEntity<List<UserDTO>> findUsersByRole(
            @Parameter(description = "User role", required = true) @PathVariable String role) {

        log.debug("Finding users by role: {}", role);
        List<UserDTO> users = userApplicationService.findUsersByRole(role);

        return ResponseEntity.ok(users);
    }

    /**
     * Finds all active users with pagination support.
     *
     * @param page Page number (0-based, default: 0)
     * @param size Page size (default: 20, max: 100)
     * @param sortBy Field to sort by (default: cedula)
     * @param sortDir Sort direction (asc/desc, default: asc)
     * @return ResponseEntity with paginated active user list
     */
    @GetMapping("/active")
    @Operation(
        summary = "Find all active users",
        description = "Retrieves all active users with pagination support"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active users retrieved successfully")
    })
    public ResponseEntity<List<UserDTO>> findAllActiveUsers() {

        log.debug("Finding all active users");
        List<UserDTO> users = userApplicationService.findAllActiveUsers();

        return ResponseEntity.ok(users);
    }

    /**
     * Finds all users with pagination support.
     *
     * @param page Page number (0-based, default: 0)
     * @param size Page size (default: 20, max: 100)
     * @param sortBy Field to sort by (default: cedula)
     * @param sortDir Sort direction (asc/desc, default: asc)
     * @return ResponseEntity with paginated user list
     */
    @GetMapping
    @Operation(
        summary = "Find all users",
        description = "Retrieves all users with pagination support"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public ResponseEntity<List<UserDTO>> findAllUsers() {

        log.debug("Finding all users");
        List<UserDTO> users = userApplicationService.findAllUsers();

        return ResponseEntity.ok(users);
    }

    /**
     * Deletes a user by their cedula.
     * Only accessible by Human Resources role.
     *
     * @param cedula The cedula of the user to delete
     * @return ResponseEntity with no content
     * @throws EntityNotFoundException if user not found
     */
    @DeleteMapping("/cedula/{cedula}")
    @Operation(
        summary = "Delete user by cedula",
        description = "Soft deletes a user by their cedula number"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUserByCedula(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula) {

        try {
            log.info("Deleting user by cedula: {}", cedula);
            userApplicationService.deleteUserByCedula(cedula);

            log.info("User deleted successfully: {}", cedula);
            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException e) {
            log.warn("User not found for deletion: {}", cedula);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting user {}: {}", cedula, e.getMessage(), e);
            return ResponseEntity.status(STATUS_BAD_REQUEST).build();
        }
    }

    /**
     * Deletes a user by their ID.
     * Only accessible by Human Resources role.
     *
     * @param userId The ID of the user to delete
     * @return ResponseEntity with no content
     * @throws EntityNotFoundException if user not found
     */
    @DeleteMapping("/id/{userId}")
    @Operation(
        summary = "Delete user by ID",
        description = "Soft deletes a user by their system ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUserById(
            @Parameter(description = "User ID", required = true) @PathVariable String userId) {

        try {
            log.info("Deleting user by ID: {}", userId);
            userApplicationService.deleteUserById(userId);

            log.info("User deleted successfully: {}", userId);
            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException e) {
            log.warn("User not found for deletion: {}", userId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(STATUS_BAD_REQUEST).build();
        }
    }

    /**
     * Activates a user.
     * Only accessible by Human Resources role.
     *
     * @param cedula The cedula of the user to activate
     * @return ResponseEntity with activated user data
     * @throws EntityNotFoundException if user not found
     */
    @PutMapping("/{cedula}/activate")
    @Operation(
        summary = "Activate user",
        description = "Activates a user account by their cedula"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User activated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> activateUser(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula) {

        try {
            log.info("Activating user: {}", cedula);
            UserDTO activatedUser = userApplicationService.activateUser(cedula);

            log.info("User activated successfully: {}", cedula);
            return ResponseEntity.ok(activatedUser);

        } catch (EntityNotFoundException e) {
            log.warn("User not found for activation: {}", cedula);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error activating user {}: {}", cedula, e.getMessage(), e);
            return ResponseEntity.status(STATUS_BAD_REQUEST).build();
        }
    }

    /**
     * Deactivates a user.
     * Only accessible by Human Resources role.
     *
     * @param cedula The cedula of the user to deactivate
     * @return ResponseEntity with deactivated user data
     * @throws EntityNotFoundException if user not found
     */
    @PutMapping("/{cedula}/deactivate")
    @Operation(
        summary = "Deactivate user",
        description = "Deactivates a user account by their cedula"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> deactivateUser(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula) {

        try {
            log.info("Deactivating user: {}", cedula);
            UserDTO deactivatedUser = userApplicationService.deactivateUser(cedula);

            log.info("User deactivated successfully: {}", cedula);
            return ResponseEntity.ok(deactivatedUser);

        } catch (EntityNotFoundException e) {
            log.warn("User not found for deactivation: {}", cedula);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deactivating user {}: {}", cedula, e.getMessage(), e);
            return ResponseEntity.status(STATUS_BAD_REQUEST).build();
        }
    }

    /**
     * Checks if a user can view patient information.
     *
     * @param cedula The cedula of the user to check
     * @return ResponseEntity with boolean indicating permission
     */
    @GetMapping("/{cedula}/can-view-patients")
    @Operation(
        summary = "Check if user can view patient information",
        description = "Verifies if a user has permission to view patient data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission checked successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Boolean> canViewPatientInfo(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula) {

        try {
            boolean canView = userApplicationService.canViewPatientInfo(cedula);
            log.debug("Permission check for viewing patients - User: {}, Can view: {}", cedula, canView);
            return ResponseEntity.ok(canView);

        } catch (EntityNotFoundException e) {
            log.warn("User not found for permission check: {}", cedula);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error checking view patient permission for user {}: {}", cedula, e.getMessage(), e);
            return ResponseEntity.status(STATUS_BAD_REQUEST).build();
        }
    }

    /**
     * Checks if a user can manage other users.
     *
     * @param cedula The cedula of the user to check
     * @return ResponseEntity with boolean indicating permission
     */
    @GetMapping("/{cedula}/can-manage-users")
    @Operation(
        summary = "Check if user can manage other users",
        description = "Verifies if a user has permission to manage other users"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission checked successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Boolean> canManageUsers(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula) {

        try {
            boolean canManage = userApplicationService.canManageUsers(cedula);
            log.debug("Permission check for managing users - User: {}, Can manage: {}", cedula, canManage);
            return ResponseEntity.ok(canManage);

        } catch (EntityNotFoundException e) {
            log.warn("User not found for permission check: {}", cedula);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error checking manage users permission for user {}: {}", cedula, e.getMessage(), e);
            return ResponseEntity.status(STATUS_BAD_REQUEST).build();
        }
    }

    /**
     * Checks if a user can register patients.
     *
     * @param cedula The cedula of the user to check
     * @return ResponseEntity with boolean indicating permission
     */
    @GetMapping("/{cedula}/can-register-patients")
    @Operation(
        summary = "Check if user can register patients",
        description = "Verifies if a user has permission to register new patients"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission checked successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Boolean> canRegisterPatients(
            @Parameter(description = "User cedula", required = true) @PathVariable String cedula) {

        try {
            boolean canRegister = userApplicationService.canRegisterPatients(cedula);
            log.debug("Permission check for registering patients - User: {}, Can register: {}", cedula, canRegister);
            return ResponseEntity.ok(canRegister);

        } catch (EntityNotFoundException e) {
            log.warn("User not found for permission check: {}", cedula);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error checking register patients permission for user {}: {}", cedula, e.getMessage(), e);
            return ResponseEntity.status(STATUS_BAD_REQUEST).build();
        }
    }

}