package app.clinic.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing the date of a patient medical record.
 * Used as a key in the unstructured medical record dictionary.
 */
public class PatientRecordDate {
    private final LocalDate value;

    private PatientRecordDate(LocalDate value) {
        this.value = value != null ? value : LocalDate.now();
    }

    public static PatientRecordDate of(LocalDate value) {
        return new PatientRecordDate(value);
    }

    public static PatientRecordDate today() {
        return new PatientRecordDate(LocalDate.now());
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordDate that = (PatientRecordDate) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}