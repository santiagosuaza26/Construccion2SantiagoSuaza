package app.clinic.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas comprehensivas para Value Objects adicionales del dominio médico.
 * Valida las reglas de negocio críticas para cada Value Object.
 */
@DisplayName("Value Objects Comprehensive Tests")
class ValueObjectsComprehensiveTest {

    @Nested
    @DisplayName("Validaciones de Información Personal")
    class PersonalInformationValidationTests {

        @Test
        @DisplayName("Debe validar nombres completos correctamente")
        void shouldValidateFullNameCorrectly() {
            // Given
            String firstName = "María";
            String lastName = "González";

            // When & Then
            assertThatCode(() -> FullName.of(firstName, lastName))
                .doesNotThrowAnyException();

            // Given
            String emptyFirstName = "";

            // When & Then
            assertThatThrownBy(() -> FullName.of(emptyFirstName, lastName))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar direcciones correctamente")
        void shouldValidateAddressCorrectly() {
            // Given
            String validAddress = "Calle 123 #45-67";

            // When & Then
            assertThatCode(() -> Address.of(validAddress))
                .doesNotThrowAnyException();

            // Given
            String emptyAddress = "";

            // When & Then
            assertThatThrownBy(() -> Address.of(emptyAddress))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar fechas de nacimiento correctamente")
        void shouldValidateBirthDateCorrectly() {
            // Given
            LocalDate validBirthDate = LocalDate.of(1990, 5, 15);

            // When & Then
            assertThatCode(() -> BirthDate.of(validBirthDate))
                .doesNotThrowAnyException();

            // Given
            LocalDate futureDate = LocalDate.now().plusDays(1);

            // When & Then
            assertThatThrownBy(() -> BirthDate.of(futureDate))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar géneros correctamente")
        void shouldValidateGenderCorrectly() {
            // When & Then - Gender is an enum, just test valid values exist
            assertThat(Gender.MASCULINO).isNotNull();
            assertThat(Gender.FEMENINO).isNotNull();
            assertThat(Gender.OTRO).isNotNull();
        }
    }

    @Nested
    @DisplayName("Validaciones de Información Médica")
    class MedicalInformationValidationTests {

        @Test
        @DisplayName("Debe validar razones de consulta correctamente")
        void shouldValidateConsultationReasonCorrectly() {
            // Given
            String validReason = "Dolor de cabeza crónico";

            // When & Then
            assertThatCode(() -> ConsultationReason.of(validReason))
                .doesNotThrowAnyException();

            // Given
            String emptyReason = "";

            // When & Then
            assertThatThrownBy(() -> ConsultationReason.of(emptyReason))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar diagnósticos correctamente")
        void shouldValidateDiagnosisCorrectly() {
            // Given
            String validDiagnosis = "Hipertensión Arterial";

            // When & Then
            assertThatCode(() -> Diagnosis.of(validDiagnosis))
                .doesNotThrowAnyException();

            // Given
            String nullDiagnosis = null;

            // When & Then
            assertThatThrownBy(() -> Diagnosis.of(nullDiagnosis))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar signos vitales correctamente")
        void shouldValidateVitalSignsCorrectly() {
            // Given
            String validBloodPressure = "120/80";

            // When & Then
            assertThatCode(() -> BloodPressure.of(validBloodPressure))
                .doesNotThrowAnyException();

            // Given
            String invalidBloodPressure = "300/80";

            // When & Then
            assertThatThrownBy(() -> BloodPressure.of(invalidBloodPressure))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Validaciones de Información Financiera")
    class FinancialInformationValidationTests {

        @Test
        @DisplayName("Debe validar montos de copago correctamente")
        void shouldValidateCopaymentAmountCorrectly() {
            // Given
            BigDecimal validAmount = new BigDecimal("50000");

            // When & Then
            assertThatCode(() -> CopaymentAmount.of(validAmount))
                .doesNotThrowAnyException();

            // Given
            BigDecimal negativeAmount = new BigDecimal("-1000");

            // When & Then
            assertThatThrownBy(() -> CopaymentAmount.of(negativeAmount))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar costos correctamente")
        void shouldValidateCostCorrectly() {
            // Given
            BigDecimal validCost = new BigDecimal("150000");

            // When & Then
            assertThatCode(() -> Cost.of(validCost))
                .doesNotThrowAnyException();

            // Given
            BigDecimal zeroCost = BigDecimal.ZERO;

            // When & Then
            assertThatThrownBy(() -> Cost.of(zeroCost))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Validaciones de Información de Contacto")
    class ContactInformationValidationTests {

        @Test
        @DisplayName("Debe validar emails correctamente")
        void shouldValidateEmailCorrectly() {
            // Given
            String validEmail = "paciente@clinica.com";

            // When & Then
            assertThatCode(() -> Email.of(validEmail))
                .doesNotThrowAnyException();

            // Given
            String invalidEmail = "email-invalido";

            // When & Then
            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar números de teléfono correctamente")
        void shouldValidatePhoneNumberCorrectly() {
            // Given
            String validPhone = "3001234567";

            // When & Then
            assertThatCode(() -> PhoneNumber.of(validPhone))
                .doesNotThrowAnyException();

            // Given
            String shortPhone = "123";

            // When & Then
            assertThatThrownBy(() -> PhoneNumber.of(shortPhone))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar contactos de emergencia correctamente")
        void shouldValidateEmergencyContactCorrectly() {
            // Given
            EmergencyContactName name = EmergencyContactName.of("Juan", "Pérez");
            Relationship relationship = Relationship.PADRE;
            EmergencyContactPhoneNumber phone = EmergencyContactPhoneNumber.of("3009876543");

            // When & Then
            assertThatCode(() -> EmergencyContact.of(name, relationship, phone))
                .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Validaciones de Información de Órdenes Médicas")
    class MedicalOrderValidationTests {

        @Test
        @DisplayName("Debe validar dosis correctamente")
        void shouldValidateDosageCorrectly() {
            // Given
            String validDosage = "500mg";

            // When & Then
            assertThatCode(() -> Dosage.of(validDosage))
                .doesNotThrowAnyException();

            // Given
            String emptyDosage = "";

            // When & Then
            assertThatThrownBy(() -> Dosage.of(emptyDosage))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar frecuencias correctamente")
        void shouldValidateFrequencyCorrectly() {
            // Given
            String validFrequency = "Cada 8 horas";

            // When & Then
            assertThatCode(() -> Frequency.of(validFrequency))
                .doesNotThrowAnyException();

            // Given
            String nullFrequency = null;

            // When & Then
            assertThatThrownBy(() -> Frequency.of(nullFrequency))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Validaciones de Información de Inventario")
    class InventoryValidationTests {

        @Test
        @DisplayName("Debe validar IDs de items de inventario correctamente")
        void shouldValidateInventoryItemIdCorrectly() {
            // Given
            String validId = "ITEM-001";

            // When & Then
            assertThatCode(() -> InventoryItemId.of(validId))
                .doesNotThrowAnyException();

            // Given
            String emptyId = "";

            // When & Then
            assertThatThrownBy(() -> InventoryItemId.of(emptyId))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar costos de items de inventario correctamente")
        void shouldValidateInventoryItemCostCorrectly() {
            // Given
            Money validMoney = Money.of(new BigDecimal("25000"));

            // When & Then
            assertThatCode(() -> InventoryItemCost.of(validMoney))
                .doesNotThrowAnyException();

            // Given
            Money negativeMoney = Money.of(new BigDecimal("-5000"));

            // When & Then
            assertThatThrownBy(() -> InventoryItemCost.of(negativeMoney))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Validaciones de Información de Facturación")
    class BillingValidationTests {

        @Test
        @DisplayName("Debe validar fechas de facturación correctamente")
        void shouldValidateBillingDateCorrectly() {
            // Given
            LocalDate validDate = LocalDate.now();

            // When & Then
            assertThatCode(() -> BillingDate.of(validDate))
                .doesNotThrowAnyException();

            // Given
            LocalDate futureDate = LocalDate.now().plusDays(1);

            // When & Then
            assertThatThrownBy(() -> BillingDate.of(futureDate))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe validar números de factura correctamente")
        void shouldValidateInvoiceNumberCorrectly() {
            // Given
            String validNumber = "FAC-2024-001";

            // When & Then
            assertThatCode(() -> InvoiceNumber.of(validNumber))
                .doesNotThrowAnyException();

            // Given
            String emptyNumber = "";

            // When & Then
            assertThatThrownBy(() -> InvoiceNumber.of(emptyNumber))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}