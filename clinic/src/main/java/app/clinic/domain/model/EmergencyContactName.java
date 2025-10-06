package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the name of an emergency contact.
 * Separates first names and last names as per requirements.
 */
public class EmergencyContactName {
    private final String firstNames;
    private final String lastNames;

    private EmergencyContactName(String firstNames, String lastNames) {
        if (firstNames == null || firstNames.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact first names cannot be null or empty");
        }
        if (lastNames == null || lastNames.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact last names cannot be null or empty");
        }
        this.firstNames = firstNames.trim();
        this.lastNames = lastNames.trim();
    }

    public static EmergencyContactName of(String firstNames, String lastNames) {
        return new EmergencyContactName(firstNames, lastNames);
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
        EmergencyContactName that = (EmergencyContactName) o;
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