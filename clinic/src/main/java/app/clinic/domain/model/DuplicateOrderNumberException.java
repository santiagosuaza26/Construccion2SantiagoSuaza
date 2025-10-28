package app.clinic.domain.model;

public class DuplicateOrderNumberException extends DomainException {
    public DuplicateOrderNumberException(String orderNumber) {
        super("Número de orden duplicado: " + orderNumber);
    }
}