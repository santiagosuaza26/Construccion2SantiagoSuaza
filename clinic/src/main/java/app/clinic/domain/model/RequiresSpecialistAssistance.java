package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing whether a procedure or diagnostic aid
 * requires specialist assistance.
 */
public class RequiresSpecialistAssistance {
    private final boolean value;

    private RequiresSpecialistAssistance(boolean value) {
        this.value = value;
    }

    public static RequiresSpecialistAssistance of(boolean value) {
        return new RequiresSpecialistAssistance(value);
    }

    public static RequiresSpecialistAssistance no() {
        return new RequiresSpecialistAssistance(false);
    }

    public static RequiresSpecialistAssistance yes() {
        return new RequiresSpecialistAssistance(true);
    }

    public boolean isRequired() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequiresSpecialistAssistance that = (RequiresSpecialistAssistance) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}