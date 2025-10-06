package app.clinic.domain.port;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.DiagnosticAidOrder;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.MedicationOrder;
import app.clinic.domain.model.OrderCreationDate;
import app.clinic.domain.model.OrderNumber;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.ProcedureOrder;

/**
 * Port interface for medical order repository operations.
 * Defines the contract for order data access in the domain layer.
 */
public interface OrderRepository {

    /**
     * Saves a medication order.
     */
    MedicationOrder saveMedicationOrder(MedicationOrder medicationOrder);

    /**
     * Saves a procedure order.
     */
    ProcedureOrder saveProcedureOrder(ProcedureOrder procedureOrder);

    /**
     * Saves a diagnostic aid order.
     */
    DiagnosticAidOrder saveDiagnosticAidOrder(DiagnosticAidOrder diagnosticAidOrder);

    /**
     * Finds a medication order by order number.
     */
    Optional<MedicationOrder> findMedicationOrderByNumber(OrderNumber orderNumber);

    /**
     * Finds a procedure order by order number.
     */
    Optional<ProcedureOrder> findProcedureOrderByNumber(OrderNumber orderNumber);

    /**
     * Finds a diagnostic aid order by order number.
     */
    Optional<DiagnosticAidOrder> findDiagnosticAidOrderByNumber(OrderNumber orderNumber);

    /**
     * Finds all medication orders for a patient.
     */
    List<MedicationOrder> findMedicationOrdersByPatient(PatientCedula patientCedula);

    /**
     * Finds all procedure orders for a patient.
     */
    List<ProcedureOrder> findProcedureOrdersByPatient(PatientCedula patientCedula);

    /**
     * Finds all diagnostic aid orders for a patient.
     */
    List<DiagnosticAidOrder> findDiagnosticAidOrdersByPatient(PatientCedula patientCedula);

    /**
     * Finds all orders by order number.
     */
    List<Object> findAllOrdersByNumber(OrderNumber orderNumber);

    /**
     * Checks if an order exists with the given order number.
     */
    boolean existsByOrderNumber(OrderNumber orderNumber);

    /**
     * Deletes an order by order number.
     */
    void deleteByOrderNumber(OrderNumber orderNumber);

    /**
     * Counts total number of orders.
     */
    long count();

    /**
     * Counts orders by patient.
     */
    long countByPatient(PatientCedula patientCedula);

    /**
     * Finds all medication orders for a doctor.
     */
    List<MedicationOrder> findMedicationOrdersByDoctor(DoctorCedula doctorCedula);

    /**
     * Finds all procedure orders for a doctor.
     */
    List<ProcedureOrder> findProcedureOrdersByDoctor(DoctorCedula doctorCedula);

    /**
     * Finds all diagnostic aid orders for a doctor.
     */
    List<DiagnosticAidOrder> findDiagnosticAidOrdersByDoctor(DoctorCedula doctorCedula);

    /**
     * Finds all medication orders within a date range.
     */
    List<MedicationOrder> findMedicationOrdersByDateRange(OrderCreationDate startDate, OrderCreationDate endDate);

    /**
     * Finds all procedure orders within a date range.
     */
    List<ProcedureOrder> findProcedureOrdersByDateRange(OrderCreationDate startDate, OrderCreationDate endDate);

    /**
     * Finds all diagnostic aid orders within a date range.
     */
    List<DiagnosticAidOrder> findDiagnosticAidOrdersByDateRange(OrderCreationDate startDate, OrderCreationDate endDate);

    /**
     * Updates a medication order.
     */
    MedicationOrder updateMedicationOrder(MedicationOrder medicationOrder);

    /**
     * Updates a procedure order.
     */
    ProcedureOrder updateProcedureOrder(ProcedureOrder procedureOrder);

    /**
     * Updates a diagnostic aid order.
     */
    DiagnosticAidOrder updateDiagnosticAidOrder(DiagnosticAidOrder diagnosticAidOrder);

    /**
     * Deletes a medication order by order number.
     */
    void deleteMedicationOrderByNumber(OrderNumber orderNumber);

    /**
     * Deletes a procedure order by order number.
     */
    void deleteProcedureOrderByNumber(OrderNumber orderNumber);

    /**
     * Deletes a diagnostic aid order by order number.
     */
    void deleteDiagnosticAidOrderByNumber(OrderNumber orderNumber);
}