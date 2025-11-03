package app.clinic.domain.model.entities;

import app.clinic.domain.model.InvalidOrderStateException;

public class DiagnosticAidOrderStrategy implements OrderStrategy {

    @Override
    public void validateOrderComposition(Order order) {
        if (order.hasMedicationsOrProcedures()) {
            throw new InvalidOrderStateException("No se pueden agregar ayudas diagnósticas cuando hay medicamentos o procedimientos en la orden.");
        }
    }

    @Override
    public boolean canAddMedication(Order order, MedicationOrder medication) {
        throw new InvalidOrderStateException("No se pueden agregar medicamentos a una orden de ayuda diagnóstica.");
    }

    @Override
    public boolean canAddProcedure(Order order, ProcedureOrder procedure) {
        throw new InvalidOrderStateException("No se pueden agregar procedimientos a una orden de ayuda diagnóstica.");
    }

    @Override
    public boolean canAddDiagnosticAid(Order order, DiagnosticAidOrder aid) {
        validateOrderComposition(order);
        return true;
    }

    @Override
    public String getOrderType() {
        return "DIAGNOSTIC_AID";
    }
}