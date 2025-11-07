package app.clinic.domain.model.valueobject;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final List<String> VALID_DOMAINS = Arrays.asList(
        "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "icloud.com",
        "aol.com", "protonmail.com", "mail.com", "yandex.com", "zoho.com",
        "example.com","clinica.com"
    );

    private final String value;

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format - must contain @ and domain");
        }

        String trimmedValue = value.trim().toLowerCase();
        validateDomain(trimmedValue);

        this.value = trimmedValue;
    }

    private void validateDomain(String email) {
        String domain = email.substring(email.indexOf('@') + 1);
        if (!VALID_DOMAINS.contains(domain)) {
            throw new IllegalArgumentException("Email domain not supported: " + domain);
        }
    }

    public String getValue() {
        return value;
    }

    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }

    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}