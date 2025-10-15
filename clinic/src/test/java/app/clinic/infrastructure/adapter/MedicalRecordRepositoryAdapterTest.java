package app.clinic.infrastructure.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.domain.model.*;
import app.clinic.infrastructure.entity.MedicalRecordDocument;
import app.clinic.infrastructure.repository.MedicalRecordMongoRepository;

/**
 * Unit tests for MedicalRecordRepositoryAdapter.
 * Tests the integration between domain layer and MongoDB infrastructure.
 */
@ExtendWith(MockitoExtension.class)
class MedicalRecordRepositoryAdapterTest {

    @Mock
    private MedicalRecordMongoRepository mongoRepository;

    private MedicalRecordRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MedicalRecordRepositoryAdapter(mongoRepository);
    }

    @Test
    @DisplayName("Should save patient record map successfully")
    void shouldSavePatientRecordMapSuccessfully() {
        // Given
        PatientRecordMap patientRecordMap = createTestPatientRecordMap();
        MedicalRecordDocument document = createTestMedicalRecordDocument();
        MedicalRecordDocument savedDocument = createTestMedicalRecordDocument();

        when(mongoRepository.save(any(MedicalRecordDocument.class))).thenReturn(savedDocument);

        // When
        PatientRecordMap result = adapter.save(patientRecordMap);

        // Then
        assertNotNull(result);
        verify(mongoRepository).save(any(MedicalRecordDocument.class));
    }

    @Test
    @DisplayName("Should find patient record by cedula successfully")
    void shouldFindPatientRecordByCedulaSuccessfully() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");
        MedicalRecordDocument document = createTestMedicalRecordDocument();

        when(mongoRepository.findByPatientNationalId("12345678")).thenReturn(Optional.of(document));

        // When
        Optional<PatientRecord> result = adapter.findByPatientCedula(cedula);

        // Then
        assertTrue(result.isPresent());
        verify(mongoRepository).findByPatientNationalId("12345678");
    }

    @Test
    @DisplayName("Should return empty when patient record not found")
    void shouldReturnEmptyWhenPatientRecordNotFound() {
        // Given
        PatientCedula cedula = PatientCedula.of("99999999");

        when(mongoRepository.findByPatientNationalId("99999999")).thenReturn(Optional.empty());

        // When
        Optional<PatientRecord> result = adapter.findByPatientCedula(cedula);

        // Then
        assertFalse(result.isPresent());
        verify(mongoRepository).findByPatientNationalId("99999999");
    }

    @Test
    @DisplayName("Should find record entry by key successfully")
    void shouldFindRecordEntryByKeySuccessfully() {
        // Given
        PatientRecordKey key = createTestPatientRecordKey();
        MedicalRecordDocument document = createTestMedicalRecordDocument();

        when(mongoRepository.findByPatientNationalId("12345678")).thenReturn(Optional.of(document));

        // When
        Optional<PatientRecordEntry> result = adapter.findEntryByKey(key);

        // Then
        assertTrue(result.isPresent());
        verify(mongoRepository).findByPatientNationalId("12345678");
    }

    @Test
    @DisplayName("Should find all patient records successfully")
    void shouldFindAllPatientRecordsSuccessfully() {
        // Given
        List<MedicalRecordDocument> documents = Arrays.asList(
            createTestMedicalRecordDocument(),
            createTestMedicalRecordDocument()
        );

        when(mongoRepository.findAll()).thenReturn(documents);

        // When
        PatientRecordMap result = adapter.findAll();

        // Then
        assertNotNull(result);
        verify(mongoRepository).findAll();
    }

    @Test
    @DisplayName("Should check if patient record exists successfully")
    void shouldCheckIfPatientRecordExistsSuccessfully() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");

        when(mongoRepository.existsByPatientNationalId("12345678")).thenReturn(true);

        // When
        boolean result = adapter.existsByPatientCedula(cedula);

        // Then
        assertTrue(result);
        verify(mongoRepository).existsByPatientNationalId("12345678");
    }

    @Test
    @DisplayName("Should delete patient record by cedula successfully")
    void shouldDeletePatientRecordByCedulaSuccessfully() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");

        doNothing().when(mongoRepository).deleteByPatientNationalId("12345678");

        // When & Then
        assertDoesNotThrow(() -> adapter.deleteByPatientCedula(cedula));

        verify(mongoRepository).deleteByPatientNationalId("12345678");
    }

    @Test
    @DisplayName("Should count patient records successfully")
    void shouldCountPatientRecordsSuccessfully() {
        // Given
        when(mongoRepository.count()).thenReturn(5L);

        // When
        long result = adapter.count();

        // Then
        assertEquals(5L, result);
        verify(mongoRepository).count();
    }

    // Helper methods
    private PatientRecordMap createTestPatientRecordMap() {
        return PatientRecordMap.empty();
    }

    private MedicalRecordDocument createTestMedicalRecordDocument() {
        return new MedicalRecordDocument("12345678", Arrays.asList());
    }

    private PatientRecordKey createTestPatientRecordKey() {
        PatientCedula cedula = PatientCedula.of("12345678");
        PatientRecordDate date = PatientRecordDate.of(LocalDate.now());
        return PatientRecordKey.of(cedula, date);
    }
}