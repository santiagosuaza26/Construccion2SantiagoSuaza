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

import app.clinic.domain.model.EmergencyContact;
import app.clinic.domain.model.InsurancePolicy;
import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientAddress;
import app.clinic.domain.model.PatientBirthDate;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientEmail;
import app.clinic.domain.model.PatientFullName;
import app.clinic.domain.model.PatientGender;
import app.clinic.domain.model.PatientPassword;
import app.clinic.domain.model.PatientPhoneNumber;
import app.clinic.domain.model.PatientUsername;
import app.clinic.domain.port.PatientRepository;

/**
 * Tests unitarios independientes para PatientDomainService.
 * Estos tests verifican la lógica de negocio pura sin dependencias externas.
 */
@ExtendWith(MockitoExtension.class)
class PatientDomainServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientDomainService patientDomainService;

    @BeforeEach
    void setUp() {
        patientDomainService = new PatientDomainService(patientRepository);
    }

    @Test
    @DisplayName("Debe registrar paciente con datos válidos")
    void shouldRegisterPatientWhenDataIsValid() {
        // Given
        Patient patient = createValidPatient();

        // When
        when(patientRepository.save(patient)).thenReturn(patient);
        Patient result = patientDomainService.registerPatient(patient);

        // Then
        assertNotNull(result);
        assertEquals(patient.getCedula(), result.getCedula());
        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("No debe registrar paciente cuando la cédula ya existe")
    void shouldNotRegisterPatientWhenCedulaAlreadyExists() {
        // Given
        Patient patient = createValidPatient();

        // When
        when(patientRepository.existsByCedula(patient.getCedula())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            patientDomainService.registerPatient(patient);
        });

        verify(patientRepository, never()).save(any());
    }

    @Test
    @DisplayName("No debe registrar paciente con póliza de seguro expirada")
    void shouldNotRegisterPatientWithExpiredInsurance() {
        // Given
        Patient patient = createPatientWithExpiredInsurance();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            patientDomainService.registerPatient(patient);
        });

        verify(patientRepository, never()).save(any());
    }

    @Test
    @DisplayName("No debe registrar paciente con más de un contacto de emergencia")
    void shouldNotRegisterPatientWithMultipleEmergencyContacts() {
        // Given
        Patient patient = createPatientWithMultipleEmergencyContacts();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            patientDomainService.registerPatient(patient);
        });

        verify(patientRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar paciente cuando existe")
    void shouldUpdatePatientWhenPatientExists() {
        // Given
        Patient existingPatient = createValidPatient();
        Patient updatedPatient = createUpdatedPatient();

        // When
        when(patientRepository.findById(any())).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(updatedPatient)).thenReturn(updatedPatient);

        Patient result = patientDomainService.updatePatient(updatedPatient);

        // Then
        assertNotNull(result);
        verify(patientRepository).save(updatedPatient);
    }

    @Test
    @DisplayName("Debe encontrar paciente por cédula")
    void shouldFindPatientByCedula() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");
        Patient patient = createValidPatient();

        // When
        when(patientRepository.findByCedula(cedula)).thenReturn(Optional.of(patient));
        Optional<Patient> result = patientDomainService.findPatientByCedula(cedula);

        // Then
        assertTrue(result.isPresent());
        assertEquals(patient.getCedula(), result.get().getCedula());
        verify(patientRepository).findByCedula(cedula);
    }

    @Test
    @DisplayName("Debe eliminar paciente cuando existe")
    void shouldDeletePatientWhenPatientExists() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");
        Patient patient = createValidPatient();

        // When
        when(patientRepository.findByCedula(cedula)).thenReturn(Optional.of(patient));
        patientDomainService.deletePatientByCedula(cedula);

        // Then
        verify(patientRepository).deleteByCedula(cedula);
    }

    @Test
    @DisplayName("No debe eliminar paciente cuando no existe")
    void shouldNotDeletePatientWhenPatientDoesNotExist() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");

        // When
        when(patientRepository.findByCedula(cedula)).thenReturn(Optional.empty());

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            patientDomainService.deletePatientByCedula(cedula);
        });

        verify(patientRepository, never()).deleteByCedula(any());
    }

    // Métodos auxiliares para crear datos de prueba
    private Patient createValidPatient() {
        return Patient.of(
            PatientCedula.of("12345678"),
            PatientUsername.of("patient1"),
            PatientPassword.of("Patient123!"),
            PatientFullName.of("Juan", "Pérez"),
            PatientBirthDate.of(LocalDate.of(1985, 5, 15)),
            PatientGender.MASCULINO,
            PatientAddress.of("Calle 123 #45-67"),
            PatientPhoneNumber.of("3001234567"),
            PatientEmail.of("juan.perez@email.com"),
            java.util.List.of(), // Lista vacía por simplicidad
            null // Sin póliza por simplicidad
        );
    }

    private Patient createUpdatedPatient() {
        return Patient.of(
            PatientCedula.of("12345678"),
            PatientUsername.of("patient1"),
            PatientPassword.of("Patient123!"),
            PatientFullName.of("Juan", "Pérez García"),
            PatientBirthDate.of(LocalDate.of(1985, 5, 15)),
            PatientGender.MASCULINO,
            PatientAddress.of("Calle 123 #45-67 Updated"),
            PatientPhoneNumber.of("3001234567"),
            PatientEmail.of("juan.perez@email.com"),
            java.util.List.of(),
            null
        );
    }

    private Patient createPatientWithExpiredInsurance() {
        return Patient.of(
            PatientCedula.of("87654321"),
            PatientUsername.of("expiredpatient"),
            PatientPassword.of("Patient123!"),
            PatientFullName.of("Paciente", "Expirado"),
            PatientBirthDate.of(LocalDate.of(1980, 3, 10)),
            PatientGender.FEMENINO,
            PatientAddress.of("Dirección expirada"),
            PatientPhoneNumber.of("3009999999"),
            PatientEmail.of("expired@email.com"),
            java.util.List.of(),
            null
        );
    }

    private Patient createPatientWithMultipleEmergencyContacts() {
        return Patient.of(
            PatientCedula.of("11223344"),
            PatientUsername.of("multiplecontact"),
            PatientPassword.of("Patient123!"),
            PatientFullName.of("Paciente", "Multiple"),
            PatientBirthDate.of(LocalDate.of(1975, 8, 20)),
            PatientGender.MASCULINO,
            PatientAddress.of("Dirección múltiple"),
            PatientPhoneNumber.of("3008888888"),
            PatientEmail.of("multiple@email.com"),
            java.util.List.of(),
            null
        );
    }
}