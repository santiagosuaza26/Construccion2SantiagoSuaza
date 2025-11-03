package app.clinic.domain.model.entities;

public interface OrderStrategy {
    void validateOrderComposition(Order order);
    boolean canAddMedication(Order order, MedicationOrder medication);
    boolean canAddProcedure(Order order, ProcedureOrder procedure);
    boolean canAddDiagnosticAid(Order order, DiagnosticAidOrder aid);
    String getOrderType();
}