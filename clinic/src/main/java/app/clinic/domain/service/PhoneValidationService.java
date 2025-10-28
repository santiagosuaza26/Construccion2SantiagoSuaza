package app.clinic.domain.service;

import java.util.regex.Pattern;

public class PhoneValidationService {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{1,10}$");

    public void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Phone must be 1 to 10 digits only");
        }
    }
}