package app.clinic.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.application.dto.medical.MedicalRecordDTO;
import app.clinic.domain.model.*;
import app.clinic.domain.service.MedicalRecordDomainService;

/**
 * Unit tests for MedicalRecordApplicationService.
 * Tests the application layer logic for medical record operations.
 */
@ExtendWith(MockitoExtension.class)
class MedicalRecordApplicationServiceTest {

    @Mock
    private MedicalRecordDomainService medicalRecordDomainService;

    private MedicalRecordApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new MedicalRecordApplicationService(medicalRecordDomainService);
    }


    @Test
    @DisplayName("Should find medical record by patient cedula successfully")
    void shouldFindMedicalRecordByPatientCedulaSuccessfully() {
        // Given
        String patientCedula = "12345678";
        PatientRecord patientRecord = PatientRecord.empty();

        when(medicalRecordDomainService.findByPatientCedula(PatientCedula.of(patientCedula)))
            .thenReturn(patientRecord);

        // When
        Optional<MedicalRecordDTO> result = applicationService.findMedicalRecordByPatientCedula(patientCedula);

        // Then
        assertTrue(result.isPresent());
        verify(medicalRecordDomainService).findByPatientCedula(PatientCedula.of(patientCedula));
    }

    @Test
    @DisplayName("Should return empty when medical record not found")
    void shouldReturnEmptyWhenMedicalRecordNotFound() {
        // Given
        String patientCedula = "99999999";
        PatientRecord emptyRecord = PatientRecord.empty();

        when(medicalRecordDomainService.findByPatientCedula(PatientCedula.of(patientCedula)))
            .thenReturn(emptyRecord);

        // When
        Optional<MedicalRecordDTO> result = applicationService.findMedicalRecordByPatientCedula(patientCedula);

        // Then
        assertFalse(result.isPresent());
        verify(medicalRecordDomainService).findByPatientCedula(PatientCedula.of(patientCedula));
    }

    @Test
    @DisplayName("Should check if patient has medical records successfully")
    void shouldCheckIfPatientHasMedicalRecordsSuccessfully() {
        // Given
        String patientCedula = "12345678";
        PatientRecord patientRecord = PatientRecord.empty();

        when(medicalRecordDomainService.findByPatientCedula(PatientCedula.of(patientCedula)))
            .thenReturn(patientRecord);

        // When
        boolean result = applicationService.hasMedicalRecords(patientCedula);

        // Then
        assertFalse(result); // Empty record means no medical records
        verify(medicalRecordDomainService).findByPatientCedula(PatientCedula.of(patientCedula));
    }

    @Test
    @DisplayName("Should get medical record count successfully")
    void shouldGetMedicalRecordCountSuccessfully() {
        // Given
        String patientCedula = "12345678";
        PatientRecord patientRecord = PatientRecord.empty();

        when(medicalRecordDomainService.findByPatientCedula(PatientCedula.of(patientCedula)))
            .thenReturn(patientRecord);

        // When
        int result = applicationService.getMedicalRecordCount(patientCedula);

        // Then
        assertEquals(0, result); // Empty record means 0 entries
        verify(medicalRecordDomainService).findByPatientCedula(PatientCedula.of(patientCedula));
    }

}