package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.InventoryRepository;
import app.clinic.domain.service.InventoryService;

@Service
public class InventoryServiceImpl extends InventoryService {

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        super(inventoryRepository);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}