package app.clinic.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.domain.model.Patient;
import app.clinic.domain.service.PatientDomainService;

/**
 * Tests de integración para casos de uso de aplicación.
 * Estos tests verifican la coordinación entre capas.
 */
@ExtendWith(MockitoExtension.class)
class RegisterPatientUseCaseTest {

    @Mock
    private PatientDomainService patientDomainService;

    private RegisterPatientUseCase registerPatientUseCase;

    @BeforeEach
    void setUp() {
        registerPatientUseCase = new RegisterPatientUseCase(patientDomainService);
    }

    @Test
    @DisplayName("Debe registrar paciente cuando los datos son válidos")
    void shouldRegisterPatientWhenDataIsValid() {
        // Given
        CreatePatientDTO request = createValidCreatePatientDTO();
        Patient patient = createValidPatient();

        // When
        when(patientDomainService.registerPatient(any(Patient.class))).thenReturn(patient);
        PatientDTO result = registerPatientUseCase.execute(request);

        // Then
        assertNotNull(result);
        verify(patientDomainService).registerPatient(any(Patient.class));
    }

    @Test
    @DisplayName("No debe registrar paciente cuando la solicitud es nula")
    void shouldNotRegisterPatientWhenRequestIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            registerPatientUseCase.execute(null);
        });

        verify(patientDomainService, never()).registerPatient(any());
    }

    @Test
    @DisplayName("No debe registrar paciente cuando la cédula está vacía")
    void shouldNotRegisterPatientWhenCedulaIsEmpty() {
        // Given
        CreatePatientDTO request = createValidCreatePatientDTO();
        request.setCedula("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            registerPatientUseCase.execute(request);
        });

        verify(patientDomainService, never()).registerPatient(any());
    }

    @Test
    @DisplayName("No debe registrar paciente cuando el nombre está vacío")
    void shouldNotRegisterPatientWhenFullNameIsEmpty() {
        // Given
        CreatePatientDTO request = createValidCreatePatientDTO();
        request.setFullName("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            registerPatientUseCase.execute(request);
        });

        verify(patientDomainService, never()).registerPatient(any());
    }

    // Métodos auxiliares para crear datos de prueba
    private CreatePatientDTO createValidCreatePatientDTO() {
        CreatePatientDTO dto = new CreatePatientDTO();
        dto.setCedula("12345678");
        dto.setUsername("patient1");
        dto.setPassword("Patient123!");
        dto.setFullName("Juan Pérez");
        dto.setBirthDate("15/05/1985");
        dto.setGender("MASCULINO");
        dto.setAddress("Calle 123 #45-67");
        dto.setPhoneNumber("3001234567");
        dto.setEmail("juan.perez@email.com");
        return dto;
    }

    private Patient createValidPatient() {
        return Patient.of(
            app.clinic.domain.model.PatientCedula.of("12345678"),
            app.clinic.domain.model.PatientUsername.of("patient1"),
            app.clinic.domain.model.PatientPassword.ofHashed("hashedPassword"),
            app.clinic.domain.model.PatientFullName.of("Juan", "Pérez"),
            app.clinic.domain.model.PatientBirthDate.of(java.time.LocalDate.of(1985, 5, 15)),
            app.clinic.domain.model.PatientGender.MASCULINO,
            app.clinic.domain.model.PatientAddress.of("Calle 123 #45-67"),
            app.clinic.domain.model.PatientPhoneNumber.of("3001234567"),
            app.clinic.domain.model.PatientEmail.of("juan.perez@email.com"),
            java.util.List.of(),
            null
        );
    }
}