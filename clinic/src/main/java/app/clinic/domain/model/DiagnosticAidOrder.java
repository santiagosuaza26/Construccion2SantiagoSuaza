package app.clinic.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Entity representing a diagnostic aid order.
 * Contains order details and list of diagnostic aids with their specifications.
 */
public class DiagnosticAidOrder {
    private final OrderNumber orderNumber;
    private final PatientCedula patientCedula;
    private final DoctorCedula doctorCedula;
    private final OrderCreationDate creationDate;
    private final OrderStatus status;
    private final List<DiagnosticAidItem> diagnosticAids;

    private DiagnosticAidOrder(OrderNumber orderNumber, PatientCedula patientCedula, DoctorCedula doctorCedula,
                              OrderCreationDate creationDate, OrderStatus status, List<DiagnosticAidItem> diagnosticAids) {
        this.orderNumber = orderNumber;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.creationDate = creationDate;
        this.status = status != null ? status : OrderStatus.CREADA;
        this.diagnosticAids = List.copyOf(diagnosticAids != null ? diagnosticAids : List.of());
    }

    public static DiagnosticAidOrder of(OrderNumber orderNumber, PatientCedula patientCedula, DoctorCedula doctorCedula,
                                       OrderCreationDate creationDate, OrderStatus status, List<DiagnosticAidItem> diagnosticAids) {
        return new DiagnosticAidOrder(orderNumber, patientCedula, doctorCedula, creationDate, status, diagnosticAids);
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

    public List<DiagnosticAidItem> getDiagnosticAids() {
        return diagnosticAids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticAidOrder that = (DiagnosticAidOrder) o;
        return Objects.equals(orderNumber, that.orderNumber) &&
               Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(doctorCedula, that.doctorCedula) &&
               Objects.equals(creationDate, that.creationDate) &&
               status == that.status &&
               Objects.equals(diagnosticAids, that.diagnosticAids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, patientCedula, doctorCedula, creationDate, status, diagnosticAids);
    }

    @Override
    public String toString() {
        return String.format("DiagnosticAidOrder{orderNumber=%s, patient=%s, doctor=%s, status=%s, diagnosticAids=%d}",
                           orderNumber, patientCedula, doctorCedula, status, diagnosticAids.size());
    }
}