package app.clinic.infrastructure.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.domain.model.DiagnosticAidOrder;
import app.clinic.domain.model.MedicationOrder;
import app.clinic.domain.model.OrderNumber;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.ProcedureOrder;
import app.clinic.domain.port.OrderRepository;

/**
 * Infrastructure service for order management operations.
 * Provides business logic implementation for medical order operations.
 */
@Service
@Transactional
public class OrderManagementInfrastructureService {

    private final OrderRepository orderRepository;

    public OrderManagementInfrastructureService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new medication order.
     */
    public MedicationOrder createMedicationOrder(MedicationOrder medicationOrder) {
        return orderRepository.saveMedicationOrder(medicationOrder);
    }

    /**
     * Creates a new procedure order.
     */
    public ProcedureOrder createProcedureOrder(ProcedureOrder procedureOrder) {
        return orderRepository.saveProcedureOrder(procedureOrder);
    }

    /**
     * Creates a new diagnostic aid order.
     */
    public DiagnosticAidOrder createDiagnosticAidOrder(DiagnosticAidOrder diagnosticAidOrder) {
        return orderRepository.saveDiagnosticAidOrder(diagnosticAidOrder);
    }

    /**
     * Finds a medication order by order number.
     */
    @Transactional(readOnly = true)
    public Optional<MedicationOrder> findMedicationOrderByNumber(OrderNumber orderNumber) {
        return orderRepository.findMedicationOrderByNumber(orderNumber);
    }

    /**
     * Finds a procedure order by order number.
     */
    @Transactional(readOnly = true)
    public Optional<ProcedureOrder> findProcedureOrderByNumber(OrderNumber orderNumber) {
        return orderRepository.findProcedureOrderByNumber(orderNumber);
    }

    /**
     * Finds a diagnostic aid order by order number.
     */
    @Transactional(readOnly = true)
    public Optional<DiagnosticAidOrder> findDiagnosticAidOrderByNumber(OrderNumber orderNumber) {
        return orderRepository.findDiagnosticAidOrderByNumber(orderNumber);
    }

    /**
     * Finds all medication orders for a patient.
     */
    @Transactional(readOnly = true)
    public List<MedicationOrder> findMedicationOrdersByPatient(PatientCedula patientCedula) {
        return orderRepository.findMedicationOrdersByPatient(patientCedula);
    }

    /**
     * Finds all procedure orders for a patient.
     */
    @Transactional(readOnly = true)
    public List<ProcedureOrder> findProcedureOrdersByPatient(PatientCedula patientCedula) {
        return orderRepository.findProcedureOrdersByPatient(patientCedula);
    }

    /**
     * Finds all diagnostic aid orders for a patient.
     */
    @Transactional(readOnly = true)
    public List<DiagnosticAidOrder> findDiagnosticAidOrdersByPatient(PatientCedula patientCedula) {
        return orderRepository.findDiagnosticAidOrdersByPatient(patientCedula);
    }

    /**
     * Checks if an order exists with the given order number.
     */
    @Transactional(readOnly = true)
    public boolean orderExistsByNumber(OrderNumber orderNumber) {
        return orderRepository.existsByOrderNumber(orderNumber);
    }

    /**
     * Deletes an order by order number.
     */
    public void deleteOrderByNumber(OrderNumber orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }

    /**
     * Counts total number of orders.
     */
    @Transactional(readOnly = true)
    public long countOrders() {
        return orderRepository.count();
    }

    /**
     * Counts orders by patient.
     */
    @Transactional(readOnly = true)
    public long countOrdersByPatient(PatientCedula patientCedula) {
        return orderRepository.countByPatient(patientCedula);
    }
}