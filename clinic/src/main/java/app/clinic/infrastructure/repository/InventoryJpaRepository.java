package app.clinic.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.InventoryItemEntity;

/**
 * JPA repository interface for Inventory entity operations.
 * Provides basic CRUD operations and custom queries for inventory management.
 */
@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryItemEntity, Long> {

    /**
     * Finds an inventory item by its name.
     */
    Optional<InventoryItemEntity> findByName(String name);

    /**
     * Finds inventory items by type.
     */
    List<InventoryItemEntity> findByType(InventoryItemEntity.InventoryItemType type);

    /**
     * Finds all active inventory items.
     */
    List<InventoryItemEntity> findByActiveTrue();

    /**
     * Finds active inventory items by type.
     */
    List<InventoryItemEntity> findByTypeAndActiveTrue(InventoryItemEntity.InventoryItemType type);

    /**
     * Checks if an inventory item exists with the given name.
     */
    boolean existsByName(String name);

    /**
     * Counts inventory items by type.
     */
    long countByType(InventoryItemEntity.InventoryItemType type);

    /**
     * Counts active inventory items.
     */
    long countByActiveTrue();
}