package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.InventoryRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.OrderService;
import app.clinic.domain.service.RoleBasedAccessService;

@Service
public class OrderServiceImpl extends OrderService {

    public OrderServiceImpl(OrderRepository orderRepository, PatientRepository patientRepository, InventoryRepository inventoryRepository, UserRepository userRepository, RoleBasedAccessService roleBasedAccessService) {
        super(orderRepository, patientRepository, inventoryRepository, userRepository, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}