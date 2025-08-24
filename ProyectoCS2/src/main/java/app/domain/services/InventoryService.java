package app.domain.services;

import java.util.Optional;

import app.domain.model.InventoryItem;

public class InventoryService {

    public Optional<InventoryItem> findItemById(String id, Iterable<InventoryItem> items) {
        for (InventoryItem item : items) {
            if (item.getId().equals(id)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
