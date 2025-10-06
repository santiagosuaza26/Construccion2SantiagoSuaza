package app.clinic.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Value Object representing a birth date.
 * Validates that the person is not older than 150 years as per requirements.
 */
public class BirthDate {
    private final LocalDate value;

    private BirthDate(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (!isValidBirthDate(value)) {
            throw new IllegalArgumentException("Birth date cannot be more than 150 years ago: " + value);
        }
        this.value = value;
    }

    public static BirthDate of(LocalDate value) {
        return new BirthDate(value);
    }

    public static BirthDate of(int year, int month, int day) {
        return new BirthDate(LocalDate.of(year, month, day));
    }

    private boolean isValidBirthDate(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Period age = Period.between(birthDate, now);
        return age.getYears() <= 150;
    }

    public LocalDate getValue() {
        return value;
    }

    public int getAge() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BirthDate that = (BirthDate) o;
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