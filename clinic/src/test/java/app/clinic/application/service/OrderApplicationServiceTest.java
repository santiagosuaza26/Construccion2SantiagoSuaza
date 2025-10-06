package app.clinic.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.application.dto.order.CreateOrderDTO;
import app.clinic.application.dto.order.OrderDTO;
import app.clinic.application.dto.order.OrderItemDTO;
import app.clinic.domain.model.MedicationOrder;
import app.clinic.domain.model.OrderNumber;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.port.InventoryRepository;
import app.clinic.domain.service.OrderDomainService;

@ExtendWith(MockitoExtension.class)
class OrderApplicationServiceTest {

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderApplicationService orderApplicationService;

    private CreateOrderDTO createOrderDTO;
    private OrderItemDTO medicationItem;

    @BeforeEach
    void setUp() {
        // Crear DTO de ítem de medicamento para pruebas
        medicationItem = new OrderItemDTO();
        medicationItem.setType("MEDICATION");
        medicationItem.setInventoryItemId("MED001");
        medicationItem.setName("Paracetamol");
        medicationItem.setQuantity(10);
        medicationItem.setDosage("500mg");
        medicationItem.setTreatmentDuration("5 días");
        medicationItem.setCost("$50.00");
        medicationItem.setRequiresSpecialistAssistance(false);

        // Crear DTO de orden para pruebas
        createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setPatientCedula("12345678");
        createOrderDTO.setDoctorCedula("87654321");
        createOrderDTO.setOrderNumber("ORD000001");
        createOrderDTO.setItems(Arrays.asList(medicationItem));
    }

    @Test
    void testValidateOrderBusinessRules_ValidMedicationOrder() {
        // Given
        CreateOrderDTO validOrder = createOrderDTO;

        // When
        boolean result = orderApplicationService.validateOrderBusinessRules(validOrder);

        // Then
        assertTrue(result);
    }

    @Test
    void testValidateOrderBusinessRules_InvalidEmptyItems() {
        // Given
        CreateOrderDTO invalidOrder = createOrderDTO;
        invalidOrder.setItems(Arrays.asList());

        // When
        boolean result = orderApplicationService.validateOrderBusinessRules(invalidOrder);

        // Then
        assertFalse(result);
    }

    @Test
    void testValidateOrderBusinessRules_InvalidMixedTypes() {
        // Given - Crear orden con tipos mixtos (no permitido)
        OrderItemDTO medicationItem2 = new OrderItemDTO();
        medicationItem2.setType("MEDICATION");
        medicationItem2.setInventoryItemId("MED002");
        medicationItem2.setName("Aspirina");
        medicationItem2.setQuantity(5);
        medicationItem2.setDosage("100mg");
        medicationItem2.setCost("$25.00");
        medicationItem2.setRequiresSpecialistAssistance(false);

        OrderItemDTO procedureItem = new OrderItemDTO();
        procedureItem.setType("PROCEDURE");
        procedureItem.setInventoryItemId("PROC001");
        procedureItem.setName("Consulta");
        procedureItem.setQuantity(1);
        procedureItem.setFrequency("Una vez");
        procedureItem.setCost("$100.00");
        procedureItem.setRequiresSpecialistAssistance(false);

        CreateOrderDTO mixedOrder = createOrderDTO;
        mixedOrder.setItems(Arrays.asList(medicationItem2, procedureItem));

        // When
        boolean result = orderApplicationService.validateOrderBusinessRules(mixedOrder);

        // Then
        assertFalse(result, "No debería permitir tipos mixtos en una orden");
    }

    @Test
    void testGenerateNextOrderNumber_UniqueNumber() {
        // Given
        when(orderDomainService.findMedicationOrderByNumber(any())).thenReturn(Optional.empty());
        when(orderDomainService.findProcedureOrderByNumber(any())).thenReturn(Optional.empty());
        when(orderDomainService.findDiagnosticAidOrderByNumber(any())).thenReturn(Optional.empty());

        // When
        String orderNumber1 = orderApplicationService.generateNextOrderNumber();
        String orderNumber2 = orderApplicationService.generateNextOrderNumber();

        // Then
        assertNotNull(orderNumber1);
        assertNotNull(orderNumber2);
        assertTrue(orderNumber1.startsWith("ORD"));
        assertTrue(orderNumber2.startsWith("ORD"));
        assertNotEquals(orderNumber1, orderNumber2);
    }

    @Test
    void testIsOrderNumberAvailable_AvailableNumber() {
        // Given
        String availableNumber = "ORD000999";
        when(orderDomainService.findMedicationOrderByNumber(any())).thenReturn(Optional.empty());
        when(orderDomainService.findProcedureOrderByNumber(any())).thenReturn(Optional.empty());
        when(orderDomainService.findDiagnosticAidOrderByNumber(any())).thenReturn(Optional.empty());

        // When
        boolean result = orderApplicationService.isOrderNumberAvailable(availableNumber);

        // Then
        assertTrue(result);
    }

    @Test
    void testFindOrdersByPatientCedula_ValidPatient() {
        // Given
        String patientCedula = "12345678";
        PatientCedula patientCedulaObj = PatientCedula.of(patientCedula);

        MedicationOrder mockMedicationOrder = mock(MedicationOrder.class);
        when(mockMedicationOrder.getOrderNumber()).thenReturn(OrderNumber.of("ORD000001"));
        when(mockMedicationOrder.getPatientCedula()).thenReturn(patientCedulaObj);

        when(orderDomainService.findMedicationOrdersByPatient(patientCedulaObj))
            .thenReturn(Arrays.asList(mockMedicationOrder));

        // When
        List<OrderDTO> result = orderApplicationService.findOrdersByPatientCedula(patientCedula);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindOrdersByPatientCedula_InvalidPatient() {
        // Given
        String invalidPatientCedula = "";

        // When
        List<OrderDTO> result = orderApplicationService.findOrdersByPatientCedula(invalidPatientCedula);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetOrderStatistics_ValidPatient() {
        // Given
        String patientCedula = "12345678";
        PatientCedula patientCedulaObj = PatientCedula.of(patientCedula);

        MedicationOrder mockMedicationOrder = mock(MedicationOrder.class);
        when(mockMedicationOrder.getOrderNumber()).thenReturn(OrderNumber.of("ORD000001"));
        when(mockMedicationOrder.getPatientCedula()).thenReturn(patientCedulaObj);

        when(orderDomainService.findMedicationOrdersByPatient(patientCedulaObj))
            .thenReturn(Arrays.asList(mockMedicationOrder));

        // When
        OrderApplicationService.OrderStatisticsDTO result =
            orderApplicationService.getOrderStatistics(patientCedula);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalOrders());
        assertEquals(1, result.getMedicationOrders());
        assertEquals(0, result.getProcedureOrders());
        assertEquals(0, result.getDiagnosticAidOrders());
    }

    @Test
    void testGetOrderStatistics_InvalidPatient() {
        // Given
        String invalidPatientCedula = "";

        // When
        OrderApplicationService.OrderStatisticsDTO result =
            orderApplicationService.getOrderStatistics(invalidPatientCedula);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalOrders());
        assertEquals(0, result.getMedicationOrders());
        assertEquals(0, result.getProcedureOrders());
        assertEquals(0, result.getDiagnosticAidOrders());
    }
}