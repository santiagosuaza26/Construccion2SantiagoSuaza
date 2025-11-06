package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.application.usecase.GenerateBillingUseCase;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.BillingService;

@Service
public class GenerateBillingUseCaseService extends GenerateBillingUseCase {

    public GenerateBillingUseCaseService(BillingService billingService, OrderRepository orderRepository,
                                         PatientRepository patientRepository, UserRepository userRepository) {
        super(billingService, orderRepository, patientRepository, userRepository);
    }

    // Infrastructure layer service that extends the domain use case
    // Can add infrastructure-specific concerns like logging, caching, etc.
}