package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an emergency contact for a patient.
 * Contains contact name, relationship, and phone number.
 */
public class EmergencyContact {
    private final EmergencyContactName name;
    private final Relationship relationship;
    private final EmergencyContactPhoneNumber phoneNumber;

    private EmergencyContact(EmergencyContactName name, Relationship relationship,
                            EmergencyContactPhoneNumber phoneNumber) {
        this.name = name;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
    }

    public static EmergencyContact of(EmergencyContactName name, Relationship relationship,
                                     EmergencyContactPhoneNumber phoneNumber) {
        return new EmergencyContact(name, relationship, phoneNumber);
    }

    public EmergencyContactName getName() {
        return name;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public EmergencyContactPhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmergencyContact that = (EmergencyContact) o;
        return Objects.equals(name, that.name) &&
               relationship == that.relationship &&
               Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, relationship, phoneNumber);
    }

    @Override
    public String toString() {
        return String.format("EmergencyContact{name=%s, relationship=%s, phone=%s}",
                           name, relationship, phoneNumber);
    }
}