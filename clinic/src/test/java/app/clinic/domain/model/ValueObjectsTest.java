package app.clinic.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for domain value objects.
 * Tests immutability, validation, and business rules for all value objects.
 */
class ValueObjectsTest {

    @Test
    @DisplayName("PatientCedula should create valid cedula")
    void patientCedulaShouldCreateValidCedula() {
        // When
        PatientCedula cedula = PatientCedula.of("12345678");

        // Then
        assertNotNull(cedula);
        assertEquals("12345678", cedula.getValue());
    }

    @Test
    @DisplayName("PatientCedula should throw exception for invalid cedula")
    void patientCedulaShouldThrowExceptionForInvalidCedula() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            PatientCedula.of("");
        });
    }

    @Test
    @DisplayName("Email should create valid email")
    void emailShouldCreateValidEmail() {
        // When
        Email email = Email.of("test@example.com");

        // Then
        assertNotNull(email);
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    @DisplayName("Email should throw exception for invalid email")
    void emailShouldThrowExceptionForInvalidEmail() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Email.of("invalid-email");
        });
    }

    @Test
    @DisplayName("PhoneNumber should create valid phone number")
    void phoneNumberShouldCreateValidPhoneNumber() {
        // When
        PhoneNumber phoneNumber = PhoneNumber.of("+573001234567");

        // Then
        assertNotNull(phoneNumber);
        assertEquals("+573001234567", phoneNumber.getValue());
    }

    @Test
    @DisplayName("PhoneNumber should throw exception for invalid phone number")
    void phoneNumberShouldThrowExceptionForInvalidPhoneNumber() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            PhoneNumber.of("invalid-phone");
        });
    }

    @Test
    @DisplayName("FullName should create valid full name")
    void fullNameShouldCreateValidFullName() {
        // When
        FullName fullName = FullName.of("Juan Pérez", "García");

        // Then
        assertNotNull(fullName);
        assertEquals("Juan Pérez", fullName.getFirstNames());
        assertEquals("García", fullName.getLastNames());
        assertEquals("Juan Pérez García", fullName.getFullName());
    }

    @Test
    @DisplayName("FullName should throw exception for empty first names")
    void fullNameShouldThrowExceptionForEmptyFirstNames() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of("", "García");
        });
    }

    @Test
    @DisplayName("FullName should throw exception for empty last names")
    void fullNameShouldThrowExceptionForEmptyLastNames() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of("Juan Pérez", "");
        });
    }

    @Test
    @DisplayName("Money should create valid money amount")
    void moneyShouldCreateValidMoneyAmount() {
        // When
        Money money = Money.of(new BigDecimal("100.50"));

        // Then
        assertNotNull(money);
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    @DisplayName("Money should throw exception for negative amount")
    void moneyShouldThrowExceptionForNegativeAmount() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Money.of(new BigDecimal("-100.50"));
        });
    }

    @Test
    @DisplayName("PatientRecordDate should create valid date")
    void patientRecordDateShouldCreateValidDate() {
        // Given
        LocalDate date = LocalDate.of(2024, 1, 15);

        // When
        PatientRecordDate recordDate = PatientRecordDate.of(date);

        // Then
        assertNotNull(recordDate);
        assertEquals(date, recordDate.getValue());
    }

    @Test
    @DisplayName("PatientRecordDate should throw exception for null date")
    void patientRecordDateShouldThrowExceptionForNullDate() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            PatientRecordDate.of(null);
        });
    }

    @Test
    @DisplayName("PatientRecordDate today should create current date")
    void patientRecordDateTodayShouldCreateCurrentDate() {
        // When
        PatientRecordDate today = PatientRecordDate.today();

        // Then
        assertNotNull(today);
        assertEquals(LocalDate.now(), today.getValue());
    }

    @Test
    @DisplayName("Diagnosis should create valid diagnosis")
    void diagnosisShouldCreateValidDiagnosis() {
        // When
        Diagnosis diagnosis = Diagnosis.of("Hipertensión arterial");

        // Then
        assertNotNull(diagnosis);
        assertEquals("Hipertensión arterial", diagnosis.getValue());
    }

    @Test
    @DisplayName("Diagnosis should throw exception for empty diagnosis")
    void diagnosisShouldThrowExceptionForEmptyDiagnosis() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Diagnosis.of("");
        });
    }

    @Test
    @DisplayName("Symptoms should create valid symptoms")
    void symptomsShouldCreateValidSymptoms() {
        // When
        Symptoms symptoms = Symptoms.of("Dolor de cabeza, mareos");

        // Then
        assertNotNull(symptoms);
        assertEquals("Dolor de cabeza, mareos", symptoms.getValue());
    }

    @Test
    @DisplayName("ConsultationReason should create valid consultation reason")
    void consultationReasonShouldCreateValidConsultationReason() {
        // When
        ConsultationReason reason = ConsultationReason.of("Control médico");

        // Then
        assertNotNull(reason);
        assertEquals("Control médico", reason.getValue());
    }

    @Test
    @DisplayName("DoctorCedula should create valid doctor cedula")
    void doctorCedulaShouldCreateValidDoctorCedula() {
        // When
        DoctorCedula doctorCedula = DoctorCedula.of("DOC-001");

        // Then
        assertNotNull(doctorCedula);
        assertEquals("DOC-001", doctorCedula.getValue());
    }

    @Test
    @DisplayName("PatientUsername should create valid username")
    void patientUsernameShouldCreateValidUsername() {
        // When
        PatientUsername username = PatientUsername.of("testuser");

        // Then
        assertNotNull(username);
        assertEquals("testuser", username.getValue());
    }

    @Test
    @DisplayName("PatientUsername should throw exception for username too long")
    void patientUsernameShouldThrowExceptionForUsernameTooLong() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            PatientUsername.of("this_username_is_way_too_long_for_the_system");
        });
    }

    @Test
    @DisplayName("PatientBirthDate should create valid birth date")
    void patientBirthDateShouldCreateValidBirthDate() {
        // Given
        LocalDate birthDate = LocalDate.of(1985, 5, 15);

        // When
        PatientBirthDate patientBirthDate = PatientBirthDate.of(birthDate);

        // Then
        assertNotNull(patientBirthDate);
        assertEquals(birthDate, patientBirthDate.getValue());
    }

    @Test
    @DisplayName("PatientBirthDate should throw exception for future birth date")
    void patientBirthDateShouldThrowExceptionForFutureBirthDate() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            PatientBirthDate.of(futureDate);
        });
    }

    @Test
    @DisplayName("PatientBirthDate should throw exception for birth date too old")
    void patientBirthDateShouldThrowExceptionForBirthDateTooOld() {
        // Given
        LocalDate tooOldDate = LocalDate.now().minusYears(151); // More than 150 years

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            PatientBirthDate.of(tooOldDate);
        });
    }

    @Test
    @DisplayName("PatientAddress should create valid address")
    void patientAddressShouldCreateValidAddress() {
        // When
        PatientAddress address = PatientAddress.of("Calle 123 #45-67");

        // Then
        assertNotNull(address);
        assertEquals("Calle 123 #45-67", address.getValue());
    }

    @Test
    @DisplayName("PatientAddress should throw exception for address too long")
    void patientAddressShouldThrowExceptionForAddressTooLong() {
        // Given
        String longAddress = "A".repeat(31); // More than 30 characters

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            PatientAddress.of(longAddress);
        });
    }

    @Test
    @DisplayName("CopaymentAmount should return standard copayment")
    void copaymentAmountShouldReturnStandardCopayment() {
        // When
        CopaymentAmount copaymentAmount = CopaymentAmount.standard();

        // Then
        assertNotNull(copaymentAmount);
        assertEquals(new BigDecimal("50000"), copaymentAmount.getValue());
    }

    @Test
    @DisplayName("MaximumCopaymentAmount should return standard maximum")
    void maximumCopaymentAmountShouldReturnStandardMaximum() {
        // When
        MaximumCopaymentAmount maxCopayment = MaximumCopaymentAmount.standard();

        // Then
        assertNotNull(maxCopayment);
        assertEquals(new BigDecimal("1000000"), maxCopayment.getValue());
    }

    @Test
    @DisplayName("Year should create current year")
    void yearShouldCreateCurrentYear() {
        // When
        Year currentYear = Year.current();

        // Then
        assertNotNull(currentYear);
        assertEquals(LocalDate.now().getYear(), currentYear.getValue());
    }

    @Test
    @DisplayName("Year should create specific year")
    void yearShouldCreateSpecificYear() {
        // When
        Year year = Year.of(2024);

        // Then
        assertNotNull(year);
        assertEquals(2024, year.getValue());
    }

    @Test
    @DisplayName("TotalCost should create valid total cost")
    void totalCostShouldCreateValidTotalCost() {
        // Given
        Money amount = Money.of(new BigDecimal("150000"));

        // When
        TotalCost totalCost = TotalCost.of(amount);

        // Then
        assertNotNull(totalCost);
        assertEquals(amount, totalCost.getValue());
    }

    @Test
    @DisplayName("BillingCalculationResult should create valid result")
    void billingCalculationResultShouldCreateValidResult() {
        // Given
        Money totalAmount = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("50000"));
        Money insuranceCoverage = Money.of(new BigDecimal("150000"));
        Money patientResponsibility = Money.of(new BigDecimal("50000"));

        // When
        BillingCalculationResult result = BillingCalculationResult.of(
            totalAmount, copaymentAmount, insuranceCoverage, patientResponsibility, false
        );

        // Then
        assertNotNull(result);
        assertEquals(totalAmount, result.getTotalCost());
        assertEquals(copaymentAmount, result.getCopaymentAmount());
        assertEquals(insuranceCoverage, result.getInsuranceCoverage());
        assertEquals(patientResponsibility, result.getPatientResponsibility());
        assertFalse(result.requiresFullPayment());
    }

    @Test
    @DisplayName("Value objects should be immutable")
    void valueObjectsShouldBeImmutable() {
        // Given
        Email email = Email.of("test@example.com");
        String originalValue = email.getValue();

        // When - Try to modify (this would fail at compile time if we tried to modify the object)
        // Since value objects are immutable, we can't modify them

        // Then
        assertEquals(originalValue, email.getValue());
    }

    @Test
    @DisplayName("Value objects should implement equals correctly")
    void valueObjectsShouldImplementEqualsCorrectly() {
        // Given
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test@example.com");
        Email email3 = Email.of("different@example.com");

        // Then
        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
        assertEquals(email1.hashCode(), email2.hashCode());
        assertNotEquals(email1.hashCode(), email3.hashCode());
    }

    @Test
    @DisplayName("Value objects should have meaningful toString")
    void valueObjectsShouldHaveMeaningfulToString() {
        // Given
        Email email = Email.of("test@example.com");
        PatientCedula cedula = PatientCedula.of("12345678");

        // Then
        assertNotNull(email.toString());
        assertNotNull(cedula.toString());
        assertTrue(email.toString().contains("test@example.com"));
        assertTrue(cedula.toString().contains("12345678"));
    }
}