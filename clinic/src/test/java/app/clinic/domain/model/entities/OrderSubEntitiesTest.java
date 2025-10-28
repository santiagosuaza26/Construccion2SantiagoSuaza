package app.clinic.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;

class OrderSubEntitiesTest {

    @Test
    void shouldCreateMedicationOrderWithValidData() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        int item = 1;
        Id medicationId = new Id("1234567902");
        String dosage = "10mg";
        String duration = "7 days";
        double cost = 10000.0;

        // When
        MedicationOrder medicationOrder = new MedicationOrder(orderNumber, item, medicationId, dosage, duration, cost);

        // Then
        assertEquals(orderNumber, medicationOrder.getOrderNumber());
        assertEquals(item, medicationOrder.getItem());
        assertEquals(medicationId, medicationOrder.getMedicationId());
        assertEquals(dosage, medicationOrder.getDosage());
        assertEquals(duration, medicationOrder.getDuration());
        assertEquals(cost, medicationOrder.getCost());
    }

    @Test
    void shouldCreateProcedureOrderWithValidData() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        int item = 1;
        Id procedureId = new Id("1234567903");
        String quantity = "1";
        String frequency = "Daily";
        boolean requiresSpecialist = false;
        Id specialistId = null;
        double cost = 20000.0;

        // When
        ProcedureOrder procedureOrder = new ProcedureOrder(orderNumber, item, procedureId, quantity, frequency, requiresSpecialist, specialistId, cost);

        // Then
        assertEquals(orderNumber, procedureOrder.getOrderNumber());
        assertEquals(item, procedureOrder.getItem());
        assertEquals(procedureId, procedureOrder.getProcedureId());
        assertEquals(quantity, procedureOrder.getQuantity());
        assertEquals(frequency, procedureOrder.getFrequency());
        assertFalse(procedureOrder.isRequiresSpecialist());
        assertNull(procedureOrder.getSpecialistId());
        assertEquals(cost, procedureOrder.getCost());
    }

    @Test
    void shouldCreateProcedureOrderWithSpecialist() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        int item = 1;
        Id procedureId = new Id("1234567904");
        String quantity = "1";
        String frequency = "Weekly";
        boolean requiresSpecialist = true;
        Id specialistId = new Id("1234567905");
        double cost = 50000.0;

        // When
        ProcedureOrder procedureOrder = new ProcedureOrder(orderNumber, item, procedureId, quantity, frequency, requiresSpecialist, specialistId, cost);

        // Then
        assertEquals(orderNumber, procedureOrder.getOrderNumber());
        assertEquals(item, procedureOrder.getItem());
        assertEquals(procedureId, procedureOrder.getProcedureId());
        assertEquals(quantity, procedureOrder.getQuantity());
        assertEquals(frequency, procedureOrder.getFrequency());
        assertTrue(procedureOrder.isRequiresSpecialist());
        assertEquals(specialistId, procedureOrder.getSpecialistId());
        assertEquals(cost, procedureOrder.getCost());
    }

    @Test
    void shouldCreateDiagnosticAidOrderWithValidData() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        int item = 1;
        Id diagnosticAidId = new Id("1234567906");
        String quantity = "1";
        boolean requiresSpecialist = false;
        Id specialistId = null;
        double cost = 50000.0;

        // When
        DiagnosticAidOrder diagnosticAidOrder = new DiagnosticAidOrder(orderNumber, item, diagnosticAidId, quantity, requiresSpecialist, specialistId, cost);

        // Then
        assertEquals(orderNumber, diagnosticAidOrder.getOrderNumber());
        assertEquals(item, diagnosticAidOrder.getItem());
        assertEquals(diagnosticAidId, diagnosticAidOrder.getDiagnosticAidId());
        assertEquals(quantity, diagnosticAidOrder.getQuantity());
        assertFalse(diagnosticAidOrder.isRequiresSpecialist());
        assertNull(diagnosticAidOrder.getSpecialistId());
        assertEquals(cost, diagnosticAidOrder.getCost());
    }

    @Test
    void shouldBeEqualBasedOnOrderNumberAndItemForMedicationOrder() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        int item = 1;
        Id medicationId = new Id("1234567907");
        String dosage = "10mg";
        String duration = "7 days";
        double cost = 10000.0;

        MedicationOrder order1 = new MedicationOrder(orderNumber, item, medicationId, dosage, duration, cost);
        MedicationOrder order2 = new MedicationOrder(orderNumber, item, new Id("1234567908"), "20mg", "5 days", 15000.0);

        // When & Then
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void shouldBeEqualBasedOnOrderNumberAndItemForProcedureOrder() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        int item = 1;
        Id procedureId = new Id("1234567909");
        String quantity = "1";
        String frequency = "Daily";
        boolean requiresSpecialist = false;
        Id specialistId = null;
        double cost = 20000.0;

        ProcedureOrder order1 = new ProcedureOrder(orderNumber, item, procedureId, quantity, frequency, requiresSpecialist, specialistId, cost);
        ProcedureOrder order2 = new ProcedureOrder(orderNumber, item, new Id("1234567910"), "2", "Weekly", true, new Id("1234567911"), 30000.0);

        // When & Then
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void shouldBeEqualBasedOnOrderNumberAndItemForDiagnosticAidOrder() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        int item = 1;
        Id diagnosticAidId = new Id("1234567912");
        String quantity = "1";
        boolean requiresSpecialist = false;
        Id specialistId = null;
        double cost = 50000.0;

        DiagnosticAidOrder order1 = new DiagnosticAidOrder(orderNumber, item, diagnosticAidId, quantity, requiresSpecialist, specialistId, cost);
        DiagnosticAidOrder order2 = new DiagnosticAidOrder(orderNumber, item, new Id("1234567913"), "2", true, new Id("1234567914"), 60000.0);

        // When & Then
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }
}