package app.domain.port;

import java.util.List;

import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderItem;
import app.domain.model.ProcedureOrderItem;

public interface OrderItemRepository {
    void saveMedicationItem(MedicationOrderItem item);
    void saveProcedureItem(ProcedureOrderItem item);
    void saveDiagnosticItem(DiagnosticOrderItem item);
    List<OrderItem> findByOrderNumber(String orderNumber);
}