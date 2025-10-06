package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a person's full name.
 * Separates first names and last names for better data management.
 */
public class FullName {
    private final String firstNames;
    private final String lastNames;

    private FullName(String firstNames, String lastNames) {
        if (firstNames == null || firstNames.trim().isEmpty()) {
            throw new IllegalArgumentException("First names cannot be null or empty");
        }
        if (lastNames == null || lastNames.trim().isEmpty()) {
            throw new IllegalArgumentException("Last names cannot be null or empty");
        }
        this.firstNames = firstNames.trim();
        this.lastNames = lastNames.trim();
    }

    public static FullName of(String firstNames, String lastNames) {
        return new FullName(firstNames, lastNames);
    }

    public String getFirstNames() {
        return firstNames;
    }

    public String getLastNames() {
        return lastNames;
    }

    public String getFullName() {
        return firstNames + " " + lastNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullName fullName = (FullName) o;
        return Objects.equals(firstNames, fullName.firstNames) &&
               Objects.equals(lastNames, fullName.lastNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstNames, lastNames);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}