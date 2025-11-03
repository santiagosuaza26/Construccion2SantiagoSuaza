package app.clinic.domain.model.valueobject;

import java.util.regex.Pattern;

public class OrderNumber {
    private static final Pattern ORDER_NUMBER_PATTERN = Pattern.compile("^\\d{1,6}$");
    private final String value;

    public OrderNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be null or empty");
        }
        String trimmed = value.trim();
        if (!ORDER_NUMBER_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Order number must contain only digits and be 1-6 characters long");
        }
        this.value = trimmed;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderNumber that = (OrderNumber) o;
        return value.equals(that.value);
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