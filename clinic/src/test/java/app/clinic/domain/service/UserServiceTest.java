package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Password;
import app.clinic.domain.model.valueobject.Username;
import app.clinic.domain.repository.UserRepository;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidationService validationService;

    @Mock
    private RoleBasedAccessService roleBasedAccessService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, validationService, roleBasedAccessService);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        String fullName = "John Doe";
        String identificationNumber = "123456789";
        String email = "john@example.com";
        String phone = "1234567890";
        String dateOfBirth = "01/01/1990";
        String address = "123 Main St";
        String role = "MEDICO";
        String username = "johndoe";
        String password = "Password123!";

        when(userRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(false);

        // When
        User user = userService.createUser(fullName, identificationNumber, email, phone, dateOfBirth, address, role, username, password);

        // Then
        assertNotNull(user);
        verify(userRepository).save(user);
        verify(validationService).validateCredentialsUniqueness(any(Credentials.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Given
        doThrow(new IllegalArgumentException("Identification number already exists")).when(validationService).validateUserData(any(), any(), any(), any(), any(), any(), any(), any(), any());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("John Doe", "123456789", "john@example.com", "1234567890", "01/01/1990", "123 Main St", "MEDICO", "johndoe", "Password123!"));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Given
        User existingUser = mock(User.class);
        when(existingUser.getCredentials()).thenReturn(new Credentials(new Username("johndoe"), new Password("Password123!")));
        when(userRepository.findByIdentificationNumber(any(Id.class))).thenReturn(java.util.Optional.of(existingUser));

        // When
        userService.updateUser("123456789", "Jane Doe", "jane@example.com", "0987654321", "01/01/1990", "456 Oak St", "ENFERMERA");

        // Then
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Given
        when(userRepository.findByIdentificationNumber(any(Id.class))).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser("123456789", "Jane Doe", "jane@example.com", "0987654321", "01/01/1990", "456 Oak St", "ENFERMERA"));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);

        // When
        userService.deleteUser("123456789");

        // Then
        verify(userRepository).deleteByIdentificationNumber(any(Id.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Given
        when(userRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser("123456789"));
    }
}