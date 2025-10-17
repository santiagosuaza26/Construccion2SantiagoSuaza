package app.clinic.application.mapper;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.dto.patient.UpdatePatientDTO;
import app.clinic.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas unitarias para PatientMapper.
 * Verifica la conversión correcta entre DTOs y entidades de dominio.
 */
@DisplayName("Patient Mapper Tests")
class PatientMapperTest {

    private CreatePatientDTO validCreatePatientDTO;
    private UpdatePatientDTO validUpdatePatientDTO;
    private Patient validPatient;

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

        validPatient = Patient.of(
            PatientCedula.of("12345678"),
            PatientUsername.of("testpatient"),
            PatientPassword.of("TestPassword123!"),
            PatientFullName.of("Juan", "Pérez"),
            PatientBirthDate.of(LocalDate.of(1990, 5, 15)),
            PatientGender.MASCULINO,
            PatientAddress.of("Calle 123 #45-67"),
            PatientPhoneNumber.of("3001234567"),
            PatientEmail.of("juan.perez@test.com"),
            List.of(),
            null
        );
    }

    @Nested
    @DisplayName("Conversión de CreatePatientDTO a Entidad de Dominio")
    class CreatePatientDTOToDomainEntityTests {

        @Test
        @DisplayName("Debe convertir CreatePatientDTO a Patient correctamente")
        void shouldConvertCreatePatientDTOToPatientCorrectly() {
            // When
            Patient result = PatientMapper.toDomainEntity(validCreatePatientDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getCedula().getValue()).isEqualTo("12345678");
            assertThat(result.getUsername().getValue()).isEqualTo("testpatient");
            assertThat(result.getFullName().getFirstNames()).isEqualTo("Juan");
            assertThat(result.getFullName().getLastNames()).isEqualTo("Pérez");
            assertThat(result.getEmail().getValue()).isEqualTo("juan.perez@test.com");
            assertThat(result.getGender()).isEqualTo(PatientGender.MASCULINO);
            assertThat(result.getAddress().getValue()).isEqualTo("Calle 123 #45-67");
            assertThat(result.getPhoneNumber().getValue()).isEqualTo("3001234567");
        }

        @Test
        @DisplayName("Debe manejar contacto de emergencia cuando está presente")
        void shouldHandleEmergencyContactWhenPresent() {
            // Given
            CreatePatientDTO dtoWithEmergencyContact = new CreatePatientDTO(
                "87654321",
                "emergencypat",
                "TestPassword123!",
                "Ana García",
                "20/03/1985",
                "FEMENINO",
                "Avenida 456 #78-90",
                "3009876543",
                "ana.garcia@test.com",
                new app.clinic.application.dto.patient.CreateEmergencyContactDTO(
                    "María González", "Hermana", "3011234567"
                ),
                null
            );

            // When
            Patient result = PatientMapper.toDomainEntity(dtoWithEmergencyContact);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getEmergencyContacts()).hasSize(1);
            assertThat(result.getEmergencyContacts().get(0).getName().getFullName()).isEqualTo("María González");
            assertThat(result.getEmergencyContacts().get(0).getRelationship()).isEqualTo(Relationship.HERMANA);
        }

        @Test
        @DisplayName("Debe manejar póliza de seguro cuando está presente")
        void shouldHandleInsurancePolicyWhenPresent() {
            // Given
            CreatePatientDTO dtoWithInsurance = new CreatePatientDTO(
                "11223344",
                "insurancepat",
                "TestPassword123!",
                "Carlos López",
                "10/12/1980",
                "MASCULINO",
                "Carrera 789 #12-34",
                "3005556666",
                "carlos.lopez@test.com",
                null,
                new app.clinic.application.dto.patient.CreateInsurancePolicyDTO(
                    "Seguros ABC S.A.",
                    "POL-001",
                    "ACTIVA",
                    "31/12/2025"
                )
            );

            // When
            Patient result = PatientMapper.toDomainEntity(dtoWithInsurance);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getInsurancePolicy()).isNotNull();
            assertThat(result.getInsurancePolicy().getCompanyName().getValue()).isEqualTo("Seguros ABC S.A.");
            assertThat(result.getInsurancePolicy().getPolicyNumber().getValue()).isEqualTo("POL-001");
            assertThat(result.getInsurancePolicy().getStatus()).isEqualTo(PolicyStatus.ACTIVA);
        }
    }

    @Nested
    @DisplayName("Conversión de Patient a PatientDTO")
    class PatientToDTOTests {

        @Test
        @DisplayName("Debe convertir Patient a PatientDTO correctamente")
        void shouldConvertPatientToPatientDTOCorrectly() {
            // When
            PatientDTO result = PatientMapper.toDTO(validPatient);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getCedula()).isEqualTo("12345678");
            assertThat(result.getUsername()).isEqualTo("testpatient");
            assertThat(result.getFullName()).isEqualTo("Juan Pérez");
            assertThat(result.getEmail()).isEqualTo("juan.perez@test.com");
            assertThat(result.getGender()).isEqualTo("MASCULINO");
            assertThat(result.getAddress()).isEqualTo("Calle 123 #45-67");
            assertThat(result.getPhoneNumber()).isEqualTo("3001234567");
            assertThat(result.getAge()).isEqualTo(35);
        }

        @Test
        @DisplayName("Debe calcular edad correctamente")
        void shouldCalculateAgeCorrectly() {
            // Given
            Patient youngPatient = Patient.of(
                PatientCedula.of("55566677"),
                PatientUsername.of("youngpat"),
                PatientPassword.of("TestPassword123!"),
                PatientFullName.of("Niño", "Pérez"),
                PatientBirthDate.of(LocalDate.now().minusYears(5)), // 5 años
                PatientGender.MASCULINO,
                PatientAddress.of("Calle infantil 123"),
                PatientPhoneNumber.of("3001112222"),
                PatientEmail.of("nino.perez@test.com"),
                List.of(),
                null
            );

            // When
            PatientDTO result = PatientMapper.toDTO(youngPatient);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getAge()).isEqualTo(5);
        }

        @Test
        @DisplayName("Debe manejar paciente con contacto de emergencia")
        void shouldHandlePatientWithEmergencyContact() {
            // Given
            EmergencyContact emergencyContact = EmergencyContact.of(
                EmergencyContactName.of("María", "González"),
                Relationship.HERMANA,
                EmergencyContactPhoneNumber.of("3011234567")
            );

            Patient patientWithEmergencyContact = Patient.of(
                PatientCedula.of("99988877"),
                PatientUsername.of("emergencypat"),
                PatientPassword.of("TestPassword123!"),
                PatientFullName.of("Paciente", "Emergencia"),
                PatientBirthDate.of(LocalDate.of(1985, 3, 20)),
                PatientGender.FEMENINO,
                PatientAddress.of("Dirección emergencia 123"),
                PatientPhoneNumber.of("3009998888"),
                PatientEmail.of("paciente.emergencia@test.com"),
                List.of(emergencyContact),
                null
            );

            // When
            PatientDTO result = PatientMapper.toDTO(patientWithEmergencyContact);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getEmergencyContact()).isNotNull();
            assertThat(result.getEmergencyContact().getName()).isEqualTo("María González");
            assertThat(result.getEmergencyContact().getRelationship()).isEqualTo("Hermana");
            assertThat(result.getEmergencyContact().getPhoneNumber()).isEqualTo("3011234567");
        }
    }

    @Nested
    @DisplayName("Actualización de Entidad de Dominio")
    class UpdateEntityTests {

        @Test
        @DisplayName("Debe actualizar entidad de dominio correctamente")
        void shouldUpdateEntityCorrectly() {
            // When
            Patient result = PatientMapper.toDomainEntityForUpdate(validPatient, validUpdatePatientDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getCedula().getValue()).isEqualTo("12345678"); // No cambia
            assertThat(result.getFullName().getFullName()).isEqualTo("Juan Pérez Actualizado");
            assertThat(result.getFullName().getFirstNames()).isEqualTo("Juan");
            assertThat(result.getFullName().getLastNames()).isEqualTo("Pérez Actualizado");
            assertThat(result.getAddress().getValue()).isEqualTo("Nueva dirección 456");
            assertThat(result.getPhoneNumber().getValue()).isEqualTo("3009876543");
            assertThat(result.getEmail().getValue()).isEqualTo("juan.nuevo@test.com");
        }

        @Test
        @DisplayName("Debe mantener valores originales cuando DTO tiene campos nulos")
        void shouldKeepOriginalValuesWhenDTOHasNullFields() {
            // Given
            UpdatePatientDTO partialUpdateDTO = new UpdatePatientDTO(
                "12345678",
                null, // No actualizar nombre
                null, // No actualizar fecha de nacimiento
                null, // No actualizar género
                "Nueva dirección 456", // Solo actualizar dirección
                null, // No actualizar teléfono
                null, // No actualizar email
                null,
                null
            );

            // When
            Patient result = PatientMapper.toDomainEntityForUpdate(validPatient, partialUpdateDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFullName().getFullName()).isEqualTo("Juan Pérez"); // Mantiene valor original
            assertThat(result.getAddress().getValue()).isEqualTo("Nueva dirección 456"); // Valor actualizado
            assertThat(result.getPhoneNumber().getValue()).isEqualTo("3001234567"); // Mantiene valor original
        }
    }

    @Nested
    @DisplayName("Validaciones de Conversión")
    class ConversionValidationTests {

        @Test
        @DisplayName("Conversión bidireccional debe mantener integridad de datos")
        void bidirectionalConversionShouldMaintainDataIntegrity() {
            // Given
            Patient originalPatient = validPatient;

            // When - Patient -> DTO -> Patient
            PatientDTO dto = PatientMapper.toDTO(originalPatient);
            Patient convertedBackPatient = PatientMapper.toDomainEntity(
                new CreatePatientDTO(
                    dto.getCedula(),
                    dto.getUsername(),
                    "TestPassword123!", // Necesario para CreatePatientDTO
                    dto.getFullName(),
                    dto.getBirthDate(), // El DTO ya devuelve la fecha en formato dd/MM/yyyy
                    dto.getGender(),
                    dto.getAddress(),
                    dto.getPhoneNumber(),
                    dto.getEmail(),
                    null,
                    null
                )
            );

            // Then
            assertThat(convertedBackPatient.getCedula().getValue()).isEqualTo(originalPatient.getCedula().getValue());
            assertThat(convertedBackPatient.getUsername().getValue()).isEqualTo(originalPatient.getUsername().getValue());
            assertThat(convertedBackPatient.getFullName().getFullName()).isEqualTo(originalPatient.getFullName().getFullName());
            assertThat(convertedBackPatient.getEmail().getValue()).isEqualTo(originalPatient.getEmail().getValue());
        }

        @Test
        @DisplayName("Debe manejar valores nulos correctamente")
        void shouldHandleNullValuesCorrectly() {
            // Given
            CreatePatientDTO dtoWithNulls = new CreatePatientDTO(
                "77788899",
                "nullpatient",
                "TestPassword123!",
                "Paciente Nulos",
                "01/01/1980",
                "MASCULINO",
                "Dirección nulos",
                "3007778888",
                "paciente.nulos@test.com",
                null, // Sin contacto de emergencia
                null  // Sin póliza de seguro
            );

            // When
            Patient result = PatientMapper.toDomainEntity(dtoWithNulls);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getEmergencyContacts()).isEmpty();
            assertThat(result.getInsurancePolicy()).isNull();
        }
    }
}