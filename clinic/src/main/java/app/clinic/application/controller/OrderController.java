package app.clinic.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.order.CreateOrderDTO;
import app.clinic.application.dto.order.OrderDTO;
import app.clinic.application.service.OrderApplicationService;
import jakarta.validation.Valid;

/**
 * REST Controller for medical order management operations.
 * Provides HTTP endpoints for creating, reading, updating, and deleting medical orders.
 * Only accessible by doctors.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    /**
     * Creates a new medical order.
     * Only accessible by doctors.
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
        OrderDTO createdOrder = orderApplicationService.createOrder(createOrderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Finds an order by its number.
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDTO> findOrderByNumber(@PathVariable String orderNumber) {
        Optional<OrderDTO> order = orderApplicationService.findOrderByNumber(orderNumber);
        return order.map(o -> ResponseEntity.ok(o))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds all orders for a specific patient.
     */
    @GetMapping("/patient/{patientCedula}")
    public ResponseEntity<List<OrderDTO>> findOrdersByPatientCedula(@PathVariable String patientCedula) {
        List<OrderDTO> orders = orderApplicationService.findOrdersByPatientCedula(patientCedula);
        return ResponseEntity.ok(orders);
    }

    /**
     * Finds all orders created by a specific doctor.
     */
    @GetMapping("/doctor/{doctorCedula}")
    public ResponseEntity<List<OrderDTO>> findOrdersByDoctorCedula(@PathVariable String doctorCedula) {
        List<OrderDTO> orders = orderApplicationService.findOrdersByDoctorCedula(doctorCedula);
        return ResponseEntity.ok(orders);
    }

    /**
     * Finds all orders within a date range.
     */
    @GetMapping("/daterange/{startDate}/{endDate}")
    public ResponseEntity<List<OrderDTO>> findOrdersByDateRange(@PathVariable String startDate,
                                                               @PathVariable String endDate) {
        List<OrderDTO> orders = orderApplicationService.findOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Updates an existing order.
     * Only accessible by doctors.
     */
    @PutMapping("/{orderNumber}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable String orderNumber,
                                               @Valid @RequestBody CreateOrderDTO updateDTO) {
        OrderDTO updatedOrder = orderApplicationService.updateOrder(orderNumber, updateDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Deletes an order by its number.
     * Only accessible by doctors.
     */
    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderNumber) {
        orderApplicationService.deleteOrder(orderNumber);
        return ResponseEntity.noContent().build();
    }

    /**
     * Validates business rules for order creation.
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateOrderBusinessRules(@Valid @RequestBody CreateOrderDTO orderDTO) {
        boolean isValid = orderApplicationService.validateOrderBusinessRules(orderDTO);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Generates the next available order number.
     */
    @GetMapping("/next-number")
    public ResponseEntity<String> generateNextOrderNumber() {
        String nextNumber = orderApplicationService.generateNextOrderNumber();
        return ResponseEntity.ok(nextNumber);
    }

    /**
     * Checks if an order number is available.
     */
    @GetMapping("/number/{orderNumber}/available")
    public ResponseEntity<Boolean> isOrderNumberAvailable(@PathVariable String orderNumber) {
        boolean available = orderApplicationService.isOrderNumberAvailable(orderNumber);
        return ResponseEntity.ok(available);
    }

    /**
     * Gets order statistics for a patient.
     */
    @GetMapping("/patient/{patientCedula}/statistics")
    public ResponseEntity<OrderApplicationService.OrderStatisticsDTO> getOrderStatistics(@PathVariable String patientCedula) {
        OrderApplicationService.OrderStatisticsDTO statistics = orderApplicationService.getOrderStatistics(patientCedula);
        return ResponseEntity.ok(statistics);
    }
}