package app.domain.repository;

import app.domain.model.InventoryItem;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    void save(InventoryItem item);

    Optional<InventoryItem> findById(String id);

    List<InventoryItem> findAll();

    void deleteById(String id);
}
