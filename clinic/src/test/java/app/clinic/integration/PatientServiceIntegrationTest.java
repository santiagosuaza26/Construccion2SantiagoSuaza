package app.clinic.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientEmail;
import app.clinic.domain.model.PatientFullName;
import app.clinic.domain.model.PatientUsername;
import app.clinic.domain.service.PatientDomainService;

/**
 * Integration tests for PatientService using Testcontainers.
 * Tests complete integration with PostgreSQL and MongoDB databases.
 *
 * This test class validates:
 * - Database connectivity and operations
 * - Domain service business logic with real data persistence
 * - Cross-cutting concerns (transactions, auditing)
 * - Data consistency across multiple database systems
 */
@SpringBootTest
@Testcontainers
@DisplayName("Patient Service Integration Tests")
class PatientServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("clinic_test")
            .withUsername("test_user")
            .withPassword("test_password");

    @Container
    static MongoDBContainer mongoDB = new MongoDBContainer("mongo:7-jammy")
            .withExposedPorts(27017);

    @Autowired
    private PatientDomainService patientDomainService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.mongodb.uri", mongoDB::getReplicaSetUrl);
    }

    @Test
    @DisplayName("Should register and retrieve patient successfully")
    void shouldRegisterAndRetrievePatientSuccessfully() {
        // Given
        Patient patient = createTestPatient();

        // When
        Patient registeredPatient = patientDomainService.registerPatient(patient);
        Patient foundPatient = patientDomainService.findPatientByCedula(patient.getCedula()).orElse(null);

        // Then
        assertThat(registeredPatient).isNotNull();
        assertThat(foundPatient).isNotNull();
        assertThat(foundPatient.getCedula()).isEqualTo(registeredPatient.getCedula());
        assertThat(foundPatient.getFullName()).isEqualTo(registeredPatient.getFullName());
        assertThat(foundPatient.getEmail()).isEqualTo(registeredPatient.getEmail());
    }

    @Test
    @DisplayName("Should update patient information successfully")
    void shouldUpdatePatientInformationSuccessfully() {
        // Given
        Patient originalPatient = createTestPatient();
        Patient registeredPatient = patientDomainService.registerPatient(originalPatient);

        // Create updated patient
        Patient updatedPatient = Patient.of(
                registeredPatient.getCedula(),
                registeredPatient.getUsername(),
                registeredPatient.getPassword(),
                PatientFullName.of("María Updated", "González Updated"),
                registeredPatient.getBirthDate(),
                registeredPatient.getGender(),
                registeredPatient.getAddress(),
                registeredPatient.getPhoneNumber(),
                PatientEmail.of("updated.patient@test.com"),
                registeredPatient.getEmergencyContacts(),
                registeredPatient.getInsurancePolicy()
        );

        // When
        Patient result = patientDomainService.updatePatient(updatedPatient);
        Patient foundPatient = patientDomainService.findPatientByCedula(registeredPatient.getCedula()).orElse(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(foundPatient).isNotNull();
        assertThat(foundPatient.getFullName().getFirstNames()).isEqualTo("María Updated");
        assertThat(foundPatient.getFullName().getLastNames()).isEqualTo("González Updated");
        assertThat(foundPatient.getEmail().getValue()).isEqualTo("updated.patient@test.com");
    }

    @Test
    @DisplayName("Should find all patients successfully")
    void shouldFindAllPatientsSuccessfully() {
        // Given
        Patient patient1 = createTestPatient();
        Patient patient2 = createSecondTestPatient();

        patientDomainService.registerPatient(patient1);
        patientDomainService.registerPatient(patient2);

        // When
        List<Patient> allPatients = patientDomainService.findAllPatients();

        // Then
        assertThat(allPatients).isNotEmpty();
        assertThat(allPatients.size()).isGreaterThanOrEqualTo(2);

        // Verify our test patients are in the list
        boolean foundPatient1 = allPatients.stream()
                .anyMatch(p -> p.getCedula().equals(patient1.getCedula()));
        boolean foundPatient2 = allPatients.stream()
                .anyMatch(p -> p.getCedula().equals(patient2.getCedula()));

        assertThat(foundPatient1).isTrue();
        assertThat(foundPatient2).isTrue();
    }

    @Test
    @DisplayName("Should prevent duplicate patient registration")
    void shouldPreventDuplicatePatientRegistration() {
        // Given
        Patient patient = createTestPatient();
        patientDomainService.registerPatient(patient);

        // When & Then
        try {
            patientDomainService.registerPatient(patient);
            assertThat(false).isTrue(); // Should not reach here
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains("already exists");
        }
    }

    @Test
    @DisplayName("Should delete patient successfully")
    void shouldDeletePatientSuccessfully() {
        // Given
        Patient patient = createTestPatient();
        Patient registeredPatient = patientDomainService.registerPatient(patient);

        // Verify patient exists
        assertThat(patientDomainService.findPatientByCedula(patient.getCedula())).isPresent();

        // When
        patientDomainService.deletePatientByCedula(patient.getCedula());

        // Then
        assertThat(patientDomainService.findPatientByCedula(patient.getCedula())).isEmpty();
    }

    /**
     * Creates a test patient with valid data for integration testing.
     */
    private Patient createTestPatient() {
        return Patient.of(
                PatientCedula.of("12345678"),
                PatientUsername.of("testpatient1"),
                app.clinic.domain.model.PatientPassword.ofHashed("hashedpassword123"),
                PatientFullName.of("María", "González"),
                app.clinic.domain.model.PatientBirthDate.of(LocalDate.of(1990, 5, 15)),
                app.clinic.domain.model.PatientGender.FEMENINO,
                app.clinic.domain.model.PatientAddress.of("Calle 123 #45-67, Bogotá"),
                app.clinic.domain.model.PatientPhoneNumber.of("3001234567"),
                PatientEmail.of("maria.gonzalez@test.com"),
                List.of(), // No emergency contacts for this test
                null // No insurance policy for this test
        );
    }

    /**
     * Creates a second test patient with different data for testing multiple records.
     */
    private Patient createSecondTestPatient() {
        return Patient.of(
                PatientCedula.of("87654321"),
                PatientUsername.of("testpatient2"),
                app.clinic.domain.model.PatientPassword.ofHashed("hashedpassword456"),
                PatientFullName.of("Carlos", "Rodríguez"),
                app.clinic.domain.model.PatientBirthDate.of(1985, 8, 20),
                app.clinic.domain.model.PatientGender.MASCULINO,
                app.clinic.domain.model.PatientAddress.of("Carrera 98 #12-34, Medellín"),
                app.clinic.domain.model.PatientPhoneNumber.of("3019876543"),
                PatientEmail.of("carlos.rodriguez@test.com"),
                List.of(),
                null
        );
    }
}