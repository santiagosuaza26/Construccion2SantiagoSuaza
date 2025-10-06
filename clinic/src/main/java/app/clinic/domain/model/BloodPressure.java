package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing blood pressure reading.
 * Format: systolic/diastolic (e.g., "120/80")
 */
public class BloodPressure {
    private final String value;

    private BloodPressure(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Blood pressure cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (!isValidBloodPressure(trimmedValue)) {
            throw new IllegalArgumentException("Invalid blood pressure format. Expected format: systolic/diastolic (e.g., 120/80)");
        }
        this.value = trimmedValue;
    }

    public static BloodPressure of(String value) {
        return new BloodPressure(value);
    }

    private boolean isValidBloodPressure(String bloodPressure) {
        // Check format: digits/digits
        return bloodPressure.matches("^\\d{2,3}/\\d{2,3}$");
    }

    public String getValue() {
        return value;
    }

    public int getSystolic() {
        return Integer.parseInt(value.split("/")[0]);
    }

    public int getDiastolic() {
        return Integer.parseInt(value.split("/")[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodPressure that = (BloodPressure) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}