package app.clinic.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Value Object representing a patient's birth date.
 * Validates that the patient is not older than 150 years as per requirements.
 */
public class PatientBirthDate {
    private final LocalDate value;

    private PatientBirthDate(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("Patient birth date cannot be null");
        }
        if (!isValidBirthDate(value)) {
            throw new IllegalArgumentException("Patient birth date cannot be more than 150 years ago: " + value);
        }
        this.value = value;
    }

    public static PatientBirthDate of(LocalDate value) {
        return new PatientBirthDate(value);
    }

    public static PatientBirthDate of(int year, int month, int day) {
        return new PatientBirthDate(LocalDate.of(year, month, day));
    }

    private boolean isValidBirthDate(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Period age = Period.between(birthDate, now);
        return age.getYears() <= 150;
    }

    public LocalDate getValue() {
        return value;
    }

    public PatientAge getAge() {
        return PatientAge.of(Period.between(value, LocalDate.now()).getYears());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientBirthDate that = (PatientBirthDate) o;
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