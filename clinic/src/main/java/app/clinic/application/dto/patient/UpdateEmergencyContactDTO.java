package app.clinic.application.dto.patient;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating emergency contact information.
 * All fields are optional for partial updates.
 */
public class UpdateEmergencyContactDTO {

    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String name;

    @Size(max = 50, message = "Relationship must not exceed 50 characters")
    private String relationship;

    @Pattern(regexp = "^\\d{10}$", message = "Emergency contact phone number must be exactly 10 digits")
    private String phoneNumber;

    // Default constructor
    public UpdateEmergencyContactDTO() {}

    // Constructor with parameters
    public UpdateEmergencyContactDTO(String name, String relationship, String phoneNumber) {
        this.name = name;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("UpdateEmergencyContactDTO{name='%s', relationship='%s', phoneNumber='%s'}",
                           name, relationship, phoneNumber);
    }
}