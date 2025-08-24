package app.domain.repository;

import app.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(String orderNumber);

    List<Order> findAll();

    void deleteById(String orderNumber);
}
