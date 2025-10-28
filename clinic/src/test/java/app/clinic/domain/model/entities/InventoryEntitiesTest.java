package app.clinic.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.valueobject.Id;

class InventoryEntitiesTest {

    @Test
    void shouldCreateMedicationWithValidData() {
        // Given
        Id id = new Id("1234567890");
        String name = "Aspirin";
        double cost = 10000.0;
        boolean requiresSpecialist = false;
        Id specialistType = null;

        // When
        Medication medication = new Medication(id, name, cost, requiresSpecialist, specialistType);

        // Then
        assertEquals(id, medication.getId());
        assertEquals(name, medication.getName());
        assertEquals(cost, medication.getCost());
        assertFalse(medication.isRequiresSpecialist());
        assertNull(medication.getSpecialistType());
    }

    @Test
    void shouldCreateMedicationWithSpecialist() {
        // Given
        Id id = new Id("1234567891");
        String name = "Special Drug";
        double cost = 50000.0;
        boolean requiresSpecialist = true;
        Id specialistType = new Id("1234567892");

        // When
        Medication medication = new Medication(id, name, cost, requiresSpecialist, specialistType);

        // Then
        assertEquals(id, medication.getId());
        assertEquals(name, medication.getName());
        assertEquals(cost, medication.getCost());
        assertTrue(medication.isRequiresSpecialist());
        assertEquals(specialistType, medication.getSpecialistType());
    }

    @Test
    void shouldCreateProcedureWithValidData() {
        // Given
        Id id = new Id("1234567893");
        String name = "Checkup";
        double cost = 20000.0;
        boolean requiresSpecialist = false;
        Id specialistType = null;

        // When
        Procedure procedure = new Procedure(id, name, cost, requiresSpecialist, specialistType);

        // Then
        assertEquals(id, procedure.getId());
        assertEquals(name, procedure.getName());
        assertEquals(cost, procedure.getCost());
        assertFalse(procedure.isRequiresSpecialist());
        assertNull(procedure.getSpecialistType());
    }

    @Test
    void shouldCreateDiagnosticAidWithValidData() {
        // Given
        Id id = new Id("1234567894");
        String name = "X-Ray";
        double cost = 50000.0;
        boolean requiresSpecialist = true;
        Id specialistType = new Id("1234567895");

        // When
        DiagnosticAid diagnosticAid = new DiagnosticAid(id, name, cost, requiresSpecialist, specialistType);

        // Then
        assertEquals(id, diagnosticAid.getId());
        assertEquals(name, diagnosticAid.getName());
        assertEquals(cost, diagnosticAid.getCost());
        assertTrue(diagnosticAid.isRequiresSpecialist());
        assertEquals(specialistType, diagnosticAid.getSpecialistType());
    }

    @Test
    void shouldBeEqualBasedOnIdForMedication() {
        // Given
        Id id = new Id("1234567896");
        String name = "Aspirin";
        double cost = 10000.0;
        boolean requiresSpecialist = false;
        Id specialistType = null;

        Medication medication1 = new Medication(id, name, cost, requiresSpecialist, specialistType);
        Medication medication2 = new Medication(id, "Different Name", 20000.0, true, new Id("1234567897"));

        // When & Then
        assertEquals(medication1, medication2);
        assertEquals(medication1.hashCode(), medication2.hashCode());
    }

    @Test
    void shouldBeEqualBasedOnIdForProcedure() {
        // Given
        Id id = new Id("1234567898");
        String name = "Checkup";
        double cost = 20000.0;
        boolean requiresSpecialist = false;
        Id specialistType = null;

        Procedure procedure1 = new Procedure(id, name, cost, requiresSpecialist, specialistType);
        Procedure procedure2 = new Procedure(id, "Different Name", 30000.0, true, new Id("1234567899"));

        // When & Then
        assertEquals(procedure1, procedure2);
        assertEquals(procedure1.hashCode(), procedure2.hashCode());
    }

    @Test
    void shouldBeEqualBasedOnIdForDiagnosticAid() {
        // Given
        Id id = new Id("1234567900");
        String name = "X-Ray";
        double cost = 50000.0;
        boolean requiresSpecialist = true;
        Id specialistType = new Id("1234567901");

        DiagnosticAid aid1 = new DiagnosticAid(id, name, cost, requiresSpecialist, specialistType);
        DiagnosticAid aid2 = new DiagnosticAid(id, "Different Name", 60000.0, false, null);

        // When & Then
        assertEquals(aid1, aid2);
        assertEquals(aid1.hashCode(), aid2.hashCode());
    }
}