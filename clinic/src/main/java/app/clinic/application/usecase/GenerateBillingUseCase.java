package app.clinic.application.usecase;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.BillingService;

public class GenerateBillingUseCase {
    private final BillingService billingService;
    private final OrderRepository orderRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public GenerateBillingUseCase(BillingService billingService, OrderRepository orderRepository,
                                  PatientRepository patientRepository, UserRepository userRepository) {
        this.billingService = billingService;
        this.orderRepository = orderRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public Billing execute(String patientId, String doctorName, String orderNumber, double totalCost,
                          String appliedMedications, String appliedProcedures, String appliedDiagnosticAids, String adminId) {
        // Fetch domain entities
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNumber));

        Patient patient = patientRepository.findByIdentificationNumber(new Id(patientId))
            .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));

        User doctor = userRepository.findByIdentificationNumber(new Id(doctorName))
            .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorName));

        return billingService.generateBilling(order, patient, doctor, adminId);
    }
}