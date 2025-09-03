package app.adapter.out.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemEntityId implements Serializable {
    private String orderNumber;
    private Integer itemNumber;

    public OrderItemEntityId() {}

    public OrderItemEntityId(String orderNumber, Integer itemNumber) {
        this.orderNumber = orderNumber;
        this.itemNumber = itemNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntityId that = (OrderItemEntityId) o;
        return Objects.equals(orderNumber, that.orderNumber) && Objects.equals(itemNumber, that.itemNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, itemNumber);
    }

    // Getters and setters
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Integer getItemNumber() { return itemNumber; }
    public void setItemNumber(Integer itemNumber) { this.itemNumber = itemNumber; }
}