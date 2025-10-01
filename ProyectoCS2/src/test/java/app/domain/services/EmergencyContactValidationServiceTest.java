package app.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import app.domain.exception.DomainValidationException;
import app.domain.model.EmergencyContact;

/**
 * Pruebas unitarias para EmergencyContactValidationService
 * Demuestra el cumplimiento de principios SOLID:
 * - SRP: Una sola responsabilidad (validar contactos de emergencia)
 */
class EmergencyContactValidationServiceTest {

    private EmergencyContactValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new EmergencyContactValidationService();
    }

    @Nested
    @DisplayName("Validaciones básicas de contacto de emergencia")
    class BasicEmergencyContactValidationTests {

        @Test
        @DisplayName("Debe lanzar excepción cuando el contacto es null")
        void shouldThrowExceptionWhenContactIsNull() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(null)
            );

            assertEquals("Emergency contact cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar nombre requerido")
        void shouldValidateFirstNameRequired() {
            // Given
            EmergencyContact contactWithoutFirstName = new EmergencyContact(null, "Pérez", "Hermano", "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithoutFirstName)
            );

            assertEquals("Emergency contact first name is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar apellido requerido")
        void shouldValidateLastNameRequired() {
            // Given
            EmergencyContact contactWithoutLastName = new EmergencyContact("María", null, "Hermana", "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithoutLastName)
            );

            assertEquals("Emergency contact last name is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar relación requerida")
        void shouldValidateRelationshipRequired() {
            // Given
            EmergencyContact contactWithoutRelationship = new EmergencyContact("María", "Pérez", null, "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithoutRelationship)
            );

            assertEquals("Emergency contact relationship is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar teléfono requerido")
        void shouldValidatePhoneRequired() {
            // Given
            EmergencyContact contactWithoutPhone = new EmergencyContact("María", "Pérez", "Hermana", null);

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithoutPhone)
            );

            assertEquals("Emergency contact phone is required", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de longitud de campos")
    class LengthValidationTests {

        @Test
        @DisplayName("Debe rechazar nombre muy largo")
        void shouldRejectFirstNameTooLong() {
            // Given
            String longFirstName = "A".repeat(51); // Más de 50 caracteres
            EmergencyContact contactWithLongFirstName = new EmergencyContact(longFirstName, "Pérez", "Hermano", "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithLongFirstName)
            );

            assertEquals("Emergency contact first name cannot exceed 50 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar apellido muy largo")
        void shouldRejectLastNameTooLong() {
            // Given
            String longLastName = "B".repeat(51); // Más de 50 caracteres
            EmergencyContact contactWithLongLastName = new EmergencyContact("María", longLastName, "Hermana", "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithLongLastName)
            );

            assertEquals("Emergency contact last name cannot exceed 50 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar relación muy larga")
        void shouldRejectRelationshipTooLong() {
            // Given
            String longRelationship = "C".repeat(31); // Más de 30 caracteres
            EmergencyContact contactWithLongRelationship = new EmergencyContact("María", "Pérez", longRelationship, "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithLongRelationship)
            );

            assertEquals("Emergency contact relationship cannot exceed 30 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar nombres en límite de longitud")
        void shouldAcceptNamesAtLengthLimit() {
            // Given
            String firstNameAtLimit = "A".repeat(50);
            String lastNameAtLimit = "B".repeat(50);
            String relationshipAtLimit = "hermano"; // Relación válida
            EmergencyContact contactAtLimits = new EmergencyContact(firstNameAtLimit, lastNameAtLimit, relationshipAtLimit, "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contactAtLimits));
        }
    }

    @Nested
    @DisplayName("Validaciones de relación")
    class RelationshipValidationTests {

        @Test
        @DisplayName("Debe aceptar relación padre")
        void shouldAcceptFatherRelationship() {
            // Given
            EmergencyContact fatherContact = new EmergencyContact("José", "Pérez", "PADRE", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(fatherContact));
        }

        @Test
        @DisplayName("Debe aceptar relación madre")
        void shouldAcceptMotherRelationship() {
            // Given
            EmergencyContact motherContact = new EmergencyContact("Ana", "García", "MADRE", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(motherContact));
        }

        @Test
        @DisplayName("Debe aceptar relación hermano")
        void shouldAcceptSiblingRelationship() {
            // Given
            EmergencyContact siblingContact = new EmergencyContact("Carlos", "Pérez", "hermano", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(siblingContact));
        }

        @Test
        @DisplayName("Debe aceptar relación hijo")
        void shouldAcceptChildRelationship() {
            // Given
            EmergencyContact childContact = new EmergencyContact("Miguel", "Pérez", "HIJO", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(childContact));
        }

        @Test
        @DisplayName("Debe aceptar relación esposo")
        void shouldAcceptSpouseRelationship() {
            // Given
            EmergencyContact spouseContact = new EmergencyContact("Roberto", "Mendoza", "ESPOSO", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(spouseContact));
        }

        @Test
        @DisplayName("Debe aceptar relación familiar")
        void shouldAcceptFamilyRelationship() {
            // Given
            EmergencyContact familyContact = new EmergencyContact("Tía", "Juana", "FAMILIAR", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(familyContact));
        }

        @Test
        @DisplayName("Debe aceptar relación amigo")
        void shouldAcceptFriendRelationship() {
            // Given
            EmergencyContact friendContact = new EmergencyContact("Jorge", "López", "AMIGO", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(friendContact));
        }

        @Test
        @DisplayName("Debe aceptar relación otro")
        void shouldAcceptOtherRelationship() {
            // Given
            EmergencyContact otherContact = new EmergencyContact("Vecino", "Martínez", "OTRO", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(otherContact));
        }

        @Test
        @DisplayName("Debe rechazar relación inválida")
        void shouldRejectInvalidRelationship() {
            // Given
            EmergencyContact invalidContact = new EmergencyContact("María", "Pérez", "PRIMO", "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(invalidContact)
            );

            assertEquals("Emergency contact relationship must be: padre, madre, hermano, hermana, hijo, hija, esposo, esposa, pareja, familiar, amigo, or otro", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de teléfono")
    class PhoneValidationTests {

        @Test
        @DisplayName("Debe rechazar teléfono con menos de 10 dígitos")
        void shouldRejectPhoneTooShort() {
            // Given
            EmergencyContact contactWithShortPhone = new EmergencyContact("María", "Pérez", "Hermana", "123456789");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithShortPhone)
            );

            assertEquals("Emergency contact phone must have exactly 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar teléfono con más de 10 dígitos")
        void shouldRejectPhoneTooLong() {
            // Given
            EmergencyContact contactWithLongPhone = new EmergencyContact("María", "Pérez", "Hermana", "12345678901");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithLongPhone)
            );

            assertEquals("Emergency contact phone must have exactly 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar teléfono con letras")
        void shouldRejectPhoneWithLetters() {
            // Given
            EmergencyContact contactWithInvalidPhone = new EmergencyContact("María", "Pérez", "Hermana", "123456789A");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithInvalidPhone)
            );

            assertEquals("Emergency contact phone must have exactly 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar teléfono con todos ceros")
        void shouldRejectPhoneWithAllZeros() {
            // Given
            EmergencyContact contactWithZerosPhone = new EmergencyContact("María", "Pérez", "Hermana", "0000000000");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithZerosPhone)
            );

            assertEquals("Emergency contact phone cannot be all zeros", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar teléfono válido")
        void shouldAcceptValidPhone() {
            // Given
            EmergencyContact contactWithValidPhone = new EmergencyContact("María", "Pérez", "Hermana", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contactWithValidPhone));
        }

        @Test
        @DisplayName("Debe aceptar diferentes números de teléfono válidos")
        void shouldAcceptDifferentValidPhones() {
            // Given
            EmergencyContact contact1 = new EmergencyContact("María", "Pérez", "Hermana", "9876543210");
            EmergencyContact contact2 = new EmergencyContact("José", "García", "Padre", "5556667777");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contact1));
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contact2));
        }
    }

    @Nested
    @DisplayName("Validaciones de comparación con paciente")
    class PatientComparisonValidationTests {

        @Test
        @DisplayName("Debe rechazar teléfono igual al del paciente")
        void shouldRejectSamePhoneAsPatient() {
            // Given
            String patientPhone = "1234567890";
            String emergencyPhone = "1234567890";

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateNotSameAsPatient(emergencyPhone, patientPhone)
            );

            assertEquals("Emergency contact phone cannot be the same as patient phone", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar teléfono diferente al del paciente")
        void shouldAcceptDifferentPhoneFromPatient() {
            // Given
            String patientPhone = "1234567890";
            String emergencyPhone = "9876543210";

            // When & Then
            assertDoesNotThrow(() -> validationService.validateNotSameAsPatient(emergencyPhone, patientPhone));
        }

        @Test
        @DisplayName("Debe manejar teléfono de emergencia null")
        void shouldHandleNullEmergencyPhone() {
            // Given
            String patientPhone = "1234567890";
            String emergencyPhone = null;

            // When & Then
            assertDoesNotThrow(() -> validationService.validateNotSameAsPatient(emergencyPhone, patientPhone));
        }

        @Test
        @DisplayName("Debe manejar teléfono de paciente null")
        void shouldHandleNullPatientPhone() {
            // Given
            String patientPhone = null;
            String emergencyPhone = "1234567890";

            // When & Then
            assertDoesNotThrow(() -> validationService.validateNotSameAsPatient(emergencyPhone, patientPhone));
        }
    }

    @Nested
    @DisplayName("Validaciones de información mínima")
    class MinimumInfoValidationTests {

        @Test
        @DisplayName("Debe rechazar contacto sin nombre")
        void shouldRejectContactWithoutName() {
            // Given
            EmergencyContact contactWithoutName = new EmergencyContact("", "Pérez", "Hermana", "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateMinimumRequiredInfo(contactWithoutName)
            );

            assertEquals("Emergency contact must have at least first name and phone", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar contacto sin teléfono")
        void shouldRejectContactWithoutPhone() {
            // Given
            EmergencyContact contactWithoutPhone = new EmergencyContact("María", "Pérez", "Hermana", "");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateMinimumRequiredInfo(contactWithoutPhone)
            );

            assertEquals("Emergency contact must have at least first name and phone", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar contacto con información mínima")
        void shouldAcceptContactWithMinimumInfo() {
            // Given
            EmergencyContact contactWithMinimumInfo = new EmergencyContact("María", "Pérez", "Hermana", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateMinimumRequiredInfo(contactWithMinimumInfo));
        }

        @Test
        @DisplayName("Debe rechazar contacto nulo")
        void shouldRejectNullContact() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateMinimumRequiredInfo(null)
            );

            assertEquals("Emergency contact is required", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Casos extremos y edge cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Debe manejar nombres con caracteres especiales")
        void shouldHandleNamesWithSpecialCharacters() {
            // Given
            EmergencyContact contactWithAccents = new EmergencyContact("María José", "Pérez García", "Hermana", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contactWithAccents));
        }

        @Test
        @DisplayName("Debe manejar relación con minúsculas")
        void shouldHandleLowercaseRelationship() {
            // Given
            EmergencyContact contactWithLowercase = new EmergencyContact("Ana", "López", "hermana", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contactWithLowercase));
        }

        @Test
        @DisplayName("Debe manejar relación con mayúsculas")
        void shouldHandleUppercaseRelationship() {
            // Given
            EmergencyContact contactWithUppercase = new EmergencyContact("Ana", "López", "HERMANA", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contactWithUppercase));
        }

        @Test
        @DisplayName("Debe manejar relación con mezcla de mayúsculas y minúsculas")
        void shouldHandleMixedCaseRelationship() {
            // Given
            EmergencyContact contactWithMixedCase = new EmergencyContact("Ana", "López", "HermAna", "1234567890");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateEmergencyContact(contactWithMixedCase));
        }

        @Test
        @DisplayName("Debe rechazar relación con caracteres especiales")
        void shouldRejectRelationshipWithSpecialCharacters() {
            // Given
            EmergencyContact contactWithSpecialChars = new EmergencyContact("Ana", "López", "Hermana-Especial", "1234567890");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateEmergencyContact(contactWithSpecialChars)
            );

            assertEquals("Emergency contact relationship must be: padre, madre, hermano, hermana, hijo, hija, esposo, esposa, pareja, familiar, amigo, or otro", exception.getMessage());
        }
    }
}