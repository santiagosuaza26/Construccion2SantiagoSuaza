package app.domain.model;

public class EmergencyContact {

    private String fullName;
    private String relationship;
    private String phoneNumber;

    public EmergencyContact() {
    }

    public EmergencyContact(String fullName, String relationship, String phoneNumber) {
        this.fullName = fullName;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        return "EmergencyContact{" +
                "fullName='" + fullName + '\'' +
                ", relationship='" + relationship + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
