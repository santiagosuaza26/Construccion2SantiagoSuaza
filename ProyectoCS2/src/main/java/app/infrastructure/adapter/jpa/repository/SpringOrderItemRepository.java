package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.infrastructure.adapter.jpa.entity.OrderItemEntity;

import java.util.List;

public interface SpringOrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrderNumber(String orderNumber);
}