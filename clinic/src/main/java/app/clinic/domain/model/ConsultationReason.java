package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the reason for medical consultation.
 */
public class ConsultationReason {
    private final String value;

    private ConsultationReason(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Consultation reason cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static ConsultationReason of(String value) {
        return new ConsultationReason(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultationReason that = (ConsultationReason) o;
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