package app.clinic.domain.model.valueobject;

public class CurrentMedications {
    private final String value;

    public CurrentMedications(String value) {
        if (value == null) {
            this.value = "";
        } else {
            this.value = value.trim();
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public String[] getMedicationList() {
        if (isEmpty()) {
            return new String[0];
        }
        return value.split(";");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentMedications that = (CurrentMedications) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}