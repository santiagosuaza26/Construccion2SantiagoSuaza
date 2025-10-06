package app.clinic.application.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import app.clinic.application.dto.inventory.CreateInventoryItemDTO;
import app.clinic.application.dto.inventory.InventoryItemDTO;
import app.clinic.application.dto.inventory.UpdateInventoryItemDTO;
import app.clinic.domain.model.Cost;
import app.clinic.domain.model.InventoryItem;
import app.clinic.domain.model.InventoryItemId;
import app.clinic.domain.model.InventoryItemName;
import app.clinic.domain.model.InventoryItemType;

/**
 * Mapper class for converting between Inventory Item DTOs and domain entities.
 */
public class InventoryMapper {

    /**
     * Converts a CreateInventoryItemDTO to domain entity.
     */
    public static InventoryItem toDomainEntity(CreateInventoryItemDTO createInventoryItemDTO) {
        String inventoryItemId = UUID.randomUUID().toString();

        return InventoryItem.of(
            InventoryItemId.of(inventoryItemId),
            InventoryItemName.of(createInventoryItemDTO.getName()),
            InventoryItemType.valueOf(createInventoryItemDTO.getType()),
            Cost.of(new BigDecimal(createInventoryItemDTO.getCost()))
        );
    }

    /**
     * Converts a domain entity to DTO.
     */
    public static InventoryItemDTO toDTO(InventoryItem inventoryItem) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(inventoryItem.getId().getValue());
        dto.setName(inventoryItem.getName().getValue());
        dto.setType(inventoryItem.getType().getDisplayName());
        dto.setCost(inventoryItem.getCost().getValue().getAmount().toString());
        dto.setDescription(null); // Description field not available in domain entity
        dto.setDosage(null); // Dosage field not available in domain entity
        dto.setFrequency(null); // Frequency field not available in domain entity
        dto.setDuration(null); // Duration field not available in domain entity
        dto.setRequiresSpecialistAssistance(false); // Not available in domain entity
        dto.setSpecialistTypeId(null); // Not available in domain entity
        return dto;
    }

    /**
     * Converts a list of domain entities to DTOs.
     */
    public static List<InventoryItemDTO> toDTOList(List<InventoryItem> inventoryItems) {
        return inventoryItems.stream()
                .map(InventoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing inventory item with data from UpdateInventoryItemDTO.
     */
    public static InventoryItem updateFromDTO(InventoryItem existingInventoryItem, UpdateInventoryItemDTO updateDTO) {
        InventoryItemName name = updateDTO.getName() != null ?
            InventoryItemName.of(updateDTO.getName()) : existingInventoryItem.getName();

        InventoryItemType type = existingInventoryItem.getType();

        Cost cost = updateDTO.getCost() != null ?
            Cost.of(new BigDecimal(updateDTO.getCost())) : existingInventoryItem.getCost();

        return InventoryItem.of(
            existingInventoryItem.getId(),
            name,
            type,
            cost,
            existingInventoryItem.isActive()
        );
    }
}