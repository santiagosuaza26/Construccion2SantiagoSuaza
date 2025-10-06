package app.clinic.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object representing the creation date and time of an order.
 */
public class OrderCreationDate {
    private final LocalDateTime value;

    private OrderCreationDate(LocalDateTime value) {
        this.value = value != null ? value : LocalDateTime.now();
    }

    public static OrderCreationDate of(LocalDateTime value) {
        return new OrderCreationDate(value);
    }

    public static OrderCreationDate now() {
        return new OrderCreationDate(LocalDateTime.now());
    }

    public LocalDateTime getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderCreationDate that = (OrderCreationDate) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}