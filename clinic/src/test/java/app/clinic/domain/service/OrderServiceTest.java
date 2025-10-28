package app.clinic.domain.service;

import java.util.List;
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

import app.clinic.domain.model.entities.DiagnosticAidOrder;
import app.clinic.domain.model.entities.MedicationOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.ProcedureOrder;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.repository.InventoryRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleBasedAccessService roleBasedAccessService;

    @Mock
    private User mockUser;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, patientRepository, inventoryRepository, userRepository, roleBasedAccessService);
    }

    @Test
    void shouldCreateDiagnosticAidOrderSuccessfully() {
        // Given
        String patientId = "123456789";
        String doctorId = "987654321";
        List<DiagnosticAidOrder> diagnosticAids = List.of(new DiagnosticAidOrder(new OrderNumber("000001"), 1, new Id("1234567933"), "1", false, null, 50000.0));

        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(orderRepository.existsByOrderNumber(any(OrderNumber.class))).thenReturn(false);
        when(inventoryRepository.existsDiagnosticAidById(any(Id.class))).thenReturn(true);

        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(orderRepository.existsByOrderNumber(any(OrderNumber.class))).thenReturn(false);
        when(inventoryRepository.existsDiagnosticAidById(any(Id.class))).thenReturn(true);
        when(userRepository.findByIdentificationNumber(any(Id.class))).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.MEDICO);
        // Mock role check to do nothing

        // When
        Order order = orderService.createStandaloneDiagnosticAidOrder(patientId, doctorId, diagnosticAids);

        // Then
        assertNotNull(order);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionForNonExistentPatient() {
        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(false);
        when(userRepository.findByIdentificationNumber(any(Id.class))).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.MEDICO);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> orderService.createStandaloneDiagnosticAidOrder("123456789", "987654321", List.of()));
    }

    @Test
    void shouldCreatePostDiagnosticOrderSuccessfully() {
        // Given
        String patientId = "123456789";
        String doctorId = "987654321";
        String diagnosis = "Hypertension";
        List<MedicationOrder> medications = List.of(new MedicationOrder(new OrderNumber("000001"), 1, new Id("1234567934"), "10mg", "7 days", 10000.0));
        List<ProcedureOrder> procedures = List.of(new ProcedureOrder(new OrderNumber("000001"), 2, new Id("1234567935"), "1", "Daily", false, null, 20000.0));

        // Mock patient and inventory existence
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(orderRepository.existsByOrderNumber(any(OrderNumber.class))).thenReturn(false);
        when(inventoryRepository.existsMedicationById(any(Id.class))).thenReturn(true);
        when(inventoryRepository.existsProcedureById(any(Id.class))).thenReturn(true);

        // Mock that patient has diagnostic aid orders
        Order diagnosticOrder = new Order(new OrderNumber("000002"), patientId, doctorId, java.time.LocalDate.now());
        diagnosticOrder.addDiagnosticAid(new DiagnosticAidOrder(new OrderNumber("000002"), 1, new Id("1234567937"), "1", false, null, 30000.0));
        when(orderRepository.findByPatientIdentificationNumber(patientId)).thenReturn(List.of(diagnosticOrder));

        // When
        Order order = orderService.createPostDiagnosticOrder(patientId, doctorId, diagnosis, medications, procedures);

        // Then
        assertNotNull(order);
        assertEquals(diagnosis, order.getDiagnosis());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionForNonExistentMedication() {
        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);
        when(orderRepository.existsByOrderNumber(any(OrderNumber.class))).thenReturn(false);
        when(inventoryRepository.existsMedicationById(any(Id.class))).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> orderService.createPostDiagnosticOrder("123456789", "987654321", "Diagnosis", List.of(new MedicationOrder(new OrderNumber("000001"), 1, new Id("1234567936"), "10mg", "7 days", 10000.0)), List.of()));
    }
}