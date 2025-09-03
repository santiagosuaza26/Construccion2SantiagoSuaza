package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.infrastructure.adapter.jpa.entity.OrderHeaderEntity;

public interface SpringOrderHeaderRepository extends JpaRepository<OrderHeaderEntity, String> {
    boolean existsByOrderNumber(String orderNumber);
}