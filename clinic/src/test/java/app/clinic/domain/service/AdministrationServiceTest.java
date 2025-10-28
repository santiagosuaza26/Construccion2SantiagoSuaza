package app.clinic.domain.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import app.clinic.domain.model.entities.MedicationOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.ProcedureOrder;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.OrderRepository;

class AdministrationServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private AdministrationService administrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        administrationService = new AdministrationService(orderRepository);
    }

    @Test
    void shouldAdministerMedicationSuccessfully() {
        // Given
        String orderNumber = "000001";
        int item = 1;
        Order order = mock(Order.class);
        MedicationOrder medOrder = new MedicationOrder(new OrderNumber(orderNumber), item, new Id("1234567922"), "10mg", "7 days", 10000.0);
        when(order.getMedications()).thenReturn(List.of(medOrder));
        when(orderRepository.findByOrderNumber(any(OrderNumber.class))).thenReturn(Optional.of(order));

        // When
        administrationService.administerMedication(orderNumber, item);

        // Then
        verify(orderRepository).findByOrderNumber(any(OrderNumber.class));
        // In practice, record administration in a log or update status
    }

    @Test
    void shouldThrowExceptionForNonExistentOrder() {
        // Given
        when(orderRepository.findByOrderNumber(any(OrderNumber.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> administrationService.administerMedication("000001", 1));
    }

    @Test
    void shouldThrowExceptionForNonExistentMedicationItem() {
        // Given
        String orderNumber = "000001";
        int item = 2; // Not in order
        Order order = mock(Order.class);
        MedicationOrder medOrder = new MedicationOrder(new OrderNumber(orderNumber), 1, new Id("1234567923"), "10mg", "7 days", 10000.0);
        when(order.getMedications()).thenReturn(List.of(medOrder));
        when(orderRepository.findByOrderNumber(any(OrderNumber.class))).thenReturn(Optional.of(order));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> administrationService.administerMedication(orderNumber, item));
    }

    @Test
    void shouldAdministerProcedureSuccessfully() {
        // Given
        String orderNumber = "000001";
        int item = 1;
        Order order = mock(Order.class);
        ProcedureOrder procOrder = new ProcedureOrder(new OrderNumber(orderNumber), item, new Id("1234567924"), "1", "Daily", false, null, 20000.0);
        when(order.getProcedures()).thenReturn(List.of(procOrder));
        when(orderRepository.findByOrderNumber(any(OrderNumber.class))).thenReturn(Optional.of(order));

        // When
        administrationService.administerProcedure(orderNumber, item);

        // Then
        verify(orderRepository).findByOrderNumber(any(OrderNumber.class));
    }
}