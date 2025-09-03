package app.domain.model;

public final class EmergencyContact {
    private final String firstName;
    private final String lastName;
    private final String relationship;
    private final String phone;

    public EmergencyContact(String firstName, String lastName, String relationship, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.relationship = relationship;
        this.phone = phone;
        validate();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRelationship() { return relationship; }
    public String getPhone() { return phone; }

    private void validate() {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Emergency phone must have 10 digits");
        }
    }
}
