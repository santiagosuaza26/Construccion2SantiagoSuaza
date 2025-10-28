package app.clinic.domain.model.entities;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class InsuranceTest {

    @Test
    void shouldCreateInsuranceWithValidData() {
        // Given
        String companyName = "Insurance Co";
        String policyNumber = "POL123";
        boolean active = true;
        LocalDate validityDate = LocalDate.now().plusDays(30);

        // When
        Insurance insurance = new Insurance(companyName, policyNumber, active, validityDate);

        // Then
        assertEquals(companyName, insurance.getCompanyName());
        assertEquals(policyNumber, insurance.getPolicyNumber());
        assertTrue(insurance.isActive());
        assertEquals(validityDate, insurance.getValidityDate());
    }

    @Test
    void shouldThrowExceptionForNullCompanyName() {
        // Given
        String companyName = null;
        String policyNumber = "POL123";
        boolean active = true;
        LocalDate validityDate = LocalDate.now().plusDays(30);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Insurance(companyName, policyNumber, active, validityDate));
    }

    @Test
    void shouldThrowExceptionForEmptyCompanyName() {
        // Given
        String companyName = "";
        String policyNumber = "POL123";
        boolean active = true;
        LocalDate validityDate = LocalDate.now().plusDays(30);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Insurance(companyName, policyNumber, active, validityDate));
    }

    @Test
    void shouldThrowExceptionForNullPolicyNumber() {
        // Given
        String companyName = "Insurance Co";
        String policyNumber = null;
        boolean active = true;
        LocalDate validityDate = LocalDate.now().plusDays(30);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Insurance(companyName, policyNumber, active, validityDate));
    }

    @Test
    void shouldBeEqualBasedOnAllFields() {
        // Given
        String companyName = "Insurance Co";
        String policyNumber = "POL123";
        boolean active = true;
        LocalDate validityDate = LocalDate.now().plusDays(30);

        Insurance insurance1 = new Insurance(companyName, policyNumber, active, validityDate);
        Insurance insurance2 = new Insurance(companyName, policyNumber, active, validityDate);

        // When & Then
        assertEquals(insurance1, insurance2);
        assertEquals(insurance1.hashCode(), insurance2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentActiveStatus() {
        // Given
        String companyName = "Insurance Co";
        String policyNumber = "POL123";
        LocalDate validityDate = LocalDate.now().plusDays(30);

        Insurance insurance1 = new Insurance(companyName, policyNumber, true, validityDate);
        Insurance insurance2 = new Insurance(companyName, policyNumber, false, validityDate);

        // When & Then
        assertNotEquals(insurance1, insurance2);
    }
}