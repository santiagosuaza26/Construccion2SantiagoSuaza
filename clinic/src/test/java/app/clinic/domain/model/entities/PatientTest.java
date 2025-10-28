package app.clinic.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.EmergencyContact;
import app.clinic.domain.model.valueobject.Gender;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Phone;

class PatientTest {

    @Test
    void shouldCreatePatientWithValidData() {
        // Given
        Id identificationNumber = new Id("123456789");
        String fullName = "John Doe";
        DateOfBirth dateOfBirth = new DateOfBirth("01/01/1990");
        Gender gender = Gender.MASCULINO;
        Address address = new Address("123 Main St");
        Phone phone = new Phone("1234567890");
        Email email = new Email("john@example.com");
        EmergencyContact emergencyContact = new EmergencyContact("Jane Doe", "Sister", new Phone("0987654321"));
        Insurance insurance = new Insurance("Company", "POL123", true, java.time.LocalDate.now().plusDays(30));

        // When
        Patient patient = new Patient(identificationNumber, fullName, dateOfBirth, gender, address, phone, email, emergencyContact, insurance);

        // Then
        assertEquals(identificationNumber, patient.getIdentificationNumber());
        assertEquals(fullName, patient.getFullName());
        assertEquals(dateOfBirth, patient.getDateOfBirth());
        assertEquals(gender, patient.getGender());
        assertEquals(address, patient.getAddress());
        assertEquals(phone, patient.getPhone());
        assertEquals(email, patient.getEmail());
        assertEquals(emergencyContact, patient.getEmergencyContact());
        assertEquals(insurance, patient.getInsurance());
        assertEquals(0.0, patient.getAnnualCopayTotal());
    }

    @Test
    void shouldAddToAnnualCopayTotal() {
        // Given
        Id identificationNumber = new Id("123456789");
        String fullName = "John Doe";
        DateOfBirth dateOfBirth = new DateOfBirth("01/01/1990");
        Gender gender = Gender.MASCULINO;
        Address address = new Address("123 Main St");
        Phone phone = new Phone("1234567890");
        Email email = new Email("john@example.com");
        EmergencyContact emergencyContact = new EmergencyContact("Jane Doe", "Sister", new Phone("0987654321"));
        Insurance insurance = new Insurance("Company", "POL123", true, java.time.LocalDate.now().plusDays(30));

        Patient patient = new Patient(identificationNumber, fullName, dateOfBirth, gender, address, phone, email, emergencyContact, insurance);

        // When
        patient.addToAnnualCopayTotal(50000.0);

        // Then
        assertEquals(50000.0, patient.getAnnualCopayTotal());
    }

    @Test
    void shouldBeEqualBasedOnIdentificationNumber() {
        // Given
        Id id = new Id("123456789");
        DateOfBirth dateOfBirth = new DateOfBirth("01/01/1990");
        Gender gender = Gender.MASCULINO;
        Address address = new Address("123 Main St");
        Phone phone = new Phone("1234567890");
        Email email = new Email("john@example.com");
        EmergencyContact emergencyContact = new EmergencyContact("Jane Doe", "Sister", new Phone("0987654321"));
        Insurance insurance = new Insurance("Company", "POL123", true, java.time.LocalDate.now().plusDays(30));

        Patient patient1 = new Patient(id, "John Doe", dateOfBirth, gender, address, phone, email, emergencyContact, insurance);
        Patient patient2 = new Patient(id, "Jane Doe", dateOfBirth, gender, address, phone, email, emergencyContact, insurance);

        // When & Then
        assertEquals(patient1, patient2);
        assertEquals(patient1.hashCode(), patient2.hashCode());
    }
}