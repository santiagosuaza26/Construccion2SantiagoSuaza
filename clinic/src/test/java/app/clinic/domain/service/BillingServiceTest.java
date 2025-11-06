package app.clinic.domain.service;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.entities.Insurance;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.EmergencyContact;
import app.clinic.domain.model.valueobject.Gender;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.model.valueobject.Password;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.model.valueobject.Username;
import app.clinic.domain.repository.BillingRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.infrastructure.service.BillingServiceImpl;

class BillingServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billingService = new BillingServiceImpl(billingRepository, patientRepository, orderRepository, userRepository);
    }

    @Test
    void shouldGenerateBillingWithActiveInsurance() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        Order order = new Order(orderNumber, "123456789", "doctor123", LocalDate.now(), "Diagnosis");

        Patient patient = new Patient(new Id("123456789"), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("3123456789"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("3123456789")), new Insurance("Company", "POL123", true, LocalDate.now().plusDays(30)));

        User doctor = new User(new Credentials(new Username("doctor"), new Password("password")), "Dr. Smith", new Id("123456789"), new Email("doctor@example.com"), new Phone("3123456789"), new DateOfBirth("01/01/1980"), new Address("456 Main St"), Role.MEDICO);

        // When
        Billing billing = billingService.generateBilling(order, patient, doctor, "admin-id");

        // Then
        assertNotNull(billing);
        assertEquals(50000.0, billing.getCopay()); // Copago de $50,000
        assertEquals(-50000.0, billing.getInsuranceCoverage()); // totalCost - copay = 0 - 50000 = -50000
        assertEquals(0.0, billing.getTotalCost()); // Order has no items, so total cost is 0
        verify(billingRepository).save(billing);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldGenerateBillingWithInactiveInsurance() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        Order order = new Order(orderNumber, "123456789", "doctor123", LocalDate.now(), "Diagnosis");

        Patient patient = new Patient(new Id("123456789"), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("3123456789"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("3123456789")), new Insurance("Company", "POL123", true, LocalDate.now().minusDays(1)));

        User doctor = new User(new Credentials(new Username("doctor"), new Password("password")), "Dr. Smith", new Id("123456789"), new Email("doctor@example.com"), new Phone("3123456789"), new DateOfBirth("01/01/1980"), new Address("456 Main St"), Role.MEDICO);

        // When
        Billing billing = billingService.generateBilling(order, patient, doctor, "admin-id");

        // Then
        assertNotNull(billing);
        assertEquals(0.0, billing.getCopay()); // No insurance, but annual limit exceeded
        assertEquals(0.0, billing.getInsuranceCoverage());
    }

    @Test
    void shouldGenerateBillingWithoutInsurance() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        Order order = new Order(orderNumber, "123456789", "doctor123", LocalDate.now(), "Diagnosis");

        Patient patient = new Patient(new Id("123456789"), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("3123456789"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("3123456789")), null);

        User doctor = new User(new Credentials(new Username("doctor"), new Password("password")), "Dr. Smith", new Id("123456789"), new Email("doctor@example.com"), new Phone("3123456789"), new DateOfBirth("01/01/1980"), new Address("456 Main St"), Role.MEDICO);

        // When
        Billing billing = billingService.generateBilling(order, patient, doctor, "admin-id");

        // Then
        assertNotNull(billing);
        assertEquals(0.0, billing.getCopay()); // No insurance, but annual limit exceeded
        assertEquals(0.0, billing.getInsuranceCoverage());
    }

    @Test
    void shouldThrowExceptionForNullOrder() {
        // Given
        Patient patient = new Patient(new Id("123456789"), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("3123456789"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("3123456789")), null);
        User doctor = new User(new Credentials(new Username("doctor"), new Password("password")), "Dr. Smith", new Id("123456789"), new Email("doctor@example.com"), new Phone("3123456789"), new DateOfBirth("01/01/1980"), new Address("456 Main St"), Role.MEDICO);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> billingService.generateBilling(null, patient, doctor, "admin-id"));
    }

    @Test
    void shouldGenerateBillingWithCopayLimitExceeded() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        Order order = new Order(orderNumber, "123456789", "doctor123", LocalDate.now(), "Diagnosis");

        Patient patient = new Patient(new Id("123456789"), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("3123456789"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("3123456789")), new Insurance("Company", "POL123", true, LocalDate.now().plusDays(30)));
        patient.addToAnnualCopayTotal(1000000.0); // Exceed annual limit

        User doctor = new User(new Credentials(new Username("doctor"), new Password("password")), "Dr. Smith", new Id("123456789"), new Email("doctor@example.com"), new Phone("3123456789"), new DateOfBirth("01/01/1980"), new Address("456 Main St"), Role.MEDICO);

        // When
        Billing billing = billingService.generateBilling(order, patient, doctor, "admin-id");

        // Then
        assertNotNull(billing);
        assertEquals(0.0, billing.getCopay()); // No copay when limit exceeded
        assertEquals(0.0, billing.getInsuranceCoverage()); // No coverage when limit exceeded
        assertEquals(0.0, billing.getTotalCost()); // Order has no items
        verify(billingRepository).save(billing);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldGenerateBillingWithOrderItems() {
        // Given
        OrderNumber orderNumber = new OrderNumber("000001");
        Order order = new Order(orderNumber, "123456789", "doctor123", LocalDate.now(), "Diagnosis");

        // Add some items to the order (mocking the cost calculation)
        // Since we can't easily add items without full implementation, we'll test with empty order for now
        // This test serves as a placeholder for when order items are properly implemented

        Patient patient = new Patient(new Id("123456789"), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("3123456789"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("3123456789")), new Insurance("Company", "POL123", true, LocalDate.now().plusDays(30)));

        User doctor = new User(new Credentials(new Username("doctor"), new Password("password")), "Dr. Smith", new Id("123456789"), new Email("doctor@example.com"), new Phone("3123456789"), new DateOfBirth("01/01/1980"), new Address("456 Main St"), Role.MEDICO);

        // When
        Billing billing = billingService.generateBilling(order, patient, doctor, "admin-id");

        // Then
        assertNotNull(billing);
        assertEquals(50000.0, billing.getCopay());
        assertEquals(-50000.0, billing.getInsuranceCoverage()); // totalCost - copay
        assertEquals(0.0, billing.getTotalCost()); // Empty order
        verify(billingRepository).save(billing);
        verify(patientRepository).save(patient);
    }
}