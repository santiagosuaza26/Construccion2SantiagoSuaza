package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an order number.
 * Must be unique and maximum 6 digits as per requirements.
 */
public class OrderNumber {
    private final String value;

    private OrderNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (!isValidOrderNumber(trimmedValue)) {
            throw new IllegalArgumentException("Order number must be maximum 6 digits: " + trimmedValue);
        }
        this.value = trimmedValue;
    }

    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    public static OrderNumber of(int value) {
        String formatted = String.format("%06d", value);
        return new OrderNumber(formatted);
    }

    private boolean isValidOrderNumber(String orderNumber) {
        if (orderNumber.length() > 6) {
            return false;
        }
        return orderNumber.matches("\\d+");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderNumber that = (OrderNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}