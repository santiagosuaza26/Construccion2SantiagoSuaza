package app.clinic.infrastructure.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.domain.model.Cost;
import app.clinic.domain.model.InventoryItem;
import app.clinic.domain.model.InventoryItemId;
import app.clinic.domain.model.InventoryItemName;
import app.clinic.domain.model.InventoryItemType;
import app.clinic.domain.port.InventoryRepository;

/**
 * Infrastructure service for inventory management operations.
 * Provides business logic implementation for inventory-related operations.
 */
@Service
@Transactional
public class InventoryManagementInfrastructureService {

    private final InventoryRepository inventoryRepository;

    public InventoryManagementInfrastructureService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Creates a new inventory item.
     */
    public InventoryItem createInventoryItem(InventoryItem inventoryItem) {
        if (inventoryRepository.existsByName(inventoryItem.getName())) {
            throw new IllegalArgumentException("Inventory item with name " + inventoryItem.getName().getValue() + " already exists");
        }
        return inventoryRepository.save(inventoryItem);
    }

    /**
     * Updates an existing inventory item.
     */
    public InventoryItem updateInventoryItem(InventoryItem inventoryItem) {
        return inventoryRepository.save(inventoryItem);
    }

    /**
     * Finds an inventory item by ID.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryItem> findInventoryItemById(InventoryItemId inventoryItemId) {
        return inventoryRepository.findById(inventoryItemId);
    }

    /**
     * Finds an inventory item by name.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryItem> findInventoryItemByName(InventoryItemName name) {
        return inventoryRepository.findByName(name);
    }

    /**
     * Finds all inventory items.
     */
    @Transactional(readOnly = true)
    public List<InventoryItem> findAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    /**
     * Finds inventory items by type.
     */
    @Transactional(readOnly = true)
    public List<InventoryItem> findInventoryItemsByType(InventoryItemType type) {
        return inventoryRepository.findByType(type);
    }

    /**
     * Finds all active inventory items.
     */
    @Transactional(readOnly = true)
    public List<InventoryItem> findAllActiveInventoryItems() {
        return inventoryRepository.findAllActive();
    }

    /**
     * Finds active inventory items by type.
     */
    @Transactional(readOnly = true)
    public List<InventoryItem> findActiveInventoryItemsByType(InventoryItemType type) {
        return inventoryRepository.findActiveByType(type);
    }

    /**
     * Deletes an inventory item by ID.
     */
    public void deleteInventoryItemById(InventoryItemId inventoryItemId) {
        inventoryRepository.deleteById(inventoryItemId);
    }

    /**
     * Checks if an inventory item exists with the given ID.
     */
    @Transactional(readOnly = true)
    public boolean inventoryItemExistsById(InventoryItemId inventoryItemId) {
        return inventoryRepository.existsById(inventoryItemId);
    }

    /**
     * Checks if an inventory item exists with the given name.
     */
    @Transactional(readOnly = true)
    public boolean inventoryItemExistsByName(InventoryItemName name) {
        return inventoryRepository.existsByName(name);
    }

    /**
     * Counts total number of inventory items.
     */
    @Transactional(readOnly = true)
    public long countInventoryItems() {
        return inventoryRepository.count();
    }

    /**
     * Counts inventory items by type.
     */
    @Transactional(readOnly = true)
    public long countInventoryItemsByType(InventoryItemType type) {
        return inventoryRepository.countByType(type);
    }

    /**
     * Counts active inventory items.
     */
    @Transactional(readOnly = true)
    public long countActiveInventoryItems() {
        return inventoryRepository.countActive();
    }

    /**
     * Creates a new medication item.
     */
    public InventoryItem createMedication(String name, Cost cost) {
        InventoryItem medication = InventoryItem.of(
                InventoryItemId.of(java.util.UUID.randomUUID().toString()),
                InventoryItemName.of(name),
                InventoryItemType.MEDICAMENTO,
                cost
        );
        return createInventoryItem(medication);
    }

    /**
     * Creates a new procedure item.
     */
    public InventoryItem createProcedure(String name, Cost cost) {
        InventoryItem procedure = InventoryItem.of(
                InventoryItemId.of(java.util.UUID.randomUUID().toString()),
                InventoryItemName.of(name),
                InventoryItemType.PROCEDIMIENTO,
                cost
        );
        return createInventoryItem(procedure);
    }

    /**
     * Creates a new diagnostic aid item.
     */
    public InventoryItem createDiagnosticAid(String name, Cost cost) {
        InventoryItem diagnosticAid = InventoryItem.of(
                InventoryItemId.of(java.util.UUID.randomUUID().toString()),
                InventoryItemName.of(name),
                InventoryItemType.AYUDA_DIAGNOSTICA,
                cost
        );
        return createInventoryItem(diagnosticAid);
    }
}