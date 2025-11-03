package app.clinic.domain.service;

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

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.InventoryRepository;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private RoleBasedAccessService roleBasedAccessService;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryService(inventoryRepository, roleBasedAccessService);
    }

    @Test
    void shouldAddMedicationSuccessfully() {
        // Given
        String id = "1234567925";
        String name = "Aspirin";
        double cost = 10000.0;
        boolean requiresSpecialist = false;
        String specialistType = null;

        when(inventoryRepository.existsMedicationById(any(Id.class))).thenReturn(false);

        // When
        Medication medication = inventoryService.addMedication(id, name, cost, requiresSpecialist, specialistType);

        // Then
        assertNotNull(medication);
        verify(inventoryRepository).saveMedication(medication);
    }

    @Test
    void shouldThrowExceptionWhenMedicationAlreadyExists() {
        // Given
        when(inventoryRepository.existsMedicationById(any(Id.class))).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.addMedication("1234567926", "Aspirin", 10000.0, false, null));
    }

    @Test
    void shouldAddProcedureSuccessfully() {
        // Given
        String id = "123456789";
        String name = "Checkup";
        double cost = 20000.0;
        boolean requiresSpecialist = false;
        String specialistType = null;

        when(inventoryRepository.existsProcedureById(any(Id.class))).thenReturn(false);

        // When
        Procedure procedure = inventoryService.addProcedure(id, name, cost, requiresSpecialist, specialistType);

        // Then
        assertNotNull(procedure);
        verify(inventoryRepository).saveProcedure(procedure);
    }

    @Test
    void shouldThrowExceptionWhenProcedureAlreadyExists() {
        // Given
        when(inventoryRepository.existsProcedureById(any(Id.class))).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.addProcedure("1234567928", "Checkup", 20000.0, true, "CARDIOLOGO"));
    }

    @Test
    void shouldUpdateProcedureSuccessfully() {
        // Given
        Procedure existingProcedure = mock(Procedure.class);
        when(inventoryRepository.findProcedureById(any(Id.class))).thenReturn(java.util.Optional.of(existingProcedure));

        // When
        inventoryService.updateProcedure("1234567937", "Updated Checkup", 25000.0, false, null);

        // Then
        verify(inventoryRepository).saveProcedure(any(Procedure.class));
    }

    @Test
    void shouldAddDiagnosticAidSuccessfully() {
        // Given
        String id = "1234567928";
        String name = "X-Ray";
        double cost = 50000.0;
        boolean requiresSpecialist = false;
        String specialistType = null;

        when(inventoryRepository.existsDiagnosticAidById(any(Id.class))).thenReturn(false);

        // When
        DiagnosticAid diagnosticAid = inventoryService.addDiagnosticAid(id, name, cost, requiresSpecialist, specialistType);

        // Then
        assertNotNull(diagnosticAid);
        verify(inventoryRepository).saveDiagnosticAid(diagnosticAid);
    }

    @Test
    void shouldUpdateMedicationSuccessfully() {
        // Given
        Medication existingMedication = mock(Medication.class);
        when(inventoryRepository.findMedicationById(any(Id.class))).thenReturn(java.util.Optional.of(existingMedication));

        // When
        inventoryService.updateMedication("123456789", "Updated Aspirin", 15000.0, false, null);

        // Then
        verify(inventoryRepository).saveMedication(any(Medication.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentMedication() {
        // Given
        when(inventoryRepository.findMedicationById(any(Id.class))).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.updateMedication("1234567930", "Updated Aspirin", 15000.0, true, "CARDIOLOGO"));
    }

    @Test
    void shouldDeleteMedicationSuccessfully() {
        // Given
        when(inventoryRepository.existsMedicationById(any(Id.class))).thenReturn(true);

        // When
        inventoryService.deleteMedication("1234567931");

        // Then
        // No exception thrown, but in practice, check if in use
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentMedication() {
        // Given
        when(inventoryRepository.existsMedicationById(any(Id.class))).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.deleteMedication("1234567932"));
    }
}