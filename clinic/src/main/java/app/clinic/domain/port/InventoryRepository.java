package app.clinic.domain.port;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.InventoryItem;
import app.clinic.domain.model.InventoryItemId;
import app.clinic.domain.model.InventoryItemName;
import app.clinic.domain.model.InventoryItemType;

/**
 * Port interface for inventory repository operations.
 * Defines the contract for inventory data access in the domain layer.
 */
public interface InventoryRepository {

    /**
     * Saves a new inventory item or updates an existing one.
     */
    InventoryItem save(InventoryItem inventoryItem);

    /**
     * Finds an inventory item by its unique identifier.
     */
    Optional<InventoryItem> findById(InventoryItemId inventoryItemId);

    /**
     * Finds an inventory item by its name.
     */
    Optional<InventoryItem> findByName(InventoryItemName name);

    /**
     * Finds all inventory items.
     */
    List<InventoryItem> findAll();

    /**
     * Finds inventory items by type.
     */
    List<InventoryItem> findByType(InventoryItemType type);

    /**
     * Finds all active inventory items.
     */
    List<InventoryItem> findAllActive();

    /**
     * Finds active inventory items by type.
     */
    List<InventoryItem> findActiveByType(InventoryItemType type);

    /**
     * Checks if an inventory item exists with the given ID.
     */
    boolean existsById(InventoryItemId inventoryItemId);

    /**
     * Checks if an inventory item exists with the given name.
     */
    boolean existsByName(InventoryItemName name);

    /**
     * Deletes an inventory item by its identifier.
     */
    void deleteById(InventoryItemId inventoryItemId);

    /**
     * Counts total number of inventory items.
     */
    long count();

    /**
     * Counts inventory items by type.
     */
    long countByType(InventoryItemType type);

    /**
     * Counts active inventory items.
     */
    long countActive();
}