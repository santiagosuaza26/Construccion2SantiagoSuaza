package app.clinic.domain.model.valueobject;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Password {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final String hashedValue;

    public Password(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (!PASSWORD_PATTERN.matcher(plainPassword).matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character");
        }
        this.hashedValue = encoder.encode(plainPassword);
    }

    // Constructor for loading from database (already hashed)
    public Password(String hashedValue, boolean isHashed) {
        if (isHashed) {
            this.hashedValue = hashedValue;
        } else {
            throw new IllegalArgumentException("Use the plain password constructor");
        }
    }

    public String getValue() {
        return hashedValue;
    }

    public boolean matches(String plainPassword) {
        return encoder.matches(plainPassword, hashedValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return hashedValue.equals(password.hashedValue);
    }

    @Override
    public int hashCode() {
        return hashedValue.hashCode();
    }

    @Override
    public String toString() {
        return "********"; // Never expose password in logs
    }
}