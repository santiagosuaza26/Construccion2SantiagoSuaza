package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a user's full name.
 * Separates first names and last names for better data management.
 */
public class UserFullName {
    private final String firstNames;
    private final String lastNames;

    private UserFullName(String firstNames, String lastNames) {
        if (firstNames == null || firstNames.trim().isEmpty()) {
            throw new IllegalArgumentException("User first names cannot be null or empty");
        }
        if (lastNames == null || lastNames.trim().isEmpty()) {
            throw new IllegalArgumentException("User last names cannot be null or empty");
        }
        this.firstNames = firstNames.trim();
        this.lastNames = lastNames.trim();
    }

    public static UserFullName of(String firstNames, String lastNames) {
        return new UserFullName(firstNames, lastNames);
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
        UserFullName that = (UserFullName) o;
        return Objects.equals(firstNames, that.firstNames) &&
               Objects.equals(lastNames, that.lastNames);
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