package app.clinic.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.UserEntity;

/**
 * JPA repository interface for User entity operations.
 * Provides basic CRUD operations and custom queries for user management.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds a user by their cedula.
     */
    Optional<UserEntity> findByCedula(String cedula);

    /**
     * Finds a user by their username.
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Finds all users with a specific role.
     */
    List<UserEntity> findByRole(UserEntity.UserRole role);

    /**
     * Finds all active users.
     */
    List<UserEntity> findByActiveTrue();

    /**
     * Checks if a user exists with the given cedula.
     */
    boolean existsByCedula(String cedula);

    /**
     * Checks if a user exists with the given username.
     */
    boolean existsByUsername(String username);

    /**
     * Counts users by role.
     */
    long countByRole(UserEntity.UserRole role);

    /**
     * Finds users by role and active status.
     */
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role AND u.active = :active")
    List<UserEntity> findByRoleAndActive(@Param("role") UserEntity.UserRole role, @Param("active") boolean active);
}