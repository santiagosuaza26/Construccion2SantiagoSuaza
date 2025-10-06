package app.clinic.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Entity representing a procedure order.
 * Contains order details and list of procedures with their specifications.
 */
public class ProcedureOrder {
    private final OrderNumber orderNumber;
    private final PatientCedula patientCedula;
    private final DoctorCedula doctorCedula;
    private final OrderCreationDate creationDate;
    private final OrderStatus status;
    private final List<ProcedureItem> procedures;

    private ProcedureOrder(OrderNumber orderNumber, PatientCedula patientCedula, DoctorCedula doctorCedula,
                          OrderCreationDate creationDate, OrderStatus status, List<ProcedureItem> procedures) {
        this.orderNumber = orderNumber;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.creationDate = creationDate;
        this.status = status != null ? status : OrderStatus.CREADA;
        this.procedures = List.copyOf(procedures != null ? procedures : List.of());
    }

    public static ProcedureOrder of(OrderNumber orderNumber, PatientCedula patientCedula, DoctorCedula doctorCedula,
                                   OrderCreationDate creationDate, OrderStatus status, List<ProcedureItem> procedures) {
        return new ProcedureOrder(orderNumber, patientCedula, doctorCedula, creationDate, status, procedures);
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public PatientCedula getPatientCedula() {
        return patientCedula;
    }

    public DoctorCedula getDoctorCedula() {
        return doctorCedula;
    }

    public OrderCreationDate getCreationDate() {
        return creationDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<ProcedureItem> getProcedures() {
        return procedures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedureOrder that = (ProcedureOrder) o;
        return Objects.equals(orderNumber, that.orderNumber) &&
               Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(doctorCedula, that.doctorCedula) &&
               Objects.equals(creationDate, that.creationDate) &&
               status == that.status &&
               Objects.equals(procedures, that.procedures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, patientCedula, doctorCedula, creationDate, status, procedures);
    }

    @Override
    public String toString() {
        return String.format("ProcedureOrder{orderNumber=%s, patient=%s, doctor=%s, status=%s, procedures=%d}",
                           orderNumber, patientCedula, doctorCedula, status, procedures.size());
    }
}