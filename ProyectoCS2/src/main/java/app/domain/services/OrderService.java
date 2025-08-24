package app.domain.services;

import app.domain.model.Order;

public class OrderService {

    public boolean validateUniqueOrderNumber(String orderNumber, Iterable<Order> orders) {
        for (Order o : orders) {
            if (o.getOrderNumber().equals(orderNumber)) {
                return false;
            }
        }
        return true;
    }
}