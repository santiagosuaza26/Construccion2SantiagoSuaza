package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.PatientRepository;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientService = new PatientService(patientRepository);
    }

    @Test
    void shouldRegisterPatientSuccessfully() {
        // Given
        String identificationNumber = "123456789";
        String fullName = "John Doe";
        String dateOfBirth = "01/01/1990";
        String gender = "MASCULINO";
        String address = "123 Main St";
        String phone = "1234567890";
        String email = "john@example.com";
        String emergencyName = "Jane Doe";
        String emergencyRelation = "Sister";
        String emergencyPhone = "0987654321";
        String companyName = "Insurance Co";
        String policyNumber = "POL123";
        boolean insuranceActive = true;
        String validityDate = "2025-12-31";

        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(false);

        // When
        Patient patient = patientService.registerPatient(identificationNumber, fullName, dateOfBirth, gender, address, phone, email, "Password123!", emergencyName, emergencyRelation, emergencyPhone, companyName, policyNumber, insuranceActive, validityDate);

        // Then
        assertNotNull(patient);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldThrowExceptionWhenPatientAlreadyExists() {
        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.registerPatient("123456789", "John Doe", "01/01/1990", "MASCULINO", "123 Main St", "1234567890", "john@example.com", "Password123!", "Jane Doe", "Sister", "0987654321", "Insurance Co", "POL123", true, "2025-12-31"));
    }

    @Test
    void shouldUpdatePatientSuccessfully() {
        // Given
        Patient existingPatient = mock(Patient.class);
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(java.util.Optional.of(existingPatient));

        // When
        patientService.updatePatient("123456789", "Jane Doe", "01/01/1990", "FEMENINO", "456 Oak St", "0987654321", "jane@example.com", "John Doe", "Brother", "1234567890", "New Insurance", "POL456", false, "2024-12-31");

        // Then
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPatient() {
        // Given
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.updatePatient("123456789", "Jane Doe", "01/01/1990", "FEMENINO", "456 Oak St", "0987654321", "jane@example.com", "John Doe", "Brother", "1234567890", "New Insurance", "POL456", false, "2024-12-31"));
    }

    @Test
    void shouldFindPatientById() {
        // Given
        Patient patient = mock(Patient.class);
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(java.util.Optional.of(patient));

        // When
        Patient result = patientService.findPatientById("123456789");

        // Then
        assertEquals(patient, result);
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistentPatient() {
        // Given
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.findPatientById("123456789"));
    }

    @Test
    void shouldDeletePatientSuccessfully() {
        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);

        // When
        patientService.deletePatient("123456789");

        // Then
        verify(patientRepository).deleteByIdentificationNumber(any(Id.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPatient() {
        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.deletePatient("123456789"));
    }
}