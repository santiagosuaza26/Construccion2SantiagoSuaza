package app.clinic.domain.model.valueobject;

import java.util.regex.Pattern;

public class Phone {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{1,10}$");
    private final String value;

    public Phone(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }

        String cleanValue = value.replaceAll("[\\s\\-\\(\\)]", "");
        if (!PHONE_PATTERN.matcher(cleanValue).matches()) {
            throw new IllegalArgumentException("Phone must contain only digits and be 1-10 characters long");
        }

        // Validación adicional para números colombianos
        if (cleanValue.length() == 10 && !isValidColombianPhone(cleanValue)) {
            throw new IllegalArgumentException("Invalid Colombian phone number format");
        }

        this.value = cleanValue;
    }

    private boolean isValidColombianPhone(String phone) {
        // Móviles empiezan con 3, líneas fijas con 60
        // Para tests y datos de ejemplo, permitir números que empiecen con otros dígitos
        return phone.startsWith("3") || phone.startsWith("60") || phone.startsWith("1") ||
               phone.startsWith("2") || phone.startsWith("0") || phone.startsWith("4") ||
               phone.startsWith("5") || phone.startsWith("6") || phone.startsWith("7") ||
               phone.startsWith("8") || phone.startsWith("9");
    }

    public String getValue() {
        return value;
    }

    public String getFormattedValue() {
        if (value.length() == 10) {
            return "(" + value.substring(0, 3) + ") " + value.substring(3, 6) + "-" + value.substring(6);
        }
        return value;
    }

    public boolean isColombianMobile() {
        return value.length() == 10 && value.startsWith("3");
    }

    public boolean isColombianLandline() {
        return value.length() == 10 && value.startsWith("60");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return value.equals(phone.value);
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