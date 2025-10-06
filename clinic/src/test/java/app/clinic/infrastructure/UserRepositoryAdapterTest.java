package app.clinic.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.domain.model.User;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserId;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;
import app.clinic.infrastructure.adapter.UserRepositoryAdapter;
import app.clinic.infrastructure.entity.UserEntity;
import app.clinic.infrastructure.repository.UserJpaRepository;

/**
 * Unit tests for UserRepositoryAdapter.
 * Tests the adapter functionality for user repository operations.
 */
@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    private UserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void setUp() {
        userRepositoryAdapter = new UserRepositoryAdapter(userJpaRepository);
    }

    @Test
    void testSaveUser() {
        // Given
        User user = User.of(
                UserCedula.of("12345678"),
                UserUsername.of("testuser"),
                app.clinic.domain.model.UserPassword.ofHashed("hashedpassword"),
                app.clinic.domain.model.UserFullName.of("Test", "User"),
                app.clinic.domain.model.UserBirthDate.of(1990, 1, 1),
                app.clinic.domain.model.UserAddress.of("Test Address"),
                app.clinic.domain.model.UserPhoneNumber.of("1234567890"),
                app.clinic.domain.model.UserEmail.of("test@example.com"),
                UserRole.ADMINISTRATIVE_STAFF,
                true
        );

        UserEntity entity = new UserEntity(
                "12345678", "testuser", "hashedpassword",
                "Test", "User",
                java.time.LocalDate.of(1990, 1, 1),
                "Test Address", "1234567890", "test@example.com",
                UserEntity.UserRole.ADMINISTRATIVE_STAFF, true
        );

        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(entity);

        // When
        User savedUser = userRepositoryAdapter.save(user);

        // Then
        assertNotNull(savedUser);
        assertEquals("12345678", savedUser.getCedula().getValue());
        assertEquals("testuser", savedUser.getUsername().getValue());
        verify(userJpaRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testFindUserById() {
        // Given
        UserId userId = UserId.of("1");
        UserEntity entity = new UserEntity(
                "12345678", "testuser", "hashedpassword",
                "Test", "User",
                java.time.LocalDate.of(1990, 1, 1),
                "Test Address", "1234567890", "test@example.com",
                UserEntity.UserRole.ADMINISTRATIVE_STAFF, true
        );

        when(userJpaRepository.findById(1L)).thenReturn(java.util.Optional.of(entity));

        // When
        java.util.Optional<User> foundUser = userRepositoryAdapter.findById(userId);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("12345678", foundUser.get().getCedula().getValue());
        verify(userJpaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserByCedula() {
        // Given
        UserCedula cedula = UserCedula.of("12345678");
        UserEntity entity = new UserEntity(
                "12345678", "testuser", "hashedpassword",
                "Test", "User",
                java.time.LocalDate.of(1990, 1, 1),
                "Test Address", "1234567890", "test@example.com",
                UserEntity.UserRole.ADMINISTRATIVE_STAFF, true
        );

        when(userJpaRepository.findByCedula("12345678")).thenReturn(java.util.Optional.of(entity));

        // When
        java.util.Optional<User> foundUser = userRepositoryAdapter.findByCedula(cedula);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("12345678", foundUser.get().getCedula().getValue());
        verify(userJpaRepository, times(1)).findByCedula("12345678");
    }

    @Test
    void testUserExistsByCedula() {
        // Given
        UserCedula cedula = UserCedula.of("12345678");
        when(userJpaRepository.existsByCedula("12345678")).thenReturn(true);

        // When
        boolean exists = userRepositoryAdapter.existsByCedula(cedula);

        // Then
        assertTrue(exists);
        verify(userJpaRepository, times(1)).existsByCedula("12345678");
    }

    @Test
    void testDeleteUserById() {
        // Given
        UserId userId = UserId.of("1");

        // When
        userRepositoryAdapter.deleteById(userId);

        // Then
        verify(userJpaRepository, times(1)).deleteById(1L);
    }
}