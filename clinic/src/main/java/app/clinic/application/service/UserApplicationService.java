package app.clinic.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.user.CreateUserDTO;
import app.clinic.application.dto.user.UpdateUserDTO;
import app.clinic.application.dto.user.UserDTO;
import app.clinic.application.mapper.UserMapper;
import app.clinic.domain.model.User;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserId;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;
import app.clinic.domain.service.UserDomainService;

/**
 * Application service for user management operations.
 * Coordinates between REST controllers and domain services.
 * Handles user-related use cases and business operations.
 */
@Service
public class UserApplicationService {

    private final UserDomainService userDomainService;

    public UserApplicationService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    /**
     * Creates a new user based on the provided DTO.
     */
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User user = UserMapper.toDomainEntity(createUserDTO);
        User createdUser = userDomainService.createUser(user);
        return UserMapper.toDTO(createdUser);
    }

    /**
     * Updates an existing user based on the provided DTO.
     */
    public UserDTO updateUser(UpdateUserDTO updateUserDTO) {
        // Find existing user by cedula
        Optional<User> existingUser = userDomainService.findUserByCedula(UserCedula.of(updateUserDTO.getCedula()));

        if (existingUser.isPresent()) {
            User updatedUser = UserMapper.updateEntity(existingUser.get(), updateUserDTO);
            User savedUser = userDomainService.updateUser(updatedUser);
            return UserMapper.toDTO(savedUser);
        } else {
            throw new IllegalArgumentException("User not found with cedula: " + updateUserDTO.getCedula());
        }
    }

    /**
     * Finds a user by their cedula.
     */
    public Optional<UserDTO> findUserByCedula(String cedula) {
        Optional<User> user = userDomainService.findUserByCedula(UserCedula.of(cedula));
        return user.map(UserMapper::toDTO);
    }

    /**
     * Finds a user by their username.
     */
    public Optional<UserDTO> findUserByUsername(String username) {
        Optional<User> user = userDomainService.findUserByUsername(UserUsername.of(username));
        return user.map(UserMapper::toDTO);
    }

    /**
     * Finds a user by their ID.
     */
    public Optional<UserDTO> findUserById(String userId) {
        Optional<User> user = userDomainService.findUserById(UserId.of(userId));
        return user.map(UserMapper::toDTO);
    }

    /**
     * Finds all users with a specific role.
     */
    public List<UserDTO> findUsersByRole(String role) {
        List<User> users = userDomainService.findUsersByRole(UserRole.valueOf(role.toUpperCase()));
        return users.stream()
                  .map(UserMapper::toDTO)
                  .collect(Collectors.toList());
    }

    /**
     * Finds all active users.
     */
    public List<UserDTO> findAllActiveUsers() {
        List<User> users = userDomainService.findAllActiveUsers();
        return users.stream()
                  .map(UserMapper::toDTO)
                  .collect(Collectors.toList());
    }

    /**
     * Finds all users.
     */
    public List<UserDTO> findAllUsers() {
        List<User> users = userDomainService.findAllUsers();
        return users.stream()
                  .map(UserMapper::toDTO)
                  .collect(Collectors.toList());
    }

    /**
     * Deletes a user by their cedula.
     */
    public void deleteUserByCedula(String cedula) {
        userDomainService.deleteUserByCedula(UserCedula.of(cedula));
    }

    /**
     * Deletes a user by their ID.
     */
    public void deleteUserById(String userId) {
        userDomainService.deleteUserById(UserId.of(userId));
    }

    /**
     * Activates a user.
     */
    public UserDTO activateUser(String cedula) {
        Optional<User> user = userDomainService.findUserByCedula(UserCedula.of(cedula));
        if (user.isPresent()) {
            User activatedUser = userDomainService.activateUser(user.get());
            return UserMapper.toDTO(activatedUser);
        } else {
            throw new IllegalArgumentException("User not found with cedula: " + cedula);
        }
    }

    /**
     * Deactivates a user.
     */
    public UserDTO deactivateUser(String cedula) {
        Optional<User> user = userDomainService.findUserByCedula(UserCedula.of(cedula));
        if (user.isPresent()) {
            User deactivatedUser = userDomainService.deactivateUser(user.get());
            return UserMapper.toDTO(deactivatedUser);
        } else {
            throw new IllegalArgumentException("User not found with cedula: " + cedula);
        }
    }

    /**
     * Checks if a user can view patient information based on their role.
     */
    public boolean canViewPatientInfo(String cedula) {
        Optional<User> user = userDomainService.findUserByCedula(UserCedula.of(cedula));
        return user.map(User::canViewPatientInfo).orElse(false);
    }

    /**
     * Checks if a user can manage other users based on their role.
     */
    public boolean canManageUsers(String cedula) {
        Optional<User> user = userDomainService.findUserByCedula(UserCedula.of(cedula));
        return user.map(User::canManageUsers).orElse(false);
    }

    /**
     * Checks if a user can register patients based on their role.
     */
    public boolean canRegisterPatients(String cedula) {
        Optional<User> user = userDomainService.findUserByCedula(UserCedula.of(cedula));
        return user.map(User::canRegisterPatients).orElse(false);
    }
}