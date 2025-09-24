package app.domain.factory;

import java.util.concurrent.ThreadLocalRandom;

import app.domain.port.OrderHeaderRepository;

public class OrderFactory {
    private final OrderHeaderRepository orderHeaderRepository;
    
    public OrderFactory(OrderHeaderRepository orderHeaderRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
    }
    
    public String generateUniqueOrderNumber() {
        String orderNumber;
        do {
            orderNumber = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        } while (orderHeaderRepository.existsOrderNumber(orderNumber));
        return orderNumber;
    }
}