package app.clinic.domain.model.entities;

import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Password;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.model.valueobject.Username;

public class User {
    private final Credentials credentials;
    private final String fullName;
    private final Id identificationNumber;
    private final Email email;
    private final Phone phone;
    private final DateOfBirth dateOfBirth;
    private final Address address;
    private final Role role;

    public User(Credentials credentials, String fullName, Id identificationNumber, Email email, Phone phone, DateOfBirth dateOfBirth, Address address, Role role) {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (identificationNumber == null) {
            throw new IllegalArgumentException("Identification number cannot be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (phone == null) {
            throw new IllegalArgumentException("Phone cannot be null");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.credentials = credentials;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.role = role;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public Username getUsername() {
        return credentials.getUsername();
    }

    public Password getPassword() {
        return credentials.getPassword();
    }

    public String getFullName() {
        return fullName;
    }

    public Id getIdentificationNumber() {
        return identificationNumber;
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return identificationNumber.equals(user.identificationNumber);
    }

    @Override
    public int hashCode() {
        return identificationNumber.hashCode();
    }

    @Override
    public String toString() {
        return fullName + " (" + identificationNumber + ")";
    }
}