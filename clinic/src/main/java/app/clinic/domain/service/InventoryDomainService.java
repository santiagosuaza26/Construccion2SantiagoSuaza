package app.clinic.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.InventoryItem;
import app.clinic.domain.model.InventoryItemId;
import app.clinic.domain.model.InventoryItemName;
import app.clinic.domain.model.InventoryItemType;
import app.clinic.domain.port.InventoryRepository;

/**
 * Domain service for inventory operations.
 * Contains business logic for inventory management following domain-driven design principles.
 */
@Service
public class InventoryDomainService {

    private final InventoryRepository inventoryRepository;

    public InventoryDomainService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Adds a new inventory item with validation.
     */
    public InventoryItem addInventoryItem(InventoryItem inventoryItem) {
        validateInventoryItemForAddition(inventoryItem);
        return inventoryRepository.save(inventoryItem);
    }

    /**
     * Updates an existing inventory item with validation.
     */
    public InventoryItem updateInventoryItem(InventoryItem inventoryItem) {
        validateInventoryItemForUpdate(inventoryItem);
        return inventoryRepository.save(inventoryItem);
    }

    /**
     * Finds an inventory item by ID.
     */
    public Optional<InventoryItem> findInventoryItemById(InventoryItemId inventoryItemId) {
        return inventoryRepository.findById(inventoryItemId);
    }

    /**
     * Finds an inventory item by name.
     */
    public Optional<InventoryItem> findInventoryItemByName(InventoryItemName name) {
        return inventoryRepository.findByName(name);
    }

    /**
     * Finds all inventory items.
     */
    public List<InventoryItem> findAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    /**
     * Finds inventory items by type.
     */
    public List<InventoryItem> findInventoryItemsByType(InventoryItemType type) {
        return inventoryRepository.findByType(type);
    }

    /**
     * Finds all active inventory items.
     */
    public List<InventoryItem> findAllActiveInventoryItems() {
        return inventoryRepository.findAllActive();
    }

    /**
     * Finds active inventory items by type.
     */
    public List<InventoryItem> findActiveInventoryItemsByType(InventoryItemType type) {
        return inventoryRepository.findActiveByType(type);
    }

    /**
     * Deactivates an inventory item.
     */
    public InventoryItem deactivateInventoryItem(InventoryItem inventoryItem) {
        InventoryItem deactivatedItem = InventoryItem.of(
            inventoryItem.getId(),
            inventoryItem.getName(),
            inventoryItem.getType(),
            inventoryItem.getCost(),
            false
        );
        return inventoryRepository.save(deactivatedItem);
    }

    /**
     * Activates an inventory item.
     */
    public InventoryItem activateInventoryItem(InventoryItem inventoryItem) {
        InventoryItem activatedItem = InventoryItem.of(
            inventoryItem.getId(),
            inventoryItem.getName(),
            inventoryItem.getType(),
            inventoryItem.getCost(),
            true
        );
        return inventoryRepository.save(activatedItem);
    }

    /**
     * Deletes an inventory item by ID.
     */
    public void deleteInventoryItemById(InventoryItemId inventoryItemId) {
        validateInventoryItemCanBeDeleted(inventoryItemId);
        inventoryRepository.deleteById(inventoryItemId);
    }

    /**
     * Validates inventory item data for addition.
     */
    private void validateInventoryItemForAddition(InventoryItem inventoryItem) {
        if (inventoryRepository.existsByName(inventoryItem.getName())) {
            throw new IllegalArgumentException("Inventory item with this name already exists");
        }
        validateInventoryItemCost(inventoryItem);
    }

    /**
     * Validates inventory item data for update.
     */
    private void validateInventoryItemForUpdate(InventoryItem inventoryItem) {
        Optional<InventoryItem> existingItem = inventoryRepository.findById(inventoryItem.getId());
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException("Inventory item to update does not exist");
        }
        validateInventoryItemCost(inventoryItem);
    }

    /**
     * Validates that the inventory item can be deleted.
     */
    private void validateInventoryItemCanBeDeleted(InventoryItemId inventoryItemId) {
        Optional<InventoryItem> inventoryItem = inventoryRepository.findById(inventoryItemId);
        if (inventoryItem.isEmpty()) {
            throw new IllegalArgumentException("Inventory item to delete does not exist");
        }
        // Add business rules for inventory item deletion if needed
    }

    /**
     * Validates inventory item cost.
     */
    private void validateInventoryItemCost(InventoryItem inventoryItem) {
        if (inventoryItem.getCost().getValue().getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Inventory item cost must be positive");
        }
    }
}