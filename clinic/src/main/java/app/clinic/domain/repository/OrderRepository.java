package app.clinic.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.valueobject.OrderNumber;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findByOrderNumber(OrderNumber orderNumber);
    List<Order> findByPatientIdentificationNumber(String patientId);
    List<Order> findByDoctorIdentificationNumber(String doctorId);
    boolean existsByOrderNumber(OrderNumber orderNumber);
}