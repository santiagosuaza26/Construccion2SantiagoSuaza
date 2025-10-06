package app.clinic.domain.port;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.User;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserId;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;

/**
 * Port interface for user repository operations.
 * Defines the contract for user data access in the domain layer.
 */
public interface UserRepository {

    /**
     * Saves a new user or updates an existing one.
     */
    User save(User user);

    /**
     * Finds a user by their unique identifier.
     */
    Optional<User> findById(UserId userId);

    /**
     * Finds a user by their cedula.
     */
    Optional<User> findByCedula(UserCedula cedula);

    /**
     * Finds a user by their username.
     */
    Optional<User> findByUsername(UserUsername username);

    /**
     * Finds all users with a specific role.
     */
    List<User> findByRole(UserRole role);

    /**
     * Finds all active users.
     */
    List<User> findAllActive();

    /**
     * Finds all users regardless of status.
     */
    List<User> findAll();

    /**
     * Checks if a user exists with the given cedula.
     */
    boolean existsByCedula(UserCedula cedula);

    /**
     * Checks if a user exists with the given username.
     */
    boolean existsByUsername(UserUsername username);

    /**
     * Deletes a user by their identifier.
     */
    void deleteById(UserId userId);

    /**
     * Deletes a user by their cedula.
     */
    void deleteByCedula(UserCedula cedula);

    /**
     * Counts total number of users.
     */
    long count();

    /**
     * Counts users by role.
     */
    long countByRole(UserRole role);
}