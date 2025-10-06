package app.clinic.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.inventory.CreateInventoryItemDTO;
import app.clinic.application.dto.inventory.InventoryItemDTO;
import app.clinic.application.dto.inventory.UpdateInventoryItemDTO;
import app.clinic.domain.model.Cost;
import app.clinic.domain.model.InventoryItem;
import app.clinic.domain.model.InventoryItemId;
import app.clinic.domain.model.InventoryItemName;
import app.clinic.domain.model.InventoryItemType;
import app.clinic.domain.model.Money;
import app.clinic.domain.service.InventoryDomainService;

/**
 * Application service for inventory management operations.
 * Coordinates between REST controllers and domain services.
 * Handles inventory-related use cases and business operations.
 */
@Service
public class InventoryApplicationService {

    private final InventoryDomainService inventoryDomainService;

    public InventoryApplicationService(InventoryDomainService inventoryDomainService) {
        this.inventoryDomainService = inventoryDomainService;
    }

    /**
     * Creates a new inventory item.
     */
    public InventoryItemDTO createInventoryItem(CreateInventoryItemDTO createInventoryItemDTO) {
        // Convert DTO to domain entity
        InventoryItemId inventoryItemId = InventoryItemId.of(UUID.randomUUID().toString());
        InventoryItemName name = InventoryItemName.of(createInventoryItemDTO.getName());
        InventoryItemType type = convertStringToInventoryItemType(createInventoryItemDTO.getType());
        Money moneyValue = Money.of(new BigDecimal(createInventoryItemDTO.getCost()));
        Cost cost = Cost.of(moneyValue);

        // Create domain entity
        InventoryItem inventoryItem = InventoryItem.of(inventoryItemId, name, type, cost);

        // Save using domain service
        InventoryItem savedInventoryItem = inventoryDomainService.addInventoryItem(inventoryItem);

        // Convert back to DTO for response
        return convertInventoryItemToDTO(savedInventoryItem);
    }

    /**
     * Updates an existing inventory item.
     * Currently supports updating: name, cost, and type.
     *
     * LIMITATIONS:
     * - The 'type' field cannot be updated through this method as it's not included in UpdateInventoryItemDTO
     * - Fields like description, dosage, frequency, duration, requiresSpecialistAssistance,
     *   and specialistTypeId are not yet supported as they don't exist in the domain model
     * - The domain model only supports basic inventory item properties: id, name, type, cost, and active status
     *
     * FUTURE IMPROVEMENTS NEEDED:
     * 1. Extend InventoryItem domain model to include additional fields (description, dosage, etc.)
     * 2. Add type field to UpdateInventoryItemDTO for complete updates
     * 3. Implement activation/deactivation functionality
     * 4. Add inventory quantity tracking if needed
     * 5. Implement inventory item categories/subcategories
     */
    public InventoryItemDTO updateInventoryItem(String itemId, UpdateInventoryItemDTO updateInventoryItemDTO) {
        // Find existing inventory item
        InventoryItemId inventoryItemId = InventoryItemId.of(itemId);
        Optional<InventoryItem> existingItemOpt = inventoryDomainService.findInventoryItemById(inventoryItemId);

        if (existingItemOpt.isEmpty()) {
            throw new IllegalArgumentException("Inventory item with ID " + itemId + " not found");
        }

        InventoryItem existingItem = existingItemOpt.get();

        // Update only the fields that are provided and exist in the domain model
        InventoryItemName updatedName = existingItem.getName();
        InventoryItemType updatedType = existingItem.getType();
        Cost updatedCost = existingItem.getCost();
        String updatedDescription = existingItem.getDescription();
        Integer updatedCurrentStock = existingItem.getCurrentStock();
        Integer updatedMinimumStock = existingItem.getMinimumStock();
        Integer updatedMaximumStock = existingItem.getMaximumStock();

        // Update name if provided
        if (updateInventoryItemDTO.getName() != null && !updateInventoryItemDTO.getName().trim().isEmpty()) {
            updatedName = InventoryItemName.of(updateInventoryItemDTO.getName());
        }

        // Update type if provided
        if (updateInventoryItemDTO.getType() != null && !updateInventoryItemDTO.getType().trim().isEmpty()) {
            updatedType = convertStringToInventoryItemType(updateInventoryItemDTO.getType());
        }

        // Update cost if provided
        if (updateInventoryItemDTO.getCost() != null && !updateInventoryItemDTO.getCost().trim().isEmpty()) {
            try {
                Money moneyValue = Money.of(new BigDecimal(updateInventoryItemDTO.getCost()));
                updatedCost = Cost.of(moneyValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid cost format: " + updateInventoryItemDTO.getCost());
            }
        }

        // Update description if provided
        if (updateInventoryItemDTO.getDescription() != null) {
            updatedDescription = updateInventoryItemDTO.getDescription();
        }

        // Update stock levels if provided
        if (updateInventoryItemDTO.getCurrentStock() != null) {
            updatedCurrentStock = updateInventoryItemDTO.getCurrentStock();
        }

        if (updateInventoryItemDTO.getMinimumStock() != null) {
            updatedMinimumStock = updateInventoryItemDTO.getMinimumStock();
        }

        if (updateInventoryItemDTO.getMaximumStock() != null) {
            updatedMaximumStock = updateInventoryItemDTO.getMaximumStock();
        }

        // Create updated inventory item
        InventoryItem updatedItem = InventoryItem.of(
            existingItem.getId(),
            updatedName,
            updatedType,
            updatedCost,
            existingItem.isActive(),
            updatedDescription,
            updatedCurrentStock,
            updatedMinimumStock,
            updatedMaximumStock
        );

        // Save using domain service
        InventoryItem savedItem = inventoryDomainService.updateInventoryItem(updatedItem);

        // Convert back to DTO for response
        return convertInventoryItemToDTO(savedItem);
    }

    /**
     * Finds an inventory item by ID.
     */
    public Optional<InventoryItemDTO> findInventoryItemById(String itemId) {
        try {
            InventoryItemId inventoryItemId = InventoryItemId.of(itemId);
            Optional<InventoryItem> inventoryItem = inventoryDomainService.findInventoryItemById(inventoryItemId);
            return inventoryItem.map(this::convertInventoryItemToDTO);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Finds all inventory items.
     */
    public List<InventoryItemDTO> findAllInventoryItems() {
        List<InventoryItem> inventoryItems = inventoryDomainService.findAllInventoryItems();
        return inventoryItems.stream()
            .map(this::convertInventoryItemToDTO)
            .toList();
    }

    /**
     * Finds all medications.
     */
    public List<InventoryItemDTO> findAllMedications() {
        List<InventoryItem> medications = inventoryDomainService.findActiveInventoryItemsByType(InventoryItemType.MEDICAMENTO);
        return medications.stream()
            .map(this::convertInventoryItemToDTO)
            .toList();
    }

    /**
     * Finds all procedures.
     */
    public List<InventoryItemDTO> findAllProcedures() {
        List<InventoryItem> procedures = inventoryDomainService.findActiveInventoryItemsByType(InventoryItemType.PROCEDIMIENTO);
        return procedures.stream()
            .map(this::convertInventoryItemToDTO)
            .toList();
    }

    /**
     * Finds all diagnostic aids.
     */
    public List<InventoryItemDTO> findAllDiagnosticAids() {
        List<InventoryItem> diagnosticAids = inventoryDomainService.findActiveInventoryItemsByType(InventoryItemType.AYUDA_DIAGNOSTICA);
        return diagnosticAids.stream()
            .map(this::convertInventoryItemToDTO)
            .toList();
    }

    /**
     * Finds inventory items by name.
     */
    public List<InventoryItemDTO> findInventoryItemsByName(String name) {
        InventoryItemName itemName = InventoryItemName.of(name);
        Optional<InventoryItem> inventoryItem = inventoryDomainService.findInventoryItemByName(itemName);
        return inventoryItem.map(item -> List.of(convertInventoryItemToDTO(item)))
            .orElse(List.of());
    }

    /**
     * Deletes an inventory item by ID.
     */
    public void deleteInventoryItem(String itemId) {
        InventoryItemId inventoryItemId = InventoryItemId.of(itemId);
        inventoryDomainService.deleteInventoryItemById(inventoryItemId);
    }

    /**
     * Checks if an inventory item exists by ID.
     */
    public boolean inventoryItemExists(String itemId) {
        try {
            InventoryItemId inventoryItemId = InventoryItemId.of(itemId);
            return inventoryDomainService.findInventoryItemById(inventoryItemId).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets inventory statistics.
     */
    public InventoryStatisticsDTO getInventoryStatistics() {
        // Get all inventory items
        List<InventoryItem> allItems = inventoryDomainService.findAllInventoryItems();

        // Calculate totals by type
        int totalItems = allItems.size();
        int totalMedications = inventoryDomainService.findActiveInventoryItemsByType(InventoryItemType.MEDICAMENTO).size();
        int totalProcedures = inventoryDomainService.findActiveInventoryItemsByType(InventoryItemType.PROCEDIMIENTO).size();
        int totalDiagnosticAids = inventoryDomainService.findActiveInventoryItemsByType(InventoryItemType.AYUDA_DIAGNOSTICA).size();

        // Calculate average cost
        String averageCost = "0.00";
        if (!allItems.isEmpty()) {
            BigDecimal totalCost = allItems.stream()
                .map(item -> item.getCost().getValue().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal average = totalCost.divide(BigDecimal.valueOf(totalItems), 2, java.math.RoundingMode.HALF_UP);
            averageCost = average.toString();
        }

        // Create and return statistics DTO
        return new InventoryStatisticsDTO(
            totalItems,
            totalMedications,
            totalProcedures,
            totalDiagnosticAids,
            averageCost
        );
    }

    /**
     * Updates the cost of an inventory item.
     * Only accessible by Information Support staff.
     */
    public InventoryItemDTO updateInventoryItemCost(String itemId, String newCost) {
        // Validate input parameters
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Inventory item ID cannot be null or empty");
        }

        if (newCost == null || newCost.trim().isEmpty()) {
            throw new IllegalArgumentException("New cost cannot be null or empty");
        }

        // Find existing inventory item
        InventoryItemId inventoryItemId = InventoryItemId.of(itemId);
        Optional<InventoryItem> existingItemOpt = inventoryDomainService.findInventoryItemById(inventoryItemId);

        if (existingItemOpt.isEmpty()) {
            throw new IllegalArgumentException("Inventory item with ID " + itemId + " not found");
        }

        InventoryItem existingItem = existingItemOpt.get();

        // Validate and parse the new cost
        try {
            BigDecimal costValue = new BigDecimal(newCost.trim());
            if (costValue.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Cost cannot be negative");
            }

            Money moneyValue = Money.of(costValue);
            Cost updatedCost = Cost.of(moneyValue);

            // Create updated inventory item with new cost
            InventoryItem updatedItem = InventoryItem.of(
                existingItem.getId(),
                existingItem.getName(),
                existingItem.getType(),
                updatedCost,
                existingItem.isActive()
            );

            // Save using domain service
            InventoryItem savedItem = inventoryDomainService.updateInventoryItem(updatedItem);

            // Convert back to DTO for response
            return convertInventoryItemToDTO(savedItem);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cost format: " + newCost + ". Must be a valid number.", e);
        }
    }

    /**
     * Checks if an inventory item is available for orders.
     */
    public boolean isInventoryItemAvailable(String itemId) {
        try {
            InventoryItemId inventoryItemId = InventoryItemId.of(itemId);
            Optional<InventoryItem> inventoryItem = inventoryDomainService.findInventoryItemById(inventoryItemId);
            return inventoryItem.map(InventoryItem::isActive).orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Converts a string type to InventoryItemType enum.
     */
    private InventoryItemType convertStringToInventoryItemType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Inventory item type cannot be null or empty");
        }

        return switch (type.toUpperCase()) {
            case "MEDICAMENTO" -> InventoryItemType.MEDICAMENTO;
            case "PROCEDIMIENTO" -> InventoryItemType.PROCEDIMIENTO;
            case "AYUDA_DIAGNOSTICA" -> InventoryItemType.AYUDA_DIAGNOSTICA;
            default -> throw new IllegalArgumentException("Invalid inventory item type: " + type +
                ". Must be MEDICAMENTO, PROCEDIMIENTO, or AYUDA_DIAGNOSTICA");
        };
    }

    /**
     * Converts an InventoryItem domain entity to InventoryItemDTO.
     */
    private InventoryItemDTO convertInventoryItemToDTO(InventoryItem inventoryItem) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(inventoryItem.getId().getValue());
        dto.setName(inventoryItem.getName().getValue());
        dto.setType(inventoryItem.getType().getDisplayName());
        dto.setCost(inventoryItem.getCost().getValue().getAmount().toString());
        dto.setDescription(inventoryItem.getDescription());
        dto.setCurrentStock(inventoryItem.getCurrentStock());
        dto.setMinimumStock(inventoryItem.getMinimumStock());
        dto.setMaximumStock(inventoryItem.getMaximumStock());
        dto.setLowStock(inventoryItem.isLowStock());
        dto.setRequiresSpecialistAssistance(false); // Default value as domain entity doesn't have this field
        return dto;
    }

    /**
     * DTO for inventory statistics.
     */
    public static class InventoryStatisticsDTO {
        private int totalItems;
        private int totalMedications;
        private int totalProcedures;
        private int totalDiagnosticAids;
        private String averageCost;

        // Constructors, getters and setters
        public InventoryStatisticsDTO() {}

        public InventoryStatisticsDTO(int totalItems, int totalMedications, int totalProcedures,
                                     int totalDiagnosticAids, String averageCost) {
            this.totalItems = totalItems;
            this.totalMedications = totalMedications;
            this.totalProcedures = totalProcedures;
            this.totalDiagnosticAids = totalDiagnosticAids;
            this.averageCost = averageCost;
        }

        // Getters and setters
        public int getTotalItems() { return totalItems; }
        public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
        public int getTotalMedications() { return totalMedications; }
        public void setTotalMedications(int totalMedications) { this.totalMedications = totalMedications; }
        public int getTotalProcedures() { return totalProcedures; }
        public void setTotalProcedures(int totalProcedures) { this.totalProcedures = totalProcedures; }
        public int getTotalDiagnosticAids() { return totalDiagnosticAids; }
        public void setTotalDiagnosticAids(int totalDiagnosticAids) { this.totalDiagnosticAids = totalDiagnosticAids; }
        public String getAverageCost() { return averageCost; }
        public void setAverageCost(String averageCost) { this.averageCost = averageCost; }
    }
}