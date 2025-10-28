package app.clinic.domain.model.valueobject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateOfBirth {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final LocalDate value;

    public DateOfBirth(String dateString) {
        if (dateString == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        try {
            LocalDate date = LocalDate.parse(dateString, FORMATTER);
            LocalDate now = LocalDate.now();
            if (date.isAfter(now) || date.isBefore(now.minusYears(150))) {
                throw new IllegalArgumentException("Date of birth must be within the last 150 years and not in the future");
            }
            this.value = date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected dd/MM/yyyy");
        }
    }

    public LocalDate getValue() {
        return value;
    }

    public int getAge() {
        return LocalDate.now().getYear() - value.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateOfBirth that = (DateOfBirth) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.format(FORMATTER);
    }
}