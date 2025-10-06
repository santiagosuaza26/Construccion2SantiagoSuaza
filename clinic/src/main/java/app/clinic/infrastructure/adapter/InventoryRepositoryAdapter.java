package app.clinic.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.Cost;
import app.clinic.domain.model.InventoryItem;
import app.clinic.domain.model.InventoryItemId;
import app.clinic.domain.model.InventoryItemName;
import app.clinic.domain.model.InventoryItemType;
import app.clinic.domain.port.InventoryRepository;
import app.clinic.infrastructure.entity.InventoryItemEntity;
import app.clinic.infrastructure.repository.InventoryJpaRepository;

/**
 * Adapter that implements the InventoryRepository port using JPA.
 * Converts between domain objects and JPA entities for inventory management.
 */
@Repository
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;

    public InventoryRepositoryAdapter(InventoryJpaRepository inventoryJpaRepository) {
        this.inventoryJpaRepository = inventoryJpaRepository;
    }

    @Override
    public InventoryItem save(InventoryItem inventoryItem) {
        InventoryItemEntity entity = toEntity(inventoryItem);
        InventoryItemEntity savedEntity = inventoryJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<InventoryItem> findById(InventoryItemId inventoryItemId) {
        return inventoryJpaRepository.findById(Long.valueOf(inventoryItemId.getValue()))
                .map(this::toDomain);
    }

    @Override
    public Optional<InventoryItem> findByName(InventoryItemName name) {
        return inventoryJpaRepository.findByName(name.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<InventoryItem> findAll() {
        return inventoryJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryItem> findByType(InventoryItemType type) {
        return inventoryJpaRepository.findByType(toEntityType(type))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryItem> findAllActive() {
        return inventoryJpaRepository.findByActiveTrue()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryItem> findActiveByType(InventoryItemType type) {
        return inventoryJpaRepository.findByTypeAndActiveTrue(toEntityType(type))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(InventoryItemId inventoryItemId) {
        return inventoryJpaRepository.existsById(Long.valueOf(inventoryItemId.getValue()));
    }

    @Override
    public boolean existsByName(InventoryItemName name) {
        return inventoryJpaRepository.existsByName(name.getValue());
    }

    @Override
    public void deleteById(InventoryItemId inventoryItemId) {
        inventoryJpaRepository.deleteById(Long.valueOf(inventoryItemId.getValue()));
    }

    @Override
    public long count() {
        return inventoryJpaRepository.count();
    }

    @Override
    public long countByType(InventoryItemType type) {
        return inventoryJpaRepository.countByType(toEntityType(type));
    }

    @Override
    public long countActive() {
        return inventoryJpaRepository.countByActiveTrue();
    }

    // Métodos de conversión entre dominio y entidad

    private InventoryItemEntity toEntity(InventoryItem inventoryItem) {
        return new InventoryItemEntity(
                inventoryItem.getName().getValue(),
                toEntityType(inventoryItem.getType()),
                inventoryItem.getCost().getValue().getAmount(),
                inventoryItem.isActive(),
                null // description field can be added later if needed
        );
    }

    private InventoryItem toDomain(InventoryItemEntity entity) {
        return InventoryItem.of(
                InventoryItemId.of(String.valueOf(entity.getId())),
                InventoryItemName.of(entity.getName()),
                toDomainType(entity.getType()),
                Cost.of(entity.getCost().doubleValue()),
                entity.isActive()
        );
    }

    private InventoryItemEntity.InventoryItemType toEntityType(InventoryItemType type) {
        return switch (type) {
            case MEDICAMENTO -> InventoryItemEntity.InventoryItemType.MEDICAMENTO;
            case PROCEDIMIENTO -> InventoryItemEntity.InventoryItemType.PROCEDIMIENTO;
            case AYUDA_DIAGNOSTICA -> InventoryItemEntity.InventoryItemType.AYUDA_DIAGNOSTICA;
        };
    }

    private InventoryItemType toDomainType(InventoryItemEntity.InventoryItemType type) {
        return switch (type) {
            case MEDICAMENTO -> InventoryItemType.MEDICAMENTO;
            case PROCEDIMIENTO -> InventoryItemType.PROCEDIMIENTO;
            case AYUDA_DIAGNOSTICA -> InventoryItemType.AYUDA_DIAGNOSTICA;
        };
    }
}