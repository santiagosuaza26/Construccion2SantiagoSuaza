package app.clinic.domain.model.valueobject;

import java.util.regex.Pattern;

public class Id {
    private static final Pattern CEDULA_PATTERN = Pattern.compile("^\\d{1,10}$");
    private final String value;

    public Id(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!CEDULA_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("ID must contain only digits and be maximum 10 characters");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id id = (Id) o;
        return value.equals(id.value);
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