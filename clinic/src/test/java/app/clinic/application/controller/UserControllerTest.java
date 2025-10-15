package app.clinic.application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.clinic.application.dto.user.CreateUserDTO;
import app.clinic.application.dto.user.UserDTO;
import app.clinic.application.service.UserApplicationService;
import app.clinic.domain.exception.EntityNotFoundException;
import app.clinic.domain.exception.UserAlreadyExistsException;

/**
 * Test unitarios para UserController.
 * Verifica el comportamiento del endpoint de creación de usuarios.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserApplicationService userApplicationService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private CreateUserDTO validCreateUserDTO;
    private UserDTO createdUserDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Configurar datos de prueba válidos
        validCreateUserDTO = new CreateUserDTO();
        validCreateUserDTO.setCedula("12345678");
        validCreateUserDTO.setUsername("testuser");
        validCreateUserDTO.setPassword("TestPass123!");
        validCreateUserDTO.setFullName("Usuario de Prueba");
        validCreateUserDTO.setBirthDate("15/03/1990");
        validCreateUserDTO.setAddress("Calle 123 #45-67");
        validCreateUserDTO.setPhoneNumber("3001234567");
        validCreateUserDTO.setEmail("test@example.com");
        validCreateUserDTO.setRole("DOCTOR");

        // Configurar respuesta esperada
        createdUserDTO = new UserDTO();
        createdUserDTO.setCedula("12345678");
        createdUserDTO.setUsername("testuser");
        createdUserDTO.setFullName("Usuario de Prueba");
        createdUserDTO.setBirthDate("15/03/1990");
        createdUserDTO.setAddress("Calle 123 #45-67");
        createdUserDTO.setPhoneNumber("3001234567");
        createdUserDTO.setEmail("test@example.com");
        createdUserDTO.setRole("DOCTOR");
        createdUserDTO.setActive(true);
        createdUserDTO.setAge(34);
    }

    @Test
    @DisplayName("Debe crear usuario exitosamente con datos válidos")
    void shouldCreateUserSuccessfullyWithValidData() {
        // Given
        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenReturn(createdUserDTO);

        // When
        ResponseEntity<UserDTO> response = userController.createUser(validCreateUserDTO);

        // Then
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().getCedula().equals("12345678");
        assert response.getBody().getUsername().equals("testuser");
        assert response.getBody().getFullName().equals("Usuario de Prueba");
        assert response.getBody().getEmail().equals("test@example.com");
        assert response.getBody().getRole().equals("DOCTOR");
        assert response.getBody().isActive();
        assert response.getBody().getAge() == 34;
    }

    @Test
    @DisplayName("Debe retornar conflicto cuando usuario ya existe")
    void shouldReturnConflictWhenUserAlreadyExists() {
        // Given
        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenThrow(new UserAlreadyExistsException("Usuario ya existe"));

        // When
        ResponseEntity<UserDTO> response = userController.createUser(validCreateUserDTO);

        // Then
        assert response.getStatusCodeValue() == 409; // HTTP CONFLICT
        assert response.getBody() == null;
    }

    @Test
    @DisplayName("Debe retornar bad request para datos inválidos - cédula faltante")
    void shouldReturnBadRequestForInvalidDataMissingCedula() {
        // Given
        CreateUserDTO invalidUserDTO = new CreateUserDTO();
        invalidUserDTO.setCedula(""); // Cédula faltante
        invalidUserDTO.setUsername("testuser");
        invalidUserDTO.setPassword("TestPass123!");
        invalidUserDTO.setFullName("Usuario de Prueba");
        invalidUserDTO.setBirthDate("15/03/1990");
        invalidUserDTO.setAddress("Calle 123 #45-67");
        invalidUserDTO.setPhoneNumber("3001234567");
        invalidUserDTO.setEmail("test@example.com");
        invalidUserDTO.setRole("DOCTOR");

        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenReturn(null); // Servicio retorna null para datos inválidos

        // When
        ResponseEntity<UserDTO> response = userController.createUser(invalidUserDTO);

        // Then
        assert response.getStatusCodeValue() == 400; // HTTP BAD REQUEST
        assert response.getBody() == null;
    }

    @Test
    @DisplayName("Debe retornar bad request para datos inválidos - email inválido")
    void shouldReturnBadRequestForInvalidDataInvalidEmail() {
        // Given
        CreateUserDTO invalidUserDTO = new CreateUserDTO();
        invalidUserDTO.setCedula("12345678");
        invalidUserDTO.setUsername("testuser");
        invalidUserDTO.setPassword("TestPass123!");
        invalidUserDTO.setFullName("Usuario de Prueba");
        invalidUserDTO.setBirthDate("15/03/1990");
        invalidUserDTO.setAddress("Calle 123 #45-67");
        invalidUserDTO.setPhoneNumber("3001234567");
        invalidUserDTO.setEmail("email-invalido"); // Email inválido
        invalidUserDTO.setRole("DOCTOR");

        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenReturn(null); // Servicio retorna null para datos inválidos

        // When
        ResponseEntity<UserDTO> response = userController.createUser(invalidUserDTO);

        // Then
        assert response.getStatusCodeValue() == 400; // HTTP BAD REQUEST
        assert response.getBody() == null;
    }

    @Test
    @DisplayName("Debe retornar bad request para datos inválidos - contraseña débil")
    void shouldReturnBadRequestForInvalidDataWeakPassword() {
        // Given
        CreateUserDTO invalidUserDTO = new CreateUserDTO();
        invalidUserDTO.setCedula("12345678");
        invalidUserDTO.setUsername("testuser");
        invalidUserDTO.setPassword("weak"); // Contraseña débil
        invalidUserDTO.setFullName("Usuario de Prueba");
        invalidUserDTO.setBirthDate("15/03/1990");
        invalidUserDTO.setAddress("Calle 123 #45-67");
        invalidUserDTO.setPhoneNumber("3001234567");
        invalidUserDTO.setEmail("test@example.com");
        invalidUserDTO.setRole("DOCTOR");

        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenReturn(null); // Servicio retorna null para datos inválidos

        // When
        ResponseEntity<UserDTO> response = userController.createUser(invalidUserDTO);

        // Then
        assert response.getStatusCodeValue() == 400; // HTTP BAD REQUEST
        assert response.getBody() == null;
    }

    @Test
    @DisplayName("Debe retornar bad request para datos inválidos - rol inválido")
    void shouldReturnBadRequestForInvalidDataInvalidRole() {
        // Given
        CreateUserDTO invalidUserDTO = new CreateUserDTO();
        invalidUserDTO.setCedula("12345678");
        invalidUserDTO.setUsername("testuser");
        invalidUserDTO.setPassword("TestPass123!");
        invalidUserDTO.setFullName("Usuario de Prueba");
        invalidUserDTO.setBirthDate("15/03/1990");
        invalidUserDTO.setAddress("Calle 123 #45-67");
        invalidUserDTO.setPhoneNumber("3001234567");
        invalidUserDTO.setEmail("test@example.com");
        invalidUserDTO.setRole("INVALID_ROLE"); // Rol inválido

        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenReturn(null); // Servicio retorna null para datos inválidos

        // When
        ResponseEntity<UserDTO> response = userController.createUser(invalidUserDTO);

        // Then
        assert response.getStatusCodeValue() == 400; // HTTP BAD REQUEST
        assert response.getBody() == null;
    }

    @Test
    @DisplayName("Debe retornar bad request para datos inválidos - formato de fecha incorrecto")
    void shouldReturnBadRequestForInvalidDataWrongDateFormat() {
        // Given
        CreateUserDTO invalidUserDTO = new CreateUserDTO();
        invalidUserDTO.setCedula("12345678");
        invalidUserDTO.setUsername("testuser");
        invalidUserDTO.setPassword("TestPass123!");
        invalidUserDTO.setFullName("Usuario de Prueba");
        invalidUserDTO.setBirthDate("1990-03-15"); // Formato incorrecto (debe ser DD/MM/YYYY)
        invalidUserDTO.setAddress("Calle 123 #45-67");
        invalidUserDTO.setPhoneNumber("3001234567");
        invalidUserDTO.setEmail("test@example.com");
        invalidUserDTO.setRole("DOCTOR");

        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenReturn(null); // Servicio retorna null para datos inválidos

        // When
        ResponseEntity<UserDTO> response = userController.createUser(invalidUserDTO);

        // Then
        assert response.getStatusCodeValue() == 400; // HTTP BAD REQUEST
        assert response.getBody() == null;
    }

    @Test
    @DisplayName("Debe retornar bad request cuando servicio lanza excepción genérica")
    void shouldReturnBadRequestWhenServiceThrowsGenericException() {
        // Given
        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenThrow(new RuntimeException("Error interno"));

        // When
        ResponseEntity<UserDTO> response = userController.createUser(validCreateUserDTO);

        // Then
        assert response.getStatusCodeValue() == 400; // HTTP BAD REQUEST
        assert response.getBody() == null;
    }

    @Test
    @DisplayName("Debe manejar correctamente caracteres especiales en nombres")
    void shouldHandleSpecialCharactersInNamesCorrectly() {
        // Given
        CreateUserDTO userWithSpecialChars = new CreateUserDTO();
        userWithSpecialChars.setCedula("87654321");
        userWithSpecialChars.setUsername("testuser2");
        userWithSpecialChars.setPassword("TestPass123!");
        userWithSpecialChars.setFullName("José María García-López");
        userWithSpecialChars.setBirthDate("20/05/1985");
        userWithSpecialChars.setAddress("Avenida José María #123");
        userWithSpecialChars.setPhoneNumber("3009876543");
        userWithSpecialChars.setEmail("josemaria@example.com");
        userWithSpecialChars.setRole("NURSE");

        UserDTO expectedResponse = new UserDTO();
        expectedResponse.setCedula("87654321");
        expectedResponse.setUsername("testuser2");
        expectedResponse.setFullName("José María García-López");
        expectedResponse.setBirthDate("20/05/1985");
        expectedResponse.setAddress("Avenida José María #123");
        expectedResponse.setPhoneNumber("3009876543");
        expectedResponse.setEmail("josemaria@example.com");
        expectedResponse.setRole("NURSE");
        expectedResponse.setActive(true);
        expectedResponse.setAge(39);

        when(userApplicationService.createUser(any(CreateUserDTO.class)))
            .thenReturn(expectedResponse);

        // When
        ResponseEntity<UserDTO> response = userController.createUser(userWithSpecialChars);

        // Then
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().getCedula().equals("87654321");
        assert response.getBody().getFullName().equals("José María García-López");
        assert response.getBody().getRole().equals("NURSE");
        assert response.getBody().getAge() == 39;
    }
}