package app.clinic.domain.model;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String orderNumber) {
        super("Order not found with number: " + orderNumber);
    }
}