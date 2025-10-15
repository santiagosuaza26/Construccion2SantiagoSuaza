package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.domain.model.*;
import app.clinic.domain.port.UserRepository;

/**
 * Comprehensive unit tests for UserDomainService.
 * Tests all business logic and validation rules for user management.
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
    @DisplayName("Should create user successfully with valid data")
    void shouldCreateUserSuccessfullyWithValidData() {
        // Given
        User user = createValidUser();

        when(userRepository.existsByCedula(any(UserCedula.class))).thenReturn(false);
        when(userRepository.existsByUsername(any(UserUsername.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userDomainService.createUser(user);

        // Then
        assertNotNull(result);
        verify(userRepository).existsByCedula(user.getCedula());
        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing cedula")
    void shouldThrowExceptionWhenCreatingUserWithExistingCedula() {
        // Given
        User user = createValidUser();

        when(userRepository.existsByCedula(any(UserCedula.class))).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userDomainService.createUser(user)
        );

        assertEquals("User with this cedula already exists", exception.getMessage());
        verify(userRepository).existsByCedula(user.getCedula());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing username")
    void shouldThrowExceptionWhenCreatingUserWithExistingUsername() {
        // Given
        User user = createValidUser();

        when(userRepository.existsByCedula(any(UserCedula.class))).thenReturn(false);
        when(userRepository.existsByUsername(any(UserUsername.class))).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userDomainService.createUser(user)
        );

        assertEquals("User with this username already exists", exception.getMessage());
        verify(userRepository).existsByCedula(user.getCedula());
        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        User user = createValidUser();
        User existingUser = createValidUser();

        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userDomainService.updateUser(user);

        // Then
        assertNotNull(result);
        verify(userRepository).findById(any(UserId.class));
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Given
        User user = createValidUser();

        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userDomainService.updateUser(user)
        );

        assertEquals("User to update does not exist", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void shouldFindUserByIdSuccessfully() {
        // Given
        UserId userId = UserId.of("test-id");
        User user = createValidUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userDomainService.findUserById(userId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should find user by cedula successfully")
    void shouldFindUserByCedulaSuccessfully() {
        // Given
        UserCedula cedula = UserCedula.of("12345678");
        User user = createValidUser();

        when(userRepository.findByCedula(cedula)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userDomainService.findUserByCedula(cedula);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findByCedula(cedula);
    }

    @Test
    @DisplayName("Should find user by username successfully")
    void shouldFindUserByUsernameSuccessfully() {
        // Given
        UserUsername username = UserUsername.of("testuser");
        User user = createValidUser();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userDomainService.findUserByUsername(username);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Should find users by role successfully")
    void shouldFindUsersByRoleSuccessfully() {
        // Given
        UserRole role = UserRole.DOCTOR;
        List<User> users = Arrays.asList(createValidUser(), createValidUser());

        when(userRepository.findByRole(role)).thenReturn(users);

        // When
        List<User> result = userDomainService.findUsersByRole(role);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findByRole(role);
    }

    @Test
    @DisplayName("Should find all active users successfully")
    void shouldFindAllActiveUsersSuccessfully() {
        // Given
        List<User> users = Arrays.asList(createValidUser(), createValidUser());
        when(userRepository.findAllActive()).thenReturn(users);

        // When
        List<User> result = userDomainService.findAllActiveUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAllActive();
    }

    @Test
    @DisplayName("Should find all users successfully")
    void shouldFindAllUsersSuccessfully() {
        // Given
        List<User> users = Arrays.asList(createValidUser(), createValidUser());
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userDomainService.findAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should delete user by ID successfully")
    void shouldDeleteUserByIdSuccessfully() {
        // Given
        UserId userId = UserId.of("test-id");
        User user = createValidUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        // When & Then
        assertDoesNotThrow(() -> userDomainService.deleteUserById(userId));

        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Given
        UserId userId = UserId.of("non-existent-id");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userDomainService.deleteUserById(userId)
        );

        assertEquals("User to delete does not exist", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should activate user successfully")
    void shouldActivateUserSuccessfully() {
        // Given
        User inactiveUser = createInactiveUser();
        User expectedActiveUser = createValidUser();

        when(userRepository.save(any(User.class))).thenReturn(expectedActiveUser);

        // When
        User result = userDomainService.activateUser(inactiveUser);

        // Then
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void shouldDeactivateUserSuccessfully() {
        // Given
        User activeUser = createValidUser();
        User expectedInactiveUser = createInactiveUser();

        when(userRepository.save(any(User.class))).thenReturn(expectedInactiveUser);

        // When
        User result = userDomainService.deactivateUser(activeUser);

        // Then
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    // Helper methods
    private User createValidUser() {
        return mock(User.class);
    }

    private User createInactiveUser() {
        return mock(User.class);
    }
}