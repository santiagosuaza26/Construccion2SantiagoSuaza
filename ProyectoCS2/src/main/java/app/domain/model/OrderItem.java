package app.domain.model;

public abstract class OrderItem {
    private final String orderNumber;
    private final int itemNumber;
    private final OrderItemType type;

    protected OrderItem(String orderNumber, int itemNumber, OrderItemType type) {
        this.orderNumber = orderNumber;
        this.itemNumber = itemNumber;
        this.type = type;
        validate();
    }

    public String getOrderNumber() { return orderNumber; }
    public int getItemNumber() { return itemNumber; }
    public OrderItemType getType() { return type; }

    private void validate() {
        if (orderNumber == null || !orderNumber.matches("\\d{1,6}")) throw new IllegalArgumentException("Invalid order number");
        if (itemNumber < 1) throw new IllegalArgumentException("Item number must start at 1");
        if (type == null) throw new IllegalArgumentException("Item type required");
    }
}