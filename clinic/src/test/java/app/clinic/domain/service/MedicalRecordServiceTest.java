package app.clinic.domain.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.MedicalRecordRepository;
import app.clinic.domain.repository.PatientRepository;

class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PatientRepository patientRepository;

    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medicalRecordService = new MedicalRecordService(medicalRecordRepository, patientRepository);
    }

    @Test
    void shouldGetOrCreateMedicalRecordForExistingPatient() {
        // Given
        String patientId = "123456789";
        MedicalRecord existingRecord = new MedicalRecord(patientId);
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(medicalRecordRepository.findByPatientIdentificationNumber(patientId)).thenReturn(Optional.of(existingRecord));

        // When
        MedicalRecord record = medicalRecordService.getOrCreateMedicalRecord(patientId);

        // Then
        assertEquals(existingRecord, record);
    }

    @Test
    void shouldCreateNewMedicalRecordForNonExistentRecord() {
        // Given
        String patientId = "123456789";
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(medicalRecordRepository.findByPatientIdentificationNumber(patientId)).thenReturn(Optional.empty());

        // When
        MedicalRecord record = medicalRecordService.getOrCreateMedicalRecord(patientId);

        // Then
        assertNotNull(record);
        assertEquals(patientId, record.getPatientIdentificationNumber());
    }

    @Test
    void shouldThrowExceptionForNonExistentPatient() {
        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.getOrCreateMedicalRecord("123456789"));
    }

    @Test
    void shouldAddRecordSuccessfully() {
        // Given
        String patientId = "123456789";
        String doctorId = "987654321";
        String reason = "Checkup";
        String symptoms = "Headache";
        String diagnosis = "Migraine";
        MedicalRecord record = new MedicalRecord(patientId);
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(medicalRecordRepository.findByPatientIdentificationNumber(patientId)).thenReturn(Optional.of(record));

        // When
        medicalRecordService.addRecord(patientId, doctorId, reason, symptoms, diagnosis);

        // Then
        verify(medicalRecordRepository).save(record);
    }

    @Test
    void shouldAddMedicationToRecordSuccessfully() {
        // Given
        String patientId = "123456789";
        String orderNumber = "000001";
        String medicationId = "MED001";
        String dosage = "10mg";
        String duration = "7 days";
        MedicalRecord record = new MedicalRecord(patientId);
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(medicalRecordRepository.findByPatientIdentificationNumber(patientId)).thenReturn(Optional.of(record));

        // When
        medicalRecordService.addMedicationToRecord(patientId, orderNumber, medicationId, dosage, duration);

        // Then
        verify(medicalRecordRepository).save(record);
    }
}