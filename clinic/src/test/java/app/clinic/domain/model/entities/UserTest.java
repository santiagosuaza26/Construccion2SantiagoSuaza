package app.clinic.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Password;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.model.valueobject.Username;

class UserTest {

    @Test
    void shouldCreateUserWithValidData() {
        // Given
        Credentials credentials = new Credentials(new Username("johndoe"), new Password("Password123!"));
        String fullName = "John Doe";
        Id identificationNumber = new Id("123456789");
        Email email = new Email("john@example.com");
        Phone phone = new Phone("1234567890");
        DateOfBirth dateOfBirth = new DateOfBirth("01/01/1990");
        Address address = new Address("123 Main St");
        Role role = Role.MEDICO;
        // When
        User user = new User(credentials, fullName, identificationNumber, email, phone, dateOfBirth, address, role);

        // Then
        assertEquals(fullName, user.getFullName());
        assertEquals(identificationNumber, user.getIdentificationNumber());
        assertEquals(email, user.getEmail());
        assertEquals(phone, user.getPhone());
        assertEquals(dateOfBirth, user.getDateOfBirth());
        assertEquals(address, user.getAddress());
        assertEquals(role, user.getRole());
    }

    @Test
    void shouldThrowExceptionForNullCredentials() {
        // Given
        Credentials credentials = null;
        String fullName = "John Doe";
        Id identificationNumber = new Id("123456789");
        Email email = new Email("john@example.com");
        Phone phone = new Phone("1234567890");
        DateOfBirth dateOfBirth = new DateOfBirth("01/01/1990");
        Address address = new Address("123 Main St");
        Role role = Role.MEDICO;
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new User(credentials, fullName, identificationNumber, email, phone, dateOfBirth, address, role));
    }

    @Test
    void shouldThrowExceptionForNullFullName() {
        // Given
        Credentials credentials = new Credentials(new Username("johndoe"), new Password("Password123!"));
        String fullName = null;
        Id identificationNumber = new Id("123456789");
        Email email = new Email("john@example.com");
        Phone phone = new Phone("1234567890");
        DateOfBirth dateOfBirth = new DateOfBirth("01/01/1990");
        Address address = new Address("123 Main St");
        Role role = Role.MEDICO;
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new User(credentials, fullName, identificationNumber, email, phone, dateOfBirth, address, role));
    }

    @Test
    void shouldBeEqualBasedOnIdentificationNumber() {
        // Given
        Credentials credentials1 = new Credentials(new Username("johndoe"), new Password("Password123!"));
        Credentials credentials2 = new Credentials(new Username("janedoe"), new Password("Password456!"));
        Id id = new Id("123456789");
        Email email = new Email("john@example.com");
        Phone phone = new Phone("1234567890");
        DateOfBirth dateOfBirth = new DateOfBirth("01/01/1990");
        Address address = new Address("123 Main St");
        Role role = Role.MEDICO;
        User user1 = new User(credentials1, "John Doe", id, email, phone, dateOfBirth, address, role);
        User user2 = new User(credentials2, "Jane Doe", id, email, phone, dateOfBirth, address, role);

        // When & Then
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}