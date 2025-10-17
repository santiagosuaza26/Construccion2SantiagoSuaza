package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.domain.model.User;
import app.clinic.domain.model.UserAddress;
import app.clinic.domain.model.UserBirthDate;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserEmail;
import app.clinic.domain.model.UserFullName;
import app.clinic.domain.model.UserPassword;
import app.clinic.domain.model.UserPhoneNumber;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;
import app.clinic.domain.port.UserRepository;

/**
 * Tests unitarios independientes para UserDomainService.
 * Estos tests se ejecutan sin Spring Context y verifican la lógica de negocio pura.
 */
@ExtendWith(MockitoExtension.class)
class UserDomainServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserDomainService userDomainService;

    @BeforeEach
    void setUp() {
        userDomainService = new UserDomainService(userRepository);
    }

    @Test
    @DisplayName("Debe crear usuario cuando los datos son válidos")
    void shouldCreateUserWhenDataIsValid() {
        // Given
        User user = createValidUser();

        // When
        when(userRepository.save(user)).thenReturn(user);
        User result = userDomainService.createUser(user);

        // Then
        assertNotNull(result);
        assertEquals(user.getCedula(), result.getCedula());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("No debe crear usuario cuando la cédula ya existe")
    void shouldNotCreateUserWhenCedulaAlreadyExists() {
        // Given
        User user = createValidUser();

        // When
        when(userRepository.existsByCedula(user.getCedula())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            userDomainService.createUser(user);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("No debe crear usuario cuando el username ya existe")
    void shouldNotCreateUserWhenUsernameAlreadyExists() {
        // Given
        User user = createValidUser();

        // When
        when(userRepository.existsByCedula(user.getCedula())).thenReturn(false);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            userDomainService.createUser(user);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar usuario cuando existe")
    void shouldUpdateUserWhenUserExists() {
        // Given
        User existingUser = createValidUser();
        User updatedUser = createUpdatedUser();

        // When
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userDomainService.updateUser(updatedUser);

        // Then
        assertNotNull(result);
        verify(userRepository).save(updatedUser);
    }

    @Test
    @DisplayName("No debe actualizar usuario cuando no existe")
    void shouldNotUpdateUserWhenUserDoesNotExist() {
        // Given
        User user = createValidUser();

        // When
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            userDomainService.updateUser(user);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe encontrar usuario por cédula")
    void shouldFindUserByCedula() {
        // Given
        UserCedula cedula = UserCedula.of("12345678");
        User user = createValidUser();

        // When
        when(userRepository.findByCedula(cedula)).thenReturn(Optional.of(user));
        Optional<User> result = userDomainService.findUserByCedula(cedula);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user.getCedula(), result.get().getCedula());
        verify(userRepository).findByCedula(cedula);
    }

    @Test
    @DisplayName("Debe activar usuario correctamente")
    void shouldActivateUserCorrectly() {
        // Given
        User inactiveUser = createInactiveUser();

        // When
        when(userRepository.save(any(User.class))).thenReturn(inactiveUser);
        User result = userDomainService.activateUser(inactiveUser);

        // Then
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Debe desactivar usuario correctamente")
    void shouldDeactivateUserCorrectly() {
        // Given
        User activeUser = createValidUser();

        // When
        when(userRepository.save(any(User.class))).thenReturn(activeUser);
        User result = userDomainService.deactivateUser(activeUser);

        // Then
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    // Métodos auxiliares para crear datos de prueba
    private User createValidUser() {
        return User.of(
            UserCedula.of("12345678"),
            UserUsername.of("testuser"),
            UserPassword.of("TestPass123!"),
            UserFullName.of("Test", "User"),
            UserBirthDate.of(LocalDate.of(1990, 1, 1)),
            UserAddress.of("Test Address 123"),
            UserPhoneNumber.of("3001234567"),
            UserEmail.of("test@example.com"),
            UserRole.ADMINISTRATIVE_STAFF
        );
    }

    private User createUpdatedUser() {
        return User.of(
            UserCedula.of("12345678"),
            UserUsername.of("testuser"),
            UserPassword.of("TestPass123!"),
            UserFullName.of("Updated", "User"),
            UserBirthDate.of(LocalDate.of(1990, 1, 1)),
            UserAddress.of("Updated Address 456"),
            UserPhoneNumber.of("3001234567"),
            UserEmail.of("test@example.com"),
            UserRole.ADMINISTRATIVE_STAFF
        );
    }

    private User createInactiveUser() {
        return User.of(
            UserCedula.of("87654321"),
            UserUsername.of("inactiveuser"),
            UserPassword.of("TestPass123!"),
            UserFullName.of("Inactive", "User"),
            UserBirthDate.of(LocalDate.of(1985, 5, 15)),
            UserAddress.of("Inactive Address"),
            UserPhoneNumber.of("3009876543"),
            UserEmail.of("inactive@example.com"),
            UserRole.DOCTOR,
            false // Inactive
        );
    }
}