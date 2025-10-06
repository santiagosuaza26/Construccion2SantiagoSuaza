package app.clinic.application.dto.patient;

/**
 * Data Transfer Object for Emergency Contact information.
 * Used for API responses containing emergency contact data.
 */
public class EmergencyContactDTO {
    private String name;
    private String relationship;
    private String phoneNumber;

    // Default constructor
    public EmergencyContactDTO() {}

    // Constructor with parameters
    public EmergencyContactDTO(String name, String relationship, String phoneNumber) {
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
        return String.format("EmergencyContactDTO{name='%s', relationship='%s', phoneNumber='%s'}",
                           name, relationship, phoneNumber);
    }
}