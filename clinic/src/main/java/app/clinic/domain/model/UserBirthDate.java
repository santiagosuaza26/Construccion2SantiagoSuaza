package app.clinic.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Value Object representing a user's birth date.
 * Validates that the user is not older than 150 years as per requirements.
 */
public class UserBirthDate {
    private final LocalDate value;

    private UserBirthDate(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("User birth date cannot be null");
        }
        if (!isValidBirthDate(value)) {
            throw new IllegalArgumentException("User birth date cannot be more than 150 years ago: " + value);
        }
        this.value = value;
    }

    public static UserBirthDate of(LocalDate value) {
        return new UserBirthDate(value);
    }

    public static UserBirthDate of(int year, int month, int day) {
        return new UserBirthDate(LocalDate.of(year, month, day));
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
        UserBirthDate that = (UserBirthDate) o;
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