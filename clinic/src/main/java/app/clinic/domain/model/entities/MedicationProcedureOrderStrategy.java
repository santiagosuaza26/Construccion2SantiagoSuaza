package app.clinic.domain.model.entities;

import app.clinic.domain.model.InvalidOrderStateException;

public class MedicationProcedureOrderStrategy implements OrderStrategy {

    @Override
    public void validateOrderComposition(Order order) {
        if (order.hasDiagnosticAids()) {
            throw new InvalidOrderStateException("No se pueden agregar medicamentos o procedimientos cuando hay ayudas diagnósticas en la orden.");
        }
    }

    @Override
    public boolean canAddMedication(Order order, MedicationOrder medication) {
        validateOrderComposition(order);
        return true;
    }

    @Override
    public boolean canAddProcedure(Order order, ProcedureOrder procedure) {
        validateOrderComposition(order);
        return true;
    }

    @Override
    public boolean canAddDiagnosticAid(Order order, DiagnosticAidOrder aid) {
        throw new InvalidOrderStateException("No se pueden agregar ayudas diagnósticas a una orden que contiene medicamentos o procedimientos.");
    }

    @Override
    public String getOrderType() {
        return "MEDICATION_PROCEDURE";
    }
}