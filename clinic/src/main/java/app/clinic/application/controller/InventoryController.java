package app.clinic.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.inventory.CreateInventoryItemDTO;
import app.clinic.application.dto.inventory.InventoryItemDTO;
import app.clinic.application.dto.inventory.UpdateInventoryItemDTO;
import app.clinic.application.service.InventoryApplicationService;
import jakarta.validation.Valid;

/**
 * REST Controller for inventory management operations.
 * Provides HTTP endpoints for managing medications, procedures, and diagnostic aids.
 * Only accessible by Information Support staff.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryApplicationService inventoryApplicationService;

    public InventoryController(InventoryApplicationService inventoryApplicationService) {
        this.inventoryApplicationService = inventoryApplicationService;
    }

    /**
     * Creates a new inventory item (medication, procedure, or diagnostic aid).
     * Only accessible by Information Support staff.
     */
    @PostMapping
    public ResponseEntity<InventoryItemDTO> createInventoryItem(@Valid @RequestBody CreateInventoryItemDTO createInventoryItemDTO) {
        InventoryItemDTO createdItem = inventoryApplicationService.createInventoryItem(createInventoryItemDTO);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Updates an existing inventory item.
     * Only accessible by Information Support staff.
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<InventoryItemDTO> updateInventoryItem(@PathVariable String itemId,
                                                               @Valid @RequestBody UpdateInventoryItemDTO updateInventoryItemDTO) {
        InventoryItemDTO updatedItem = inventoryApplicationService.updateInventoryItem(itemId, updateInventoryItemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Finds an inventory item by ID.
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<InventoryItemDTO> findInventoryItemById(@PathVariable String itemId) {
        Optional<InventoryItemDTO> item = inventoryApplicationService.findInventoryItemById(itemId);
        return item.map(i -> ResponseEntity.ok(i))
                  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds all inventory items.
     */
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> findAllInventoryItems() {
        List<InventoryItemDTO> items = inventoryApplicationService.findAllInventoryItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Finds all medications.
     */
    @GetMapping("/type/medications")
    public ResponseEntity<List<InventoryItemDTO>> findAllMedications() {
        List<InventoryItemDTO> medications = inventoryApplicationService.findAllMedications();
        return ResponseEntity.ok(medications);
    }

    /**
     * Finds all procedures.
     */
    @GetMapping("/type/procedures")
    public ResponseEntity<List<InventoryItemDTO>> findAllProcedures() {
        List<InventoryItemDTO> procedures = inventoryApplicationService.findAllProcedures();
        return ResponseEntity.ok(procedures);
    }

    /**
     * Finds all diagnostic aids.
     */
    @GetMapping("/type/diagnostic-aids")
    public ResponseEntity<List<InventoryItemDTO>> findAllDiagnosticAids() {
        List<InventoryItemDTO> diagnosticAids = inventoryApplicationService.findAllDiagnosticAids();
        return ResponseEntity.ok(diagnosticAids);
    }

    /**
     * Finds inventory items by name.
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<List<InventoryItemDTO>> findInventoryItemsByName(@PathVariable String name) {
        List<InventoryItemDTO> items = inventoryApplicationService.findInventoryItemsByName(name);
        return ResponseEntity.ok(items);
    }

    /**
     * Deletes an inventory item by ID.
     * Only accessible by Information Support staff.
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable String itemId) {
        inventoryApplicationService.deleteInventoryItem(itemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if an inventory item exists by ID.
     */
    @GetMapping("/{itemId}/exists")
    public ResponseEntity<Boolean> inventoryItemExists(@PathVariable String itemId) {
        boolean exists = inventoryApplicationService.inventoryItemExists(itemId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Gets inventory statistics.
     */
    @GetMapping("/statistics")
    public ResponseEntity<InventoryApplicationService.InventoryStatisticsDTO> getInventoryStatistics() {
        InventoryApplicationService.InventoryStatisticsDTO statistics = inventoryApplicationService.getInventoryStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Updates the cost of an inventory item.
     * Only accessible by Information Support staff.
     */
    @PutMapping("/{itemId}/cost")
    public ResponseEntity<InventoryItemDTO> updateInventoryItemCost(@PathVariable String itemId,
                                                                   @Valid @RequestBody String newCost) {
        InventoryItemDTO updatedItem = inventoryApplicationService.updateInventoryItemCost(itemId, newCost);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Checks if an inventory item is available for orders.
     */
    @GetMapping("/{itemId}/available")
    public ResponseEntity<Boolean> isInventoryItemAvailable(@PathVariable String itemId) {
        boolean available = inventoryApplicationService.isInventoryItemAvailable(itemId);
        return ResponseEntity.ok(available);
    }
}