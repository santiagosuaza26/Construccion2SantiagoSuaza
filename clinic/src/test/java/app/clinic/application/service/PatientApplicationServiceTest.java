package app.clinic.application.service;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.dto.patient.UpdatePatientDTO;
import app.clinic.domain.model.*;
import app.clinic.domain.service.PatientDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para PatientApplicationService.
 * Verifica la lógica de negocio de la capa de aplicación para pacientes.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Patient Application Service Tests")
class PatientApplicationServiceTest {

    @Mock
    private PatientDomainService patientDomainService;

    @InjectMocks
    private PatientApplicationService patientApplicationService;

    private CreatePatientDTO validCreatePatientDTO;
    private UpdatePatientDTO validUpdatePatientDTO;
    private Patient validPatient;
    private PatientCedula validPatientCedula;

    @BeforeEach
    void setUp() {
        validPatientCedula = PatientCedula.of("12345678");

        validCreatePatientDTO = new CreatePatientDTO(
            "12345678",
            "testpatient",
            "TestPassword123!",
            "Juan Pérez",
            "15/05/1990",
            "MASCULINO",
            "Calle 123 #45-67",
            "3001234567",
            "juan.perez@test.com",
            null,
            null
        );

        validUpdatePatientDTO = new UpdatePatientDTO(
            "12345678",
            "Juan Pérez Actualizado",
            "15/05/1990",
            "MASCULINO",
            "Nueva dirección 456",
            "3009876543",
            "juan.nuevo@test.com",
            null,
            null
        );

        validPatient = Patient.of(
            validPatientCedula,
            PatientUsername.of("testpatient"),
            PatientPassword.of("TestPassword123!"),
            PatientFullName.of("Juan", "Pérez"),
            PatientBirthDate.of(java.time.LocalDate.of(1990, 5, 15)),
            PatientGender.MASCULINO,
            PatientAddress.of("Calle 123 #45-67"),
            PatientPhoneNumber.of("3001234567"),
            PatientEmail.of("juan.perez@test.com"),
            List.of(),
            null
        );
    }

    @Nested
    @DisplayName("Registro de Pacientes")
    class PatientRegistrationTests {

        @Test
        @DisplayName("Debe registrar paciente exitosamente cuando los datos son válidos")
        void shouldRegisterPatientSuccessfullyWhenDataIsValid() {
            // Given
            when(patientDomainService.registerPatient(any(Patient.class))).thenReturn(validPatient);

            // When
            PatientDTO result = patientApplicationService.registerPatient(validCreatePatientDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getCedula()).isEqualTo("12345678");
            assertThat(result.getFullName()).isEqualTo("Juan Pérez");

            verify(patientDomainService).registerPatient(any(Patient.class));
        }
    }

    @Nested
    @DisplayName("Actualización de Pacientes")
    class PatientUpdateTests {

        @Test
        @DisplayName("Debe actualizar paciente exitosamente")
        void shouldUpdatePatientSuccessfully() {
            // Given
            when(patientDomainService.findPatientByCedula(validPatientCedula)).thenReturn(Optional.of(validPatient));
            when(patientDomainService.updatePatient(any(Patient.class))).thenReturn(validPatient);

            // When
            PatientDTO result = patientApplicationService.updatePatient(validUpdatePatientDTO);

            // Then
            assertThat(result).isNotNull();
            verify(patientDomainService).findPatientByCedula(validPatientCedula);
            verify(patientDomainService).updatePatient(any(Patient.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el paciente no existe")
        void shouldThrowExceptionWhenPatientDoesNotExist() {
            // Given
            when(patientDomainService.findPatientByCedula(validPatientCedula)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> patientApplicationService.updatePatient(validUpdatePatientDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Patient not found");

            verify(patientDomainService).findPatientByCedula(validPatientCedula);
            verify(patientDomainService, never()).updatePatient(any(Patient.class));
        }
    }

    @Nested
    @DisplayName("Consulta de Pacientes")
    class PatientQueryTests {

        @Test
        @DisplayName("Debe encontrar paciente por cédula cuando existe")
        void shouldFindPatientByCedulaWhenExists() {
            // Given
            when(patientDomainService.findPatientByCedula(validPatientCedula)).thenReturn(Optional.of(validPatient));

            // When
            Optional<PatientDTO> result = patientApplicationService.findPatientByCedula("12345678");

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getCedula()).isEqualTo("12345678");
            verify(patientDomainService).findPatientByCedula(validPatientCedula);
        }

        @Test
        @DisplayName("Debe retornar vacío cuando paciente no existe por cédula")
        void shouldReturnEmptyWhenPatientDoesNotExistByCedula() {
            // Given
            when(patientDomainService.findPatientByCedula(validPatientCedula)).thenReturn(Optional.empty());

            // When
            Optional<PatientDTO> result = patientApplicationService.findPatientByCedula("12345678");

            // Then
            assertThat(result).isEmpty();
            verify(patientDomainService).findPatientByCedula(validPatientCedula);
        }

        @Test
        @DisplayName("Debe encontrar paciente por nombre de usuario")
        void shouldFindPatientByUsername() {
            // Given
            PatientUsername username = PatientUsername.of("testpatient");
            when(patientDomainService.findPatientByUsername(username)).thenReturn(Optional.of(validPatient));

            // When
            Optional<PatientDTO> result = patientApplicationService.findPatientByUsername("testpatient");

            // Then
            assertThat(result).isPresent();
            verify(patientDomainService).findPatientByUsername(username);
        }

        @Test
        @DisplayName("Debe encontrar paciente por ID")
        void shouldFindPatientById() {
            // Given
            PatientId patientId = PatientId.of("patient-123");
            when(patientDomainService.findPatientById(patientId)).thenReturn(Optional.of(validPatient));

            // When
            Optional<PatientDTO> result = patientApplicationService.findPatientById("patient-123");

            // Then
            assertThat(result).isPresent();
            verify(patientDomainService).findPatientById(patientId);
        }

        @Test
        @DisplayName("Debe retornar todos los pacientes")
        void shouldReturnAllPatients() {
            // Given
            List<Patient> patients = List.of(validPatient);
            when(patientDomainService.findAllPatients()).thenReturn(patients);

            // When
            List<PatientDTO> result = patientApplicationService.findAllPatients();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCedula()).isEqualTo("12345678");
            verify(patientDomainService).findAllPatients();
        }
    }

    @Nested
    @DisplayName("Eliminación de Pacientes")
    class PatientDeletionTests {

        @Test
        @DisplayName("Debe eliminar paciente por cédula exitosamente")
        void shouldDeletePatientByCedulaSuccessfully() {
            // Given
            doNothing().when(patientDomainService).deletePatientByCedula(validPatientCedula);

            // When & Then
            assertThatCode(() -> patientApplicationService.deletePatientByCedula("12345678"))
                .doesNotThrowAnyException();

            verify(patientDomainService).deletePatientByCedula(validPatientCedula);
        }

        @Test
        @DisplayName("Debe eliminar paciente por ID exitosamente")
        void shouldDeletePatientByIdSuccessfully() {
            // Given
            PatientId patientId = PatientId.of("patient-123");
            doNothing().when(patientDomainService).deletePatientById(patientId);

            // When & Then
            assertThatCode(() -> patientApplicationService.deletePatientById("patient-123"))
                .doesNotThrowAnyException();

            verify(patientDomainService).deletePatientById(patientId);
        }
    }

    @Nested
    @DisplayName("Validaciones Adicionales")
    class AdditionalValidationTests {

        @Test
        @DisplayName("Debe verificar si paciente tiene seguro activo")
        void shouldCheckIfPatientHasActiveInsurance() {
            // Given
            when(patientDomainService.findPatientByCedula(validPatientCedula)).thenReturn(Optional.of(validPatient));

            // When
            boolean result = patientApplicationService.hasActiveInsurance("12345678");

            // Then
            assertThat(result).isFalse(); // El paciente de prueba no tiene póliza de seguro
            verify(patientDomainService).findPatientByCedula(validPatientCedula);
        }

        @Test
        @DisplayName("Debe obtener edad del paciente correctamente")
        void shouldGetPatientAgeCorrectly() {
            // Given
            when(patientDomainService.findPatientByCedula(validPatientCedula)).thenReturn(Optional.of(validPatient));

            // When
            int age = patientApplicationService.getPatientAge("12345678");

            // Then
            assertThat(age).isEqualTo(34); // Edad calculada basada en la fecha de nacimiento
            verify(patientDomainService).findPatientByCedula(validPatientCedula);
        }

        @Test
        @DisplayName("Debe retornar 0 cuando paciente no existe para cálculo de edad")
        void shouldReturnZeroWhenPatientDoesNotExistForAgeCalculation() {
            // Given
            when(patientDomainService.findPatientByCedula(validPatientCedula)).thenReturn(Optional.empty());

            // When
            int age = patientApplicationService.getPatientAge("12345678");

            // Then
            assertThat(age).isEqualTo(0);
            verify(patientDomainService).findPatientByCedula(validPatientCedula);
        }
    }
}