package app.infrastructure.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import app.domain.model.OrderHeader;
import app.domain.port.OrderHeaderRepository;
import app.infrastructure.adapter.jpa.entity.OrderHeaderEntity;
import app.infrastructure.adapter.jpa.repository.SpringOrderHeaderRepository;

@Component
public class OrderHeaderRepositoryAdapter implements OrderHeaderRepository {

    private final SpringOrderHeaderRepository springOrderHeaderRepository;

    public OrderHeaderRepositoryAdapter(SpringOrderHeaderRepository springOrderHeaderRepository) {
        this.springOrderHeaderRepository = springOrderHeaderRepository;
    }

    @Override
    public boolean existsOrderNumber(String orderNumber) {
        return springOrderHeaderRepository.existsByOrderNumber(orderNumber);
    }

    @Override
    public OrderHeader save(OrderHeader header) {
        // TODO: Implementar conversión de OrderHeader a OrderHeaderEntity
        // Por ahora, crear una implementación básica
        OrderHeaderEntity entity = new OrderHeaderEntity();
        entity.setOrderNumber(header.getOrderNumber());
        entity.setPatientIdCard(header.getPatientIdCard());
        // TODO: Completar conversión cuando se implemente el mapper

        OrderHeaderEntity savedEntity = springOrderHeaderRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<OrderHeader> findByNumber(String orderNumber) {
        return springOrderHeaderRepository.findById(orderNumber)
                .map(this::toDomain);
    }

    private OrderHeader toDomain(OrderHeaderEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OrderHeader(
            entity.getOrderNumber(),
            entity.getPatientIdCard(),
            entity.getDoctorIdCard(),
            entity.getCreationDate()
        );
    }
}