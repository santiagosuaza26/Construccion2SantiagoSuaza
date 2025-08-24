package app.adapter.out.persistence;

import app.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataOrderRepository extends JpaRepository<Order, String> {
    List<Order> findByPatientId(String patientId);
}
