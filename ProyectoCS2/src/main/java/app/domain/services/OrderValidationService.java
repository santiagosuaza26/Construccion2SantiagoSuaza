package app.domain.services;

import app.domain.model.*;

public class OrderValidationService {

    public boolean validateNoMixing(Order order) {
        if (order instanceof DiagnosticOrder) {
            return order.getItems().size() == 1; // no meds/procedures allowed
        }
        return true;
    }
}