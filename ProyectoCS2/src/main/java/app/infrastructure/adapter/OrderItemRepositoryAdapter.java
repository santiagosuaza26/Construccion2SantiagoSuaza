package app.infrastructure.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderItem;
import app.domain.model.ProcedureOrderItem;
import app.domain.port.OrderItemRepository;
import app.infrastructure.adapter.jpa.entity.OrderItemEntity;
import app.infrastructure.adapter.jpa.repository.SpringOrderItemRepository;

@Component
public class OrderItemRepositoryAdapter implements OrderItemRepository {

    private final SpringOrderItemRepository springOrderItemRepository;

    public OrderItemRepositoryAdapter(SpringOrderItemRepository springOrderItemRepository) {
        this.springOrderItemRepository = springOrderItemRepository;
    }

    @Override
    public void saveMedicationItem(MedicationOrderItem item) {
        // TODO: Implementar conversión de MedicationOrderItem a OrderItemEntity
        // Por ahora, crear una implementación básica
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderNumber(item.getOrderNumber());
        entity.setType("MEDICATION");
        entity.setName(item.getMedicationName());
        entity.setDose(item.getDosage());
        entity.setQuantity(1);
        entity.setCost(item.getCost());
        springOrderItemRepository.save(entity);
    }

    @Override
    public void saveProcedureItem(ProcedureOrderItem item) {
        // TODO: Implementar conversión de ProcedureOrderItem a OrderItemEntity
        // Por ahora, crear una implementación básica
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderNumber(item.getOrderNumber());
        entity.setType("PROCEDURE");
        entity.setName(item.getProcedureName());
        entity.setQuantity(item.getQuantity());
        entity.setFrequency(item.getFrequency());
        entity.setCost(item.getCost());
        springOrderItemRepository.save(entity);
    }

    @Override
    public void saveDiagnosticItem(DiagnosticOrderItem item) {
        // TODO: Implementar conversión de DiagnosticOrderItem a OrderItemEntity
        // Por ahora, crear una implementación básica
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderNumber(item.getOrderNumber());
        entity.setType("DIAGNOSTIC");
        entity.setName(item.getDiagnosticName());
        entity.setQuantity(item.getQuantity());
        entity.setCost(item.getCost());
        springOrderItemRepository.save(entity);
    }

    @Override
    public List<OrderItem> findByOrderNumber(String orderNumber) {
        List<OrderItemEntity> entities = springOrderItemRepository.findByOrderNumber(orderNumber);
        return entities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private OrderItem toDomain(OrderItemEntity entity) {
        // TODO: Implementar conversión completa basada en el tipo de item
        // Por ahora, retornar null para evitar errores de compilación
        return null;
    }
}