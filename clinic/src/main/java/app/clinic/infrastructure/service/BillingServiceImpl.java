package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.BillingRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.BillingService;
import app.clinic.domain.service.RoleBasedAccessService;

@Service
public class BillingServiceImpl extends BillingService {

    public BillingServiceImpl(BillingRepository billingRepository,
                              PatientRepository patientRepository,
                              OrderRepository orderRepository,
                              UserRepository userRepository,
                              RoleBasedAccessService roleBasedAccessService) {
        super(billingRepository, patientRepository, orderRepository, userRepository, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}