package app.clinic.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Entity representing a medication order.
 * Contains order details and list of medications with their specifications.
 */
public class MedicationOrder {
    private final OrderNumber orderNumber;
    private final PatientCedula patientCedula;
    private final DoctorCedula doctorCedula;
    private final OrderCreationDate creationDate;
    private final OrderStatus status;
    private final List<MedicationItem> medications;

    private MedicationOrder(OrderNumber orderNumber, PatientCedula patientCedula, DoctorCedula doctorCedula,
                           OrderCreationDate creationDate, OrderStatus status, List<MedicationItem> medications) {
        this.orderNumber = orderNumber;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.creationDate = creationDate;
        this.status = status != null ? status : OrderStatus.CREADA;
        this.medications = List.copyOf(medications != null ? medications : List.of());
    }

    public static MedicationOrder of(OrderNumber orderNumber, PatientCedula patientCedula, DoctorCedula doctorCedula,
                                    OrderCreationDate creationDate, OrderStatus status, List<MedicationItem> medications) {
        return new MedicationOrder(orderNumber, patientCedula, doctorCedula, creationDate, status, medications);
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

    public List<MedicationItem> getMedications() {
        return medications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationOrder that = (MedicationOrder) o;
        return Objects.equals(orderNumber, that.orderNumber) &&
               Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(doctorCedula, that.doctorCedula) &&
               Objects.equals(creationDate, that.creationDate) &&
               status == that.status &&
               Objects.equals(medications, that.medications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, patientCedula, doctorCedula, creationDate, status, medications);
    }

    @Override
    public String toString() {
        return String.format("MedicationOrder{orderNumber=%s, patient=%s, doctor=%s, status=%s, medications=%d}",
                           orderNumber, patientCedula, doctorCedula, status, medications.size());
    }
}