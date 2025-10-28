package app.clinic.domain.service;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.entities.Insurance;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.EmergencyContact;
import app.clinic.domain.model.valueobject.Gender;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.repository.BillingRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;

class BillingServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleBasedAccessService roleBasedAccessService;

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billingService = new BillingService(billingRepository, patientRepository, orderRepository, userRepository, roleBasedAccessService);
    }

    @Test
    void shouldGenerateBillingWithActiveInsurance() {
        // Given
        String patientId = "123456789";
        String doctorName = "Dr. Smith";
        String orderNumber = "000001";
        double totalCost = 100000.0;
        String appliedMedications = "Aspirin";
        String appliedProcedures = "Checkup";
        String appliedDiagnosticAids = "";

        Patient patient = new Patient(new Id(patientId), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("1234567890"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("0987654321")), new Insurance("Company", "POL123", true, LocalDate.now().plusDays(30)));
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(Optional.of(patient));

        // When
        Billing billing = billingService.generateBilling(patientId, doctorName, orderNumber, totalCost, appliedMedications, appliedProcedures, appliedDiagnosticAids);

        // Then
        assertNotNull(billing);
        assertEquals(50000.0, billing.getCopay()); // Copago de $50,000
        assertEquals(50000.0, billing.getInsuranceCoverage());
        verify(billingRepository).save(billing);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldGenerateBillingWithInactiveInsurance() {
        // Given
        String patientId = "123456789";
        String doctorName = "Dr. Smith";
        String orderNumber = "000001";
        double totalCost = 100000.0;
        String appliedMedications = "Aspirin";
        String appliedProcedures = "Checkup";
        String appliedDiagnosticAids = "";

        Patient patient = new Patient(new Id(patientId), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("1234567890"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("0987654321")), new Insurance("Company", "POL123", true, LocalDate.now().minusDays(1)));
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(Optional.of(patient));

        // When
        Billing billing = billingService.generateBilling(patientId, doctorName, orderNumber, totalCost, appliedMedications, appliedProcedures, appliedDiagnosticAids);

        // Then
        assertNotNull(billing);
        assertEquals(100000.0, billing.getCopay()); // Pago completo
        assertEquals(0.0, billing.getInsuranceCoverage());
    }

    @Test
    void shouldGenerateBillingWithoutInsurance() {
        // Given
        String patientId = "123456789";
        String doctorName = "Dr. Smith";
        String orderNumber = "000001";
        double totalCost = 100000.0;
        String appliedMedications = "Aspirin";
        String appliedProcedures = "Checkup";
        String appliedDiagnosticAids = "";

        Patient patient = new Patient(new Id(patientId), "John Doe", new DateOfBirth("01/01/1990"), Gender.MASCULINO, new Address("123 Main St"), new Phone("1234567890"), new Email("john@example.com"), new EmergencyContact("Jane Doe", "Sister", new Phone("0987654321")), null);
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(Optional.of(patient));

        // When
        Billing billing = billingService.generateBilling(patientId, doctorName, orderNumber, totalCost, appliedMedications, appliedProcedures, appliedDiagnosticAids);

        // Then
        assertNotNull(billing);
        assertEquals(100000.0, billing.getCopay()); // Pago completo
        assertEquals(0.0, billing.getInsuranceCoverage());
    }

    @Test
    void shouldThrowExceptionForNonExistentPatient() {
        // Given
        when(patientRepository.findByIdentificationNumber(any(Id.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> billingService.generateBilling("123456789", "Dr. Smith", "000001", 100000.0, "Aspirin", "Checkup", ""));
    }
}