package app.clinic.domain.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.valueobject.OrderNumber;

class BillingTest {

    @Test
    void shouldCreateBillingWithValidData() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        String patientName = "John Doe";
        int age = 30;
        String identificationNumber = "123456789";
        String doctorName = "Dr. Smith";
        String company = "Insurance Co";
        String policyNumber = "POL123";
        int validityDays = 30;
        LocalDate validityDate = LocalDate.now().plusDays(30);
        double totalCost = 100000.0;
        double copay = 50000.0;
        double insuranceCoverage = 50000.0;
        String appliedMedications = "Aspirin";
        String appliedProcedures = "Checkup";
        String appliedDiagnosticAids = "X-Ray";

        // When
        LocalDateTime generatedAt = LocalDateTime.now();
        String generatedBy = "admin";
        Billing billing = new Billing(orderNumber, patientName, age, identificationNumber, doctorName, company, policyNumber, validityDays, validityDate, totalCost, copay, insuranceCoverage, appliedMedications, appliedProcedures, appliedDiagnosticAids, generatedAt, generatedBy);

        // Then
        assertEquals(orderNumber, billing.getOrderNumber());
        assertEquals(patientName, billing.getPatientName());
        assertEquals(age, billing.getAge());
        assertEquals(identificationNumber, billing.getIdentificationNumber());
        assertEquals(doctorName, billing.getDoctorName());
        assertEquals(company, billing.getCompany());
        assertEquals(policyNumber, billing.getPolicyNumber());
        assertEquals(validityDays, billing.getValidityDays());
        assertEquals(validityDate, billing.getValidityDate());
        assertEquals(totalCost, billing.getTotalCost());
        assertEquals(copay, billing.getCopay());
        assertEquals(insuranceCoverage, billing.getInsuranceCoverage());
        assertEquals(appliedMedications, billing.getAppliedMedications());
        assertEquals(appliedProcedures, billing.getAppliedProcedures());
        assertEquals(appliedDiagnosticAids, billing.getAppliedDiagnosticAids());
    }

    @Test
    void shouldBeEqualBasedOnOrderNumber() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        LocalDate validityDate = LocalDate.now().plusDays(30);

        LocalDateTime generatedAt = LocalDateTime.now();
        String generatedBy = "admin";
        Billing billing1 = new Billing(orderNumber, "John Doe", 30, "123456789", "Dr. Smith", "Insurance Co", "POL123", 30, validityDate, 100000.0, 50000.0, 50000.0, "Aspirin", "Checkup", "X-Ray", generatedAt, generatedBy);
        Billing billing2 = new Billing(orderNumber, "Jane Doe", 25, "987654321", "Dr. Jones", "Other Co", "POL456", 60, validityDate, 150000.0, 75000.0, 75000.0, "Ibuprofen", "Surgery", "MRI", generatedAt, generatedBy);

        // When & Then
        assertEquals(billing1, billing2);
        assertEquals(billing1.hashCode(), billing2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentOrderNumbers() {
        // Given
        OrderNumber orderNumber1 = new OrderNumber("000001");
        OrderNumber orderNumber2 = new OrderNumber("000002");
        LocalDate validityDate = LocalDate.now().plusDays(30);

        LocalDateTime generatedAt = LocalDateTime.now();
        String generatedBy = "admin";
        Billing billing1 = new Billing(orderNumber1, "John Doe", 30, "123456789", "Dr. Smith", "Insurance Co", "POL123", 30, validityDate, 100000.0, 50000.0, 50000.0, "Aspirin", "Checkup", "X-Ray", generatedAt, generatedBy);
        Billing billing2 = new Billing(orderNumber2, "John Doe", 30, "123456789", "Dr. Smith", "Insurance Co", "POL123", 30, validityDate, 100000.0, 50000.0, 50000.0, "Aspirin", "Checkup", "X-Ray", generatedAt, generatedBy);

        // When & Then
        assertNotEquals(billing1, billing2);
    }
}