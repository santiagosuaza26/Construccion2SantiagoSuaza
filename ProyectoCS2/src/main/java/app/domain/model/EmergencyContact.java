package app.domain.model;

public class EmergencyContact {
    private String name;
    private String relationship;
    private String phoneNumber;

    public EmergencyContact(String name, String relationship, String phoneNumber) {
        this.name = name;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "EmergencyContact{" +
                "name='" + name + '\'' +
                ", relationship='" + relationship + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
