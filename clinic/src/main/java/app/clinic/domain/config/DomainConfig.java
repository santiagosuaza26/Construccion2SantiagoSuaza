package app.clinic.domain.config;

import app.clinic.domain.port.AppointmentRepository;
import app.clinic.domain.port.BillingRepository;
import app.clinic.domain.port.InventoryRepository;
import app.clinic.domain.port.MedicalRecordRepository;
import app.clinic.domain.port.OrderRepository;
import app.clinic.domain.port.PatientRepository;
import app.clinic.domain.port.PatientVisitRepository;
import app.clinic.domain.port.UserRepository;
import app.clinic.domain.service.AppointmentDomainService;
import app.clinic.domain.service.BillingDomainService;
import app.clinic.domain.service.InventoryDomainService;
import app.clinic.domain.service.MedicalRecordDomainService;
import app.clinic.domain.service.OrderDomainService;
import app.clinic.domain.service.PatientDomainService;
import app.clinic.domain.service.PatientVisitDomainService;
import app.clinic.domain.service.UserDomainService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Domain Layer.
 * Creates and configures all domain services without coupling them to Spring.
 * This factory ensures domain services remain independent of external frameworks.
 */
@Configuration
public class DomainConfig {

    /**
     * Creates UserDomainService instance.
     * Pure domain service without external dependencies.
     */
    @Bean
    public UserDomainService userDomainService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }

    /**
     * Creates PatientDomainService instance.
     * Handles patient-related business logic independently.
     */
    @Bean
    public PatientDomainService patientDomainService(PatientRepository patientRepository) {
        return new PatientDomainService(patientRepository);
    }

    /**
     * Creates AppointmentDomainService instance.
     * Manages appointment scheduling business rules.
     */
    @Bean
    public AppointmentDomainService appointmentDomainService(
            AppointmentRepository appointmentRepository) {
        return new AppointmentDomainService(appointmentRepository);
    }

    /**
     * Creates BillingDomainService instance.
     * Handles billing calculations and copayment logic.
     */
    @Bean
    public BillingDomainService billingDomainService(
            BillingRepository billingRepository,
            PatientRepository patientRepository) {
        return new BillingDomainService(billingRepository, patientRepository);
    }

    /**
     * Creates MedicalRecordDomainService instance.
     * Manages medical records and clinical documentation.
     */
    @Bean
    public MedicalRecordDomainService medicalRecordDomainService(
            MedicalRecordRepository medicalRecordRepository) {
        return new MedicalRecordDomainService(medicalRecordRepository);
    }

    /**
     * Creates InventoryDomainService instance.
     * Handles inventory management business logic.
     */
    @Bean
    public InventoryDomainService inventoryDomainService(InventoryRepository inventoryRepository) {
        return new InventoryDomainService(inventoryRepository);
    }

    /**
     * Creates OrderDomainService instance.
     * Manages medical orders and prescriptions.
     */
    @Bean
    public OrderDomainService orderDomainService(
            OrderRepository orderRepository) {
        return new OrderDomainService(orderRepository);
    }

    /**
     * Creates PatientVisitDomainService instance.
     * Handles patient visit tracking and vital signs.
     */
    @Bean
    public PatientVisitDomainService patientVisitDomainService(
            PatientVisitRepository patientVisitRepository) {
        return new PatientVisitDomainService(patientVisitRepository);
    }
}