package app.application.usecase;

import app.domain.model.InventoryItem;
import app.domain.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class SupportService {

    private final InventoryRepository inventoryRepository;

    public SupportService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void updateInventoryItem(InventoryItem item) {
        inventoryRepository.save(item);
    }
}