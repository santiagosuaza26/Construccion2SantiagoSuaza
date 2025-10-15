package app.clinic.application.controller;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.dto.patient.UpdatePatientDTO;
import app.clinic.application.service.PatientApplicationService;
import app.clinic.domain.exception.PatientAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para PatientController.
 * Verifica los endpoints REST para operaciones de pacientes.
 */
@WebMvcTest(PatientController.class)
@DisplayName("Patient Controller Tests")
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientApplicationService patientApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreatePatientDTO validCreatePatientDTO;
    private UpdatePatientDTO validUpdatePatientDTO;
    private PatientDTO validPatientDTO;

    @BeforeEach
    void setUp() {
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

        validPatientDTO = new PatientDTO(
            "12345678",
            "testpatient",
            "Juan Pérez",
            "15/05/1990",
            "MASCULINO",
            "Calle 123 #45-67",
            "3001234567",
            "juan.perez@test.com",
            34,
            null,
            null
        );
    }

    @Nested
    @DisplayName("Registro de Pacientes")
    class PatientRegistrationTests {

        @Test
        @DisplayName("Debe registrar paciente exitosamente")
        void shouldRegisterPatientSuccessfully() throws Exception {
            // Given
            when(patientApplicationService.registerPatient(any(CreatePatientDTO.class)))
                .thenReturn(validPatientDTO);

            // When & Then
            mockMvc.perform(post("/api/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validCreatePatientDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.cedula").value("12345678"))
                    .andExpect(jsonPath("$.fullName").value("Juan Pérez"))
                    .andExpect(jsonPath("$.email").value("juan.perez@test.com"));

            verify(patientApplicationService).registerPatient(any(CreatePatientDTO.class));
        }

        @Test
        @DisplayName("Debe retornar conflicto cuando paciente ya existe")
        void shouldReturnConflictWhenPatientAlreadyExists() throws Exception {
            // Given
            when(patientApplicationService.registerPatient(any(CreatePatientDTO.class)))
                .thenThrow(new PatientAlreadyExistsException("12345678"));

            // When & Then
            mockMvc.perform(post("/api/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validCreatePatientDTO)))
                    .andExpect(status().isConflict());

            verify(patientApplicationService).registerPatient(any(CreatePatientDTO.class));
        }

        @Test
        @DisplayName("Debe retornar error de validación para datos inválidos")
        void shouldReturnValidationErrorForInvalidData() throws Exception {
            // Given
            CreatePatientDTO invalidDTO = new CreatePatientDTO(
                "", // Cédula vacía
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

            // When & Then
            mockMvc.perform(post("/api/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(status().isBadRequest());

            verify(patientApplicationService, never()).registerPatient(any(CreatePatientDTO.class));
        }
    }

    @Nested
    @DisplayName("Consulta de Pacientes")
    class PatientQueryTests {

        @Test
        @DisplayName("Debe retornar paciente por cédula")
        void shouldReturnPatientByCedula() throws Exception {
            // Given
            when(patientApplicationService.findPatientByCedula("12345678"))
                .thenReturn(Optional.of(validPatientDTO));

            // When & Then
            mockMvc.perform(get("/api/patients/cedula/12345678"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cedula").value("12345678"))
                    .andExpect(jsonPath("$.fullName").value("Juan Pérez"));

            verify(patientApplicationService).findPatientByCedula("12345678");
        }

        @Test
        @DisplayName("Debe retornar not found cuando paciente no existe por cédula")
        void shouldReturnNotFoundWhenPatientDoesNotExistByCedula() throws Exception {
            // Given
            when(patientApplicationService.findPatientByCedula("12345678"))
                .thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(get("/api/patients/cedula/12345678"))
                    .andExpect(status().isNotFound());

            verify(patientApplicationService).findPatientByCedula("12345678");
        }

        @Test
        @DisplayName("Debe retornar paciente por nombre de usuario")
        void shouldReturnPatientByUsername() throws Exception {
            // Given
            when(patientApplicationService.findPatientByUsername("testpatient"))
                .thenReturn(Optional.of(validPatientDTO));

            // When & Then
            mockMvc.perform(get("/api/patients/username/testpatient"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("testpatient"))
                    .andExpect(jsonPath("$.fullName").value("Juan Pérez"));

            verify(patientApplicationService).findPatientByUsername("testpatient");
        }

        @Test
        @DisplayName("Debe retornar todos los pacientes")
        void shouldReturnAllPatients() throws Exception {
            // Given
            List<PatientDTO> patients = List.of(validPatientDTO);
            when(patientApplicationService.findAllPatients()).thenReturn(patients);

            // When & Then
            mockMvc.perform(get("/api/patients"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].cedula").value("12345678"));

            verify(patientApplicationService).findAllPatients();
        }
    }

    @Nested
    @DisplayName("Actualización de Pacientes")
    class PatientUpdateTests {

        @Test
        @DisplayName("Debe actualizar paciente exitosamente")
        void shouldUpdatePatientSuccessfully() throws Exception {
            // Given
            when(patientApplicationService.updatePatient(any(UpdatePatientDTO.class)))
                .thenReturn(validPatientDTO);

            // When & Then
            mockMvc.perform(put("/api/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validUpdatePatientDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cedula").value("12345678"))
                    .andExpect(jsonPath("$.address").value("Nueva dirección 456"));

            verify(patientApplicationService).updatePatient(any(UpdatePatientDTO.class));
        }

        @Test
        @DisplayName("Debe retornar not found cuando paciente no existe para actualizar")
        void shouldReturnNotFoundWhenPatientDoesNotExistForUpdate() throws Exception {
            // Given
            when(patientApplicationService.updatePatient(any(UpdatePatientDTO.class)))
                .thenThrow(new IllegalArgumentException("Patient not found"));

            // When & Then
            mockMvc.perform(put("/api/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validUpdatePatientDTO)))
                    .andExpect(status().isNotFound());

            verify(patientApplicationService).updatePatient(any(UpdatePatientDTO.class));
        }
    }

    @Nested
    @DisplayName("Eliminación de Pacientes")
    class PatientDeletionTests {

        @Test
        @DisplayName("Debe eliminar paciente por cédula exitosamente")
        void shouldDeletePatientByCedulaSuccessfully() throws Exception {
            // Given
            doNothing().when(patientApplicationService).deletePatientByCedula("12345678");

            // When & Then
            mockMvc.perform(delete("/api/patients/cedula/12345678"))
                    .andExpect(status().isNoContent());

            verify(patientApplicationService).deletePatientByCedula("12345678");
        }

        @Test
        @DisplayName("Debe eliminar paciente por ID exitosamente")
        void shouldDeletePatientByIdSuccessfully() throws Exception {
            // Given
            doNothing().when(patientApplicationService).deletePatientById("patient-123");

            // When & Then
            mockMvc.perform(delete("/api/patients/id/patient-123"))
                    .andExpect(status().isNoContent());

            verify(patientApplicationService).deletePatientById("patient-123");
        }
    }

    @Nested
    @DisplayName("Validaciones Adicionales")
    class AdditionalValidationTests {

        @Test
        @DisplayName("Debe verificar si paciente tiene seguro activo")
        void shouldCheckIfPatientHasActiveInsurance() throws Exception {
            // Given
            when(patientApplicationService.hasActiveInsurance("12345678"))
                .thenReturn(true);

            // When & Then
            mockMvc.perform(get("/api/patients/12345678/has-active-insurance"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(true));

            verify(patientApplicationService).hasActiveInsurance("12345678");
        }

        @Test
        @DisplayName("Debe obtener edad del paciente")
        void shouldGetPatientAge() throws Exception {
            // Given
            when(patientApplicationService.getPatientAge("12345678"))
                .thenReturn(34);

            // When & Then
            mockMvc.perform(get("/api/patients/12345678/age"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(34));

            verify(patientApplicationService).getPatientAge("12345678");
        }

        @Test
        @DisplayName("Debe manejar errores internos del servidor")
        void shouldHandleInternalServerErrors() throws Exception {
            // Given
            when(patientApplicationService.findAllPatients())
                .thenThrow(new RuntimeException("Database connection failed"));

            // When & Then
            mockMvc.perform(get("/api/patients"))
                    .andExpect(status().isInternalServerError());

            verify(patientApplicationService).findAllPatients();
        }
    }
}