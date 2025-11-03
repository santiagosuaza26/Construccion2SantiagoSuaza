package app.clinic.domain.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.clinic.domain.model.InvalidOrderStateException;
import app.clinic.domain.model.valueobject.OrderNumber;

public class Order {
    private final OrderNumber orderNumber;
    private final String patientIdentificationNumber;
    private final String doctorIdentificationNumber;
    private final LocalDate date;
    private final String diagnosis;
    private final List<MedicationOrder> medications;
    private final List<ProcedureOrder> procedures;
    private final List<DiagnosticAidOrder> diagnosticAids;
    private final OrderStrategy orderStrategy;

    public Order(OrderNumber orderNumber, String patientIdentificationNumber, String doctorIdentificationNumber, LocalDate date, String diagnosis) {
        this(orderNumber, patientIdentificationNumber, doctorIdentificationNumber, date, diagnosis, new MedicationProcedureOrderStrategy());
    }

    public Order(OrderNumber orderNumber, String patientIdentificationNumber, String doctorIdentificationNumber, LocalDate date) {
        this(orderNumber, patientIdentificationNumber, doctorIdentificationNumber, date, null, new MedicationProcedureOrderStrategy());
    }

    public Order(OrderNumber orderNumber, String patientIdentificationNumber, String doctorIdentificationNumber, LocalDate date, String diagnosis, OrderStrategy orderStrategy) {
        this.orderNumber = orderNumber;
        this.patientIdentificationNumber = patientIdentificationNumber;
        this.doctorIdentificationNumber = doctorIdentificationNumber;
        this.date = date;
        this.diagnosis = diagnosis;
        this.orderStrategy = orderStrategy;
        this.medications = new ArrayList<>();
        this.procedures = new ArrayList<>();
        this.diagnosticAids = new ArrayList<>();
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public String getPatientIdentificationNumber() {
        return patientIdentificationNumber;
    }

    public String getDoctorIdentificationNumber() {
        return doctorIdentificationNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public List<MedicationOrder> getMedications() {
        return medications;
    }

    public List<ProcedureOrder> getProcedures() {
        return procedures;
    }

    public List<DiagnosticAidOrder> getDiagnosticAids() {
        return diagnosticAids;
    }

    public boolean isDiagnosticAidOnly() {
        return !diagnosticAids.isEmpty() && medications.isEmpty() && procedures.isEmpty();
    }

    public boolean hasDiagnosticAids() {
        return !diagnosticAids.isEmpty();
    }

    public boolean hasMedicationsOrProcedures() {
        return !medications.isEmpty() || !procedures.isEmpty();
    }

    public void addMedication(MedicationOrder medication) {
        if (!orderStrategy.canAddMedication(this, medication)) {
            throw new InvalidOrderStateException("No se pueden agregar medicamentos según la estrategia de la orden.");
        }
        validateUniqueItem(medication.getItem());
        medications.add(medication);
    }

    public void addProcedure(ProcedureOrder procedure) {
        if (!orderStrategy.canAddProcedure(this, procedure)) {
            throw new InvalidOrderStateException("No se pueden agregar procedimientos según la estrategia de la orden.");
        }
        validateUniqueItem(procedure.getItem());
        procedures.add(procedure);
    }

    public void addDiagnosticAid(DiagnosticAidOrder aid) {
        if (!orderStrategy.canAddDiagnosticAid(this, aid)) {
            throw new InvalidOrderStateException("No se pueden agregar ayudas diagnósticas según la estrategia de la orden.");
        }
        validateUniqueItem(aid.getItem());
        diagnosticAids.add(aid);
    }

    private void validateUniqueItem(int item) {
        Set<Integer> allItems = new HashSet<>();
        for (MedicationOrder med : medications) {
            allItems.add(med.getItem());
        }
        for (ProcedureOrder proc : procedures) {
            allItems.add(proc.getItem());
        }
        for (DiagnosticAidOrder aid : diagnosticAids) {
            allItems.add(aid.getItem());
        }
        if (allItems.contains(item)) {
            throw new InvalidOrderStateException("El ítem " + item + " ya existe en la orden.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber.equals(order.orderNumber);
    }

    @Override
    public int hashCode() {
        return orderNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Order " + orderNumber + " for patient " + patientIdentificationNumber;
    }
}