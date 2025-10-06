package app.clinic.infrastructure.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.domain.model.User;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserId;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;
import app.clinic.domain.port.UserRepository;

/**
 * Infrastructure service for user management operations.
 * Provides business logic implementation for user-related operations.
 */
@Service
@Transactional
public class UserManagementInfrastructureService {

    private final UserRepository userRepository;
    private final DomainModelMapperService mapperService;

    public UserManagementInfrastructureService(UserRepository userRepository,
                                            DomainModelMapperService mapperService) {
        this.userRepository = userRepository;
        this.mapperService = mapperService;
    }

    /**
     * Creates a new user in the system.
     */
    public User createUser(User user) {
        if (userRepository.existsByCedula(user.getCedula())) {
            throw new IllegalArgumentException("User with cedula " + user.getCedula().getValue() + " already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with username " + user.getUsername().getValue() + " already exists");
        }
        return userRepository.save(user);
    }

    /**
     * Updates an existing user.
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user by ID.
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserById(UserId userId) {
        return userRepository.findById(userId);
    }

    /**
     * Finds a user by cedula.
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserByCedula(UserCedula cedula) {
        return userRepository.findByCedula(cedula);
    }

    /**
     * Finds a user by username.
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(UserUsername username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds all users with a specific role.
     */
    @Transactional(readOnly = true)
    public List<User> findUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Finds all active users.
     */
    @Transactional(readOnly = true)
    public List<User> findAllActiveUsers() {
        return userRepository.findAllActive();
    }

    /**
     * Finds all users.
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by ID.
     */
    public void deleteUserById(UserId userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Deletes a user by cedula.
     */
    public void deleteUserByCedula(UserCedula cedula) {
        userRepository.deleteByCedula(cedula);
    }

    /**
     * Checks if a user exists with the given cedula.
     */
    @Transactional(readOnly = true)
    public boolean userExistsByCedula(UserCedula cedula) {
        return userRepository.existsByCedula(cedula);
    }

    /**
     * Checks if a user exists with the given username.
     */
    @Transactional(readOnly = true)
    public boolean userExistsByUsername(UserUsername username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Counts total number of users.
     */
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    /**
     * Counts users by role.
     */
    @Transactional(readOnly = true)
    public long countUsersByRole(UserRole role) {
        return userRepository.countByRole(role);
    }
}