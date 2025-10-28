package app.clinic.domain.model.valueobject;

public class EmergencyContact {
    private final String name;
    private final String relation;
    private final Phone phone;

    public EmergencyContact(String name, String relation, Phone phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact name cannot be null or empty");
        }
        if (relation == null || relation.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact relation cannot be null or empty");
        }
        if (phone == null) {
            throw new IllegalArgumentException("Emergency contact phone cannot be null");
        }
        // Validate phone has exactly 10 digits
        if (phone.getValue().length() != 10) {
            throw new IllegalArgumentException("Emergency contact phone must be exactly 10 digits");
        }
        this.name = name.trim();
        this.relation = relation.trim();
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    public Phone getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmergencyContact that = (EmergencyContact) o;
        return name.equals(that.name) && relation.equals(that.relation) && phone.equals(that.phone);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + relation.hashCode() + phone.hashCode();
    }

    @Override
    public String toString() {
        return name + " (" + relation + ") - " + phone;
    }
}