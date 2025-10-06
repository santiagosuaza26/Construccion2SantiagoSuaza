package app.clinic.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.User;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserId;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;
import app.clinic.domain.port.UserRepository;

/**
 * Domain service for user management operations.
 * Contains business logic for user operations following domain-driven design principles.
 */
@Service
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user with validation.
     */
    public User createUser(User user) {
        validateUserForCreation(user);
        return userRepository.save(user);
    }

    /**
     * Updates an existing user with validation.
     */
    public User updateUser(User user) {
        validateUserForUpdate(user);
        return userRepository.save(user);
    }

    /**
     * Finds a user by ID.
     */
    public Optional<User> findUserById(UserId userId) {
        return userRepository.findById(userId);
    }

    /**
     * Finds a user by cedula.
     */
    public Optional<User> findUserByCedula(UserCedula cedula) {
        return userRepository.findByCedula(cedula);
    }

    /**
     * Finds a user by username.
     */
    public Optional<User> findUserByUsername(UserUsername username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds all users with a specific role.
     */
    public List<User> findUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Finds all active users.
     */
    public List<User> findAllActiveUsers() {
        return userRepository.findAllActive();
    }

    /**
     * Finds all users.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by ID.
     */
    public void deleteUserById(UserId userId) {
        validateUserCanBeDeleted(userId);
        userRepository.deleteById(userId);
    }

    /**
     * Deletes a user by cedula.
     */
    public void deleteUserByCedula(UserCedula cedula) {
        validateUserCanBeDeletedByCedula(cedula);
        userRepository.deleteByCedula(cedula);
    }

    /**
     * Activates a user.
     */
    public User activateUser(User user) {
        User activatedUser = User.of(
            user.getCedula(),
            user.getUsername(),
            user.getPassword(),
            user.getFullName(),
            user.getBirthDate(),
            user.getAddress(),
            user.getPhoneNumber(),
            user.getEmail(),
            user.getRole(),
            true
        );
        return userRepository.save(activatedUser);
    }

    /**
     * Deactivates a user.
     */
    public User deactivateUser(User user) {
        User deactivatedUser = User.of(
            user.getCedula(),
            user.getUsername(),
            user.getPassword(),
            user.getFullName(),
            user.getBirthDate(),
            user.getAddress(),
            user.getPhoneNumber(),
            user.getEmail(),
            user.getRole(),
            false
        );
        return userRepository.save(deactivatedUser);
    }

    /**
     * Validates user data for creation.
     */
    private void validateUserForCreation(User user) {
        if (userRepository.existsByCedula(user.getCedula())) {
            throw new IllegalArgumentException("User with this cedula already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        validateUserRolePermissions(user);
    }

    /**
     * Validates user data for update.
     */
    private void validateUserForUpdate(User user) {
        Optional<User> existingUser = userRepository.findById(extractUserIdFromUser(user));
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User to update does not exist");
        }
        validateUserRolePermissions(user);
    }

    /**
     * Validates that the user can be deleted.
     */
    private void validateUserCanBeDeleted(UserId userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User to delete does not exist");
        }
        // Add additional business rules for user deletion if needed
    }

    /**
     * Validates that the user can be deleted by cedula.
     */
    private void validateUserCanBeDeletedByCedula(UserCedula cedula) {
        Optional<User> user = userRepository.findByCedula(cedula);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User to delete does not exist");
        }
        // Add additional business rules for user deletion if needed
    }

    /**
     * Validates user role permissions.
     */
    private void validateUserRolePermissions(User user) {
        // Add role-specific validation logic here
        if (user.getRole() == UserRole.HUMAN_RESOURCES && !user.getFullName().getFullName().contains("HR")) {
            // Example validation - can be extended based on business rules
        }
    }

    /**
     * Extracts user ID from user object (placeholder for actual implementation).
     */
    private UserId extractUserIdFromUser(User user) {
        // Extract UserId from the user's cedula value
        return UserId.of(user.getCedula().getValue());
    }
}