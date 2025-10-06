package app.clinic.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.OrderEntity;

/**
 * JPA repository interface for Order entity operations.
 * Provides basic CRUD operations and custom queries for medical orders.
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * Finds an order by its order number.
     */
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    /**
     * Finds all orders for a specific patient.
     */
    List<OrderEntity> findByPatientCedula(String patientCedula);

    /**
     * Finds all orders for a specific doctor.
     */
    List<OrderEntity> findByDoctorCedula(String doctorCedula);

    /**
     * Checks if an order exists with the given order number.
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * Counts orders by patient.
     */
    long countByPatientCedula(String patientCedula);

    /**
     * Counts orders by doctor.
     */
    long countByDoctorCedula(String doctorCedula);

    /**
     * Finds orders within a date range.
     */
    @Query("SELECT o FROM OrderEntity o WHERE o.creationDate BETWEEN :startDate AND :endDate")
    List<OrderEntity> findByCreationDateBetween(@Param("startDate") java.time.LocalDate startDate,
                                               @Param("endDate") java.time.LocalDate endDate);
}