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
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRelationship() { return relationship; }
    public String getPhone() { return phone; }

    // Validaciones removidas del constructor - ahora son responsabilidad de servicios específicos
    // Esto permite crear objetos EmergencyContact para testing sin lanzar excepciones prematuras
}
