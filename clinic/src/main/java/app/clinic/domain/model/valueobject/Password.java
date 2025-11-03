package app.clinic.domain.model.valueobject;

import java.util.regex.Pattern;

public class Password {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$|^password$");
    private final String value;

    public Password(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (!PASSWORD_PATTERN.matcher(plainPassword).matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character, or be 'password' for development");
        }
        this.value = plainPassword; // Store plain text for domain independence
    }

    // Constructor for loading from database (already hashed)
    public Password(String value, boolean isHashed) {
        if (isHashed) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Use the plain password constructor");
        }
    }

    public String getValue() {
        return value;
    }

    public boolean matches(String plainPassword) {
        return value.equals(plainPassword); // Simple comparison for domain independence
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "********"; // Never expose password in logs
    }
}