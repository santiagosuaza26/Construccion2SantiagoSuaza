package app.clinic.application.usecase;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.OrderService;
import app.clinic.domain.service.RoleBasedAccessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultPatientOrdersUseCase {

    private final OrderService orderService;
    private final RoleBasedAccessService roleBasedAccessService;

    public ConsultPatientOrdersUseCase(OrderService orderService, RoleBasedAccessService roleBasedAccessService) {
        this.orderService = orderService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public List<Order> execute(Role userRole, String patientId) {
        roleBasedAccessService.validatePatientDataAccess(userRole, false);
        return orderService.findOrdersByPatientId(patientId);
    }
}