package app.clinic.domain.model.valueobject;

public class CurrentDiagnosis {
    private final String value;

    public CurrentDiagnosis(String value) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentDiagnosis that = (CurrentDiagnosis) o;
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