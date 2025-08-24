package app.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.domain.model.Order;
import app.domain.repository.OrderRepository;

@Repository
public class JpaOrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderRepository jpa;

    public JpaOrderRepositoryAdapter(SpringDataOrderRepository jpa) {
        this.jpa = jpa;
    }

    @Override public void save(Order order) { jpa.save(order); }
    @Override public Optional<Order> findById(String orderNumber) { return jpa.findById(orderNumber); }
    @Override public List<Order> findAll() { return jpa.findAll(); }
    @Override public void deleteById(String orderNumber) { jpa.deleteById(orderNumber); }
}
