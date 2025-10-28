package app.clinic.domain.model.entities;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.valueobject.OrderNumber;

class OrderTest {

    @Test
    void shouldCreateOrderWithValidData() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();
        String diagnosis = "Hypertension";

        // When
        Order order = new Order(orderNumber, patientId, doctorId, date, diagnosis);

        // Then
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(patientId, order.getPatientIdentificationNumber());
        assertEquals(doctorId, order.getDoctorIdentificationNumber());
        assertEquals(date, order.getDate());
        assertEquals(diagnosis, order.getDiagnosis());
        assertTrue(order.getMedications().isEmpty());
        assertTrue(order.getProcedures().isEmpty());
        assertTrue(order.getDiagnosticAids().isEmpty());
    }

    @Test
    void shouldCreateOrderWithoutDiagnosis() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();

        // When
        Order order = new Order(orderNumber, patientId, doctorId, date);

        // Then
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(patientId, order.getPatientIdentificationNumber());
        assertEquals(doctorId, order.getDoctorIdentificationNumber());
        assertEquals(date, order.getDate());
        assertNull(order.getDiagnosis());
    }

    @Test
    void shouldAddMedicationToOrder() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();
        Order order = new Order(orderNumber, patientId, doctorId, date);
        MedicationOrder medication = new MedicationOrder(orderNumber, 1, new app.clinic.domain.model.valueobject.Id("1234567915"), "10mg", "7 days", 10000.0);

        // When
        order.addMedication(medication);

        // Then
        assertEquals(1, order.getMedications().size());
        assertEquals(medication, order.getMedications().get(0));
    }

    @Test
    void shouldAddProcedureToOrder() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();
        Order order = new Order(orderNumber, patientId, doctorId, date);
        ProcedureOrder procedure = new ProcedureOrder(orderNumber, 1, new app.clinic.domain.model.valueobject.Id("1234567916"), "1", "Daily", false, null, 20000.0);

        // When
        order.addProcedure(procedure);

        // Then
        assertEquals(1, order.getProcedures().size());
        assertEquals(procedure, order.getProcedures().get(0));
    }

    @Test
    void shouldAddDiagnosticAidToOrder() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();
        Order order = new Order(orderNumber, patientId, doctorId, date);
        DiagnosticAidOrder aid = new DiagnosticAidOrder(orderNumber, 1, new app.clinic.domain.model.valueobject.Id("1234567917"), "1", false, null, 50000.0);

        // When
        order.addDiagnosticAid(aid);

        // Then
        assertEquals(1, order.getDiagnosticAids().size());
        assertEquals(aid, order.getDiagnosticAids().get(0));
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateItem() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();
        Order order = new Order(orderNumber, patientId, doctorId, date);
        MedicationOrder medication1 = new MedicationOrder(orderNumber, 1, new app.clinic.domain.model.valueobject.Id("1234567918"), "10mg", "7 days", 10000.0);
        MedicationOrder medication2 = new MedicationOrder(orderNumber, 1, new app.clinic.domain.model.valueobject.Id("1234567919"), "20mg", "5 days", 15000.0);

        order.addMedication(medication1);

        // When & Then
        assertThrows(app.clinic.domain.model.InvalidOrderStateException.class, () -> order.addMedication(medication2));
    }

    @Test
    void shouldThrowExceptionWhenAddingMedicationToDiagnosticAidOrder() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();
        Order order = new Order(orderNumber, patientId, doctorId, date);
        DiagnosticAidOrder aid = new DiagnosticAidOrder(orderNumber, 1, new app.clinic.domain.model.valueobject.Id("1234567920"), "1", false, null, 50000.0);
        order.addDiagnosticAid(aid);
        MedicationOrder medication = new MedicationOrder(orderNumber, 2, new app.clinic.domain.model.valueobject.Id("1234567921"), "10mg", "7 days", 10000.0);

        // When & Then
        assertThrows(app.clinic.domain.model.InvalidOrderStateException.class, () -> order.addMedication(medication));
    }

    @Test
    void shouldBeEqualBasedOnOrderNumber() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientId = "123456789";
        String doctorId = "987654321";
        LocalDate date = LocalDate.now();

        Order order1 = new Order(orderNumber, patientId, doctorId, date);
        Order order2 = new Order(orderNumber, patientId, doctorId, date);

        // When & Then
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }
}