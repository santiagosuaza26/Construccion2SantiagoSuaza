package app.clinic.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a patient's email address.
 * Optional field that may or may not be provided.
 */
public class PatientEmail {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private PatientEmail(String value) {
        if (value != null && !value.trim().isEmpty()) {
            String trimmedValue = value.trim();
            if (!isValidEmail(trimmedValue)) {
                throw new IllegalArgumentException("Invalid email format: " + trimmedValue);
            }
            this.value = trimmedValue.toLowerCase();
        } else {
            this.value = null;
        }
    }

    public static PatientEmail of(String value) {
        return new PatientEmail(value);
    }

    public static PatientEmail empty() {
        return new PatientEmail(null);
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches() && email.contains("@");
    }

    public String getValue() {
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientEmail that = (PatientEmail) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}