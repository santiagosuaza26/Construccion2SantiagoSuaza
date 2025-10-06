package app.clinic.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.order.CreateOrderDTO;
import app.clinic.application.dto.order.DiagnosticAidOrderDTO;
import app.clinic.application.dto.order.MedicationOrderDTO;
import app.clinic.application.dto.order.OrderDTO;
import app.clinic.application.dto.order.OrderItemDTO;
import app.clinic.application.dto.order.ProcedureOrderDTO;
import app.clinic.domain.model.DiagnosticAidItem;
import app.clinic.domain.model.DiagnosticAidOrder;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.Dosage;
import app.clinic.domain.model.Frequency;
import app.clinic.domain.model.InventoryItemId;
import app.clinic.domain.model.ItemNumber;
import app.clinic.domain.model.MedicalSpecialty;
import app.clinic.domain.model.MedicationItem;
import app.clinic.domain.model.MedicationOrder;
import app.clinic.domain.model.OrderCreationDate;
import app.clinic.domain.model.OrderNumber;
import app.clinic.domain.model.OrderStatus;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.ProcedureItem;
import app.clinic.domain.model.ProcedureOrder;
import app.clinic.domain.model.Quantity;
import app.clinic.domain.model.RequiresSpecialistAssistance;
import app.clinic.domain.model.TreatmentDuration;
import app.clinic.domain.port.InventoryRepository;
import app.clinic.domain.service.OrderDomainService;

/**
 * Application service for medical order management operations.
 * Coordinates between REST controllers and domain services.
 * Handles medical order-related use cases and business operations.
 */
@Service
public class OrderApplicationService {

    private final OrderDomainService orderDomainService;
    private final InventoryRepository inventoryRepository;

    public OrderApplicationService(OrderDomainService orderDomainService, InventoryRepository inventoryRepository) {
        this.orderDomainService = orderDomainService;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Creates a new medical order.
     */
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        // Validate business rules first
        if (!validateOrderBusinessRules(createOrderDTO)) {
            throw new IllegalArgumentException("Order violates business rules");
        }

        // Generate order number if not provided
        String orderNumber = createOrderDTO.getOrderNumber() != null ?
            createOrderDTO.getOrderNumber() : generateNextOrderNumber();

        try {
            // Create domain entities based on order type
            OrderNumber orderNumberObj = OrderNumber.of(orderNumber);
            PatientCedula patientCedula = PatientCedula.of(createOrderDTO.getPatientCedula());
            DoctorCedula doctorCedula = DoctorCedula.of(createOrderDTO.getDoctorCedula());
            OrderCreationDate creationDate = OrderCreationDate.now();

            // Determine order type and create appropriate domain entity
            if (isMedicationOrder(createOrderDTO)) {
                List<MedicationItem> medications = convertToMedicationItems(createOrderDTO.getItems());
                MedicationOrder medicationOrder = MedicationOrder.of(
                    orderNumberObj, patientCedula, doctorCedula, creationDate,
                    OrderStatus.CREADA, medications);
                MedicationOrder savedOrder = orderDomainService.createMedicationOrder(medicationOrder);

                return mapMedicationOrderToDTO(savedOrder);

            } else if (isProcedureOrder(createOrderDTO)) {
                List<ProcedureItem> procedures = convertToProcedureItems(createOrderDTO.getItems());
                ProcedureOrder procedureOrder = ProcedureOrder.of(
                    orderNumberObj, patientCedula, doctorCedula, creationDate,
                    OrderStatus.CREADA, procedures);
                ProcedureOrder savedOrder = orderDomainService.createProcedureOrder(procedureOrder);

                return mapProcedureOrderToDTO(savedOrder);

            } else if (isDiagnosticAidOrder(createOrderDTO)) {
                List<DiagnosticAidItem> diagnosticAids = convertToDiagnosticAidItems(createOrderDTO.getItems());
                DiagnosticAidOrder diagnosticAidOrder = DiagnosticAidOrder.of(
                    orderNumberObj, patientCedula, doctorCedula, creationDate,
                    OrderStatus.CREADA, diagnosticAids);
                DiagnosticAidOrder savedOrder = orderDomainService.createDiagnosticAidOrder(diagnosticAidOrder);

                return mapDiagnosticAidOrderToDTO(savedOrder);

            } else {
                throw new IllegalArgumentException("Invalid order type or mixed order types not allowed");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an order by its number.
     * Searches across all order types (medication, procedure, diagnostic aid).
     */
    public Optional<OrderDTO> findOrderByNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            OrderNumber orderNumberObj = OrderNumber.of(orderNumber);

            // Search for medication order first
            Optional<MedicationOrder> medicationOrder = orderDomainService.findMedicationOrderByNumber(orderNumberObj);
            if (medicationOrder.isPresent()) {
                return Optional.of(mapMedicationOrderToDTO(medicationOrder.get()));
            }

            // Search for procedure order
            Optional<ProcedureOrder> procedureOrder = orderDomainService.findProcedureOrderByNumber(orderNumberObj);
            if (procedureOrder.isPresent()) {
                return Optional.of(mapProcedureOrderToDTO(procedureOrder.get()));
            }

            // Search for diagnostic aid order
            Optional<DiagnosticAidOrder> diagnosticAidOrder = orderDomainService.findDiagnosticAidOrderByNumber(orderNumberObj);
            if (diagnosticAidOrder.isPresent()) {
                return Optional.of(mapDiagnosticAidOrderToDTO(diagnosticAidOrder.get()));
            }

            // Order not found
            return Optional.empty();

        } catch (IllegalArgumentException e) {
            // Invalid order number format
            return Optional.empty();
        }
    }

    /**
     * Finds all orders for a specific patient.
     */
    public List<OrderDTO> findOrdersByPatientCedula(String patientCedula) {
        if (patientCedula == null || patientCedula.trim().isEmpty()) {
            return List.of();
        }

        try {
            PatientCedula patientCedulaObj = PatientCedula.of(patientCedula.trim());
            List<OrderDTO> orders = new ArrayList<>();

            // Search in all order types
            List<MedicationOrder> medicationOrders = orderDomainService.findMedicationOrdersByPatient(patientCedulaObj);
            medicationOrders.forEach(order -> orders.add(mapMedicationOrderToDTO(order)));

            List<ProcedureOrder> procedureOrders = orderDomainService.findProcedureOrdersByPatient(patientCedulaObj);
            procedureOrders.forEach(order -> orders.add(mapProcedureOrderToDTO(order)));

            List<DiagnosticAidOrder> diagnosticAidOrders = orderDomainService.findDiagnosticAidOrdersByPatient(patientCedulaObj);
            diagnosticAidOrders.forEach(order -> orders.add(mapDiagnosticAidOrderToDTO(order)));

            return orders;

        } catch (IllegalArgumentException e) {
            // Invalid patient cedula format
            return List.of();
        }
    }

    /**
     * Finds all orders created by a specific doctor.
     */
    public List<OrderDTO> findOrdersByDoctorCedula(String doctorCedula) {
        if (doctorCedula == null || doctorCedula.trim().isEmpty()) {
            return List.of();
        }

        try {
            DoctorCedula doctorCedulaObj = DoctorCedula.of(doctorCedula.trim());

            // Use the new domain service method to find orders by doctor
            List<Object> domainOrders = orderDomainService.findOrdersByDoctor(doctorCedulaObj);

            List<OrderDTO> orders = new ArrayList<>();
            for (Object domainOrder : domainOrders) {
                if (domainOrder instanceof MedicationOrder medicationOrder) {
                    orders.add(mapMedicationOrderToDTO(medicationOrder));
                } else if (domainOrder instanceof ProcedureOrder procedureOrder) {
                    orders.add(mapProcedureOrderToDTO(procedureOrder));
                } else if (domainOrder instanceof DiagnosticAidOrder diagnosticAidOrder) {
                    orders.add(mapDiagnosticAidOrderToDTO(diagnosticAidOrder));
                }
            }

            return orders;

        } catch (IllegalArgumentException e) {
            // Invalid doctor cedula format
            return List.of();
        }
    }

    /**
     * Finds all orders within a date range.
     */
    public List<OrderDTO> findOrdersByDateRange(String startDate, String endDate) {
        if (startDate == null || endDate == null || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
            return List.of();
        }

        try {
            // Parse dates - expecting format yyyy-MM-dd
            java.time.LocalDate start = java.time.LocalDate.parse(startDate.trim());
            java.time.LocalDate end = java.time.LocalDate.parse(endDate.trim());

            if (start.isAfter(end)) {
                return List.of(); // Invalid date range
            }

            // Convert to LocalDateTime for OrderCreationDate
            java.time.LocalDateTime startDateTime = start.atStartOfDay();
            java.time.LocalDateTime endDateTime = end.atTime(23, 59, 59);

            OrderCreationDate startDateObj = OrderCreationDate.of(startDateTime);
            OrderCreationDate endDateObj = OrderCreationDate.of(endDateTime);

            // Use the new domain service method to find orders by date range
            List<Object> domainOrders = orderDomainService.findOrdersByDateRange(startDateObj, endDateObj);

            List<OrderDTO> orders = new ArrayList<>();
            for (Object domainOrder : domainOrders) {
                if (domainOrder instanceof MedicationOrder medicationOrder) {
                    orders.add(mapMedicationOrderToDTO(medicationOrder));
                } else if (domainOrder instanceof ProcedureOrder procedureOrder) {
                    orders.add(mapProcedureOrderToDTO(procedureOrder));
                } else if (domainOrder instanceof DiagnosticAidOrder diagnosticAidOrder) {
                    orders.add(mapDiagnosticAidOrderToDTO(diagnosticAidOrder));
                }
            }

            return orders;

        } catch (Exception e) {
            // Invalid date format or other error
            return List.of();
        }
    }

    /**
     * Updates an existing order.
     */
    public OrderDTO updateOrder(String orderNumber, CreateOrderDTO updateDTO) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be null or empty");
        }

        if (updateDTO == null) {
            throw new IllegalArgumentException("Update DTO cannot be null");
        }

        // Validate business rules first
        if (!validateOrderBusinessRules(updateDTO)) {
            throw new IllegalArgumentException("Order violates business rules");
        }

        try {
            OrderNumber orderNumberObj = OrderNumber.of(orderNumber.trim());
            PatientCedula patientCedula = PatientCedula.of(updateDTO.getPatientCedula());
            DoctorCedula doctorCedula = DoctorCedula.of(updateDTO.getDoctorCedula());
            OrderCreationDate creationDate = OrderCreationDate.now();

            // Find existing order first
            Optional<OrderDTO> existingOrderOpt = findOrderByNumber(orderNumber);
            if (existingOrderOpt.isEmpty()) {
                throw new IllegalArgumentException("Order not found: " + orderNumber);
            }

            // Determine order type and update appropriate domain entity
            if (isMedicationOrder(updateDTO)) {
                List<MedicationItem> medications = convertToMedicationItems(updateDTO.getItems());
                MedicationOrder medicationOrder = MedicationOrder.of(
                    orderNumberObj, patientCedula, doctorCedula, creationDate,
                    OrderStatus.CREADA, medications);
                MedicationOrder updatedOrder = orderDomainService.updateMedicationOrder(medicationOrder);

                return mapMedicationOrderToDTO(updatedOrder);

            } else if (isProcedureOrder(updateDTO)) {
                List<ProcedureItem> procedures = convertToProcedureItems(updateDTO.getItems());
                ProcedureOrder procedureOrder = ProcedureOrder.of(
                    orderNumberObj, patientCedula, doctorCedula, creationDate,
                    OrderStatus.CREADA, procedures);
                ProcedureOrder updatedOrder = orderDomainService.updateProcedureOrder(procedureOrder);

                return mapProcedureOrderToDTO(updatedOrder);

            } else if (isDiagnosticAidOrder(updateDTO)) {
                List<DiagnosticAidItem> diagnosticAids = convertToDiagnosticAidItems(updateDTO.getItems());
                DiagnosticAidOrder diagnosticAidOrder = DiagnosticAidOrder.of(
                    orderNumberObj, patientCedula, doctorCedula, creationDate,
                    OrderStatus.CREADA, diagnosticAids);
                DiagnosticAidOrder updatedOrder = orderDomainService.updateDiagnosticAidOrder(diagnosticAidOrder);

                return mapDiagnosticAidOrderToDTO(updatedOrder);

            } else {
                throw new IllegalArgumentException("Invalid order type or mixed order types not allowed");
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error updating order: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an order by its number.
     */
    public void deleteOrder(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be null or empty");
        }

        try {
            OrderNumber orderNumberObj = OrderNumber.of(orderNumber.trim());

            // Try to delete from all order types
            boolean deleted = false;

            // Try to delete as medication order first
            try {
                orderDomainService.deleteMedicationOrder(orderNumberObj);
                deleted = true;
            } catch (IllegalArgumentException e) {
                // Order not found as medication order, try other types
            }

            // Try to delete as procedure order
            if (!deleted) {
                try {
                    orderDomainService.deleteProcedureOrder(orderNumberObj);
                    deleted = true;
                } catch (IllegalArgumentException e) {
                    // Order not found as procedure order, try other types
                }
            }

            // Try to delete as diagnostic aid order
            if (!deleted) {
                try {
                    orderDomainService.deleteDiagnosticAidOrder(orderNumberObj);
                    deleted = true;
                } catch (IllegalArgumentException e) {
                    // Order not found as diagnostic aid order
                }
            }

            if (!deleted) {
                throw new IllegalArgumentException("Order not found: " + orderNumber);
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error deleting order: " + e.getMessage(), e);
        }
    }

    /**
     * Validates business rules for order creation.
     * Ensures no mixing of diagnostic aids with medications/procedures.
     */
    public boolean validateOrderBusinessRules(CreateOrderDTO orderDTO) {
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            return false;
        }

        boolean hasDiagnosticAid = false;
        boolean hasMedication = false;
        boolean hasProcedure = false;

        // Check for mixing of diagnostic aids with other types
        for (OrderItemDTO item : orderDTO.getItems()) {
            switch (item.getType().toUpperCase()) {
                case "DIAGNOSTIC_AID":
                    hasDiagnosticAid = true;
                    break;
                case "MEDICATION":
                    hasMedication = true;
                    break;
                case "PROCEDURE":
                    hasProcedure = true;
                    break;
                default:
                    return false; // Invalid item type
            }
        }

        // Business rule: Cannot mix diagnostic aids with medications or procedures
        if (hasDiagnosticAid && (hasMedication || hasProcedure)) {
            return false;
        }

        // Additional validations
        if (orderDTO.getOrderNumber() != null && !isOrderNumberAvailable(orderDTO.getOrderNumber())) {
            return false;
        }

        // Validate that all items have unique inventory item IDs within the order
        List<String> inventoryIds = orderDTO.getItems().stream()
            .map(OrderItemDTO::getInventoryItemId)
            .toList();

        if (inventoryIds.size() != inventoryIds.stream().distinct().count()) {
            return false; // Duplicate inventory items in same order
        }

        return true;
    }

    /**
     * Generates the next available order number.
     * Uses a sequential number format with validation to ensure uniqueness.
     * @return A unique order number in format ORDxxxxxx (6 digits)
     */
    public String generateNextOrderNumber() {
        // Start with a base number and increment until we find an available one
        int attempt = 0;
        final int maxAttempts = 10000; // Prevent infinite loops

        while (attempt < maxAttempts) {
            // Generate a sequential number starting from current timestamp seconds
            long currentSeconds = java.time.Instant.now().getEpochSecond();
            int sequentialNumber = (int)(currentSeconds + attempt) % 1000000; // 6-digit number

            String orderNumber = String.format("ORD%06d", sequentialNumber);

            // Check if this order number is available
            if (isOrderNumberAvailable(orderNumber)) {
                return orderNumber;
            }

            attempt++;
        }

        // Fallback: if we can't find an available number after max attempts
        throw new RuntimeException("Unable to generate a unique order number after " + maxAttempts + " attempts");
    }

    /**
     * Checks if an order number is available.
     * @param orderNumber The order number to check
     * @return true if the order number is available, false if already exists
     */
    public boolean isOrderNumberAvailable(String orderNumber) {
        try {
            OrderNumber orderNumberObj = OrderNumber.of(orderNumber);

            // Check if order number exists in any of the three order types
            return orderDomainService.findMedicationOrderByNumber(orderNumberObj).isEmpty() &&
                   orderDomainService.findProcedureOrderByNumber(orderNumberObj).isEmpty() &&
                   orderDomainService.findDiagnosticAidOrderByNumber(orderNumberObj).isEmpty();
        } catch (IllegalArgumentException e) {
            // If order number format is invalid, consider it as not available
            return false;
        }
    }

    /**
     * Determines if the order contains only medication items.
     */
    private boolean isMedicationOrder(CreateOrderDTO createOrderDTO) {
        return createOrderDTO.getItems().stream()
            .allMatch(item -> "MEDICATION".equalsIgnoreCase(item.getType()));
    }

    /**
     * Determines if the order contains only procedure items.
     */
    private boolean isProcedureOrder(CreateOrderDTO createOrderDTO) {
        return createOrderDTO.getItems().stream()
            .allMatch(item -> "PROCEDURE".equalsIgnoreCase(item.getType()));
    }

    /**
     * Determines if the order contains only diagnostic aid items.
     */
    private boolean isDiagnosticAidOrder(CreateOrderDTO createOrderDTO) {
        return createOrderDTO.getItems().stream()
            .allMatch(item -> "DIAGNOSTIC_AID".equalsIgnoreCase(item.getType()));
    }

    /**
     * Converts OrderItemDTO list to MedicationItem list.
     */
    private List<MedicationItem> convertToMedicationItems(List<OrderItemDTO> items) {
        return items.stream()
            .map(itemDTO -> convertToMedicationItem(itemDTO, items.indexOf(itemDTO)))
            .toList();
    }

    /**
     * Converts a single OrderItemDTO to MedicationItem.
     */
    private MedicationItem convertToMedicationItem(OrderItemDTO itemDTO, int index) {
        ItemNumber itemNumber = ItemNumber.of(index + 1);
        InventoryItemId medicationId = InventoryItemId.of(itemDTO.getInventoryItemId());
        Dosage dosage = Dosage.of(itemDTO.getDosage() != null ? itemDTO.getDosage() : "");
        TreatmentDuration duration = TreatmentDuration.of(itemDTO.getTreatmentDuration() != null ?
            itemDTO.getTreatmentDuration() : "");

        return MedicationItem.of(itemNumber, medicationId, dosage, duration);
    }

    /**
     * Converts OrderItemDTO list to ProcedureItem list.
     */
    private List<ProcedureItem> convertToProcedureItems(List<OrderItemDTO> items) {
        return items.stream()
            .map(itemDTO -> convertToProcedureItem(itemDTO, items.indexOf(itemDTO)))
            .toList();
    }

    /**
     * Converts a single OrderItemDTO to ProcedureItem.
     */
    private ProcedureItem convertToProcedureItem(OrderItemDTO itemDTO, int index) {
        ItemNumber itemNumber = ItemNumber.of(index + 1);
        InventoryItemId procedureId = InventoryItemId.of(itemDTO.getInventoryItemId());
        Quantity quantity = Quantity.of(itemDTO.getQuantity());
        Frequency frequency = Frequency.of(itemDTO.getFrequency() != null ? itemDTO.getFrequency() : "");

        RequiresSpecialistAssistance requiresSpecialist = RequiresSpecialistAssistance.of(
            Boolean.TRUE.equals(itemDTO.getRequiresSpecialistAssistance()));

        MedicalSpecialty specialistType = null;
        if (itemDTO.getSpecialistTypeId() != null && !itemDTO.getSpecialistTypeId().isEmpty()) {
            try {
                specialistType = MedicalSpecialty.valueOf(itemDTO.getSpecialistTypeId().toUpperCase());
            } catch (IllegalArgumentException e) {
                specialistType = MedicalSpecialty.GENERAL;
            }
        }

        return ProcedureItem.of(itemNumber, procedureId, quantity, frequency,
                               requiresSpecialist, specialistType);
    }

    /**
     * Converts OrderItemDTO list to DiagnosticAidItem list.
     */
    private List<DiagnosticAidItem> convertToDiagnosticAidItems(List<OrderItemDTO> items) {
        return items.stream()
            .map(itemDTO -> convertToDiagnosticAidItem(itemDTO, items.indexOf(itemDTO)))
            .toList();
    }

    /**
     * Converts a single OrderItemDTO to DiagnosticAidItem.
     */
    private DiagnosticAidItem convertToDiagnosticAidItem(OrderItemDTO itemDTO, int index) {
        ItemNumber itemNumber = ItemNumber.of(index + 1);
        InventoryItemId diagnosticAidId = InventoryItemId.of(itemDTO.getInventoryItemId());
        Quantity quantity = Quantity.of(itemDTO.getQuantity());

        RequiresSpecialistAssistance requiresSpecialist = RequiresSpecialistAssistance.of(
            Boolean.TRUE.equals(itemDTO.getRequiresSpecialistAssistance()));

        MedicalSpecialty specialistType = null;
        if (itemDTO.getSpecialistTypeId() != null && !itemDTO.getSpecialistTypeId().isEmpty()) {
            try {
                specialistType = MedicalSpecialty.valueOf(itemDTO.getSpecialistTypeId().toUpperCase());
            } catch (IllegalArgumentException e) {
                specialistType = MedicalSpecialty.GENERAL;
            }
        }

        return DiagnosticAidItem.of(itemNumber, diagnosticAidId, quantity,
                                   requiresSpecialist, specialistType);
    }

    /**
     * Gets order statistics for a patient.
     */
    public OrderStatisticsDTO getOrderStatistics(String patientCedula) {
        if (patientCedula == null || patientCedula.trim().isEmpty()) {
            return new OrderStatisticsDTO(0, 0, 0, 0);
        }

        try {
            PatientCedula patientCedulaObj = PatientCedula.of(patientCedula.trim());

            // Get orders for the patient
            List<OrderDTO> patientOrders = findOrdersByPatientCedula(patientCedula);

            int totalOrders = patientOrders.size();
            int medicationOrders = 0;
            int procedureOrders = 0;
            int diagnosticAidOrders = 0;

            // Count by order type based on the items in each order
            for (OrderDTO order : patientOrders) {
                if (order.getMedications() != null && !order.getMedications().isEmpty()) {
                    medicationOrders++;
                }
                if (order.getProcedures() != null && !order.getProcedures().isEmpty()) {
                    procedureOrders++;
                }
                if (order.getDiagnosticAids() != null && !order.getDiagnosticAids().isEmpty()) {
                    diagnosticAidOrders++;
                }
            }

            return new OrderStatisticsDTO(totalOrders, medicationOrders, procedureOrders, diagnosticAidOrders);

        } catch (IllegalArgumentException e) {
            // Invalid patient cedula format
            return new OrderStatisticsDTO(0, 0, 0, 0);
        }
    }

    /**
     * Maps a MedicationOrder domain model to OrderDTO.
     */
    private OrderDTO mapMedicationOrderToDTO(MedicationOrder medicationOrder) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(medicationOrder.getOrderNumber().getValue());
        orderDTO.setPatientCedula(medicationOrder.getPatientCedula().getValue());
        orderDTO.setDoctorCedula(medicationOrder.getDoctorCedula().getValue());
        orderDTO.setCreationDate(medicationOrder.getCreationDate().getValue().toString());

        // Map medications
        List<MedicationOrderDTO> medicationDTOs = medicationOrder.getMedications().stream()
            .map(this::mapMedicationItemToDTO)
            .toList();
        orderDTO.setMedications(medicationDTOs);

        return orderDTO;
    }

    /**
     * Maps a ProcedureOrder domain model to OrderDTO.
     */
    private OrderDTO mapProcedureOrderToDTO(ProcedureOrder procedureOrder) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(procedureOrder.getOrderNumber().getValue());
        orderDTO.setPatientCedula(procedureOrder.getPatientCedula().getValue());
        orderDTO.setDoctorCedula(procedureOrder.getDoctorCedula().getValue());
        orderDTO.setCreationDate(procedureOrder.getCreationDate().getValue().toString());

        // Map procedures
        List<ProcedureOrderDTO> procedureDTOs = procedureOrder.getProcedures().stream()
            .map(this::mapProcedureItemToDTO)
            .toList();
        orderDTO.setProcedures(procedureDTOs);

        return orderDTO;
    }

    /**
     * Maps a DiagnosticAidOrder domain model to OrderDTO.
     */
    private OrderDTO mapDiagnosticAidOrderToDTO(DiagnosticAidOrder diagnosticAidOrder) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(diagnosticAidOrder.getOrderNumber().getValue());
        orderDTO.setPatientCedula(diagnosticAidOrder.getPatientCedula().getValue());
        orderDTO.setDoctorCedula(diagnosticAidOrder.getDoctorCedula().getValue());
        orderDTO.setCreationDate(diagnosticAidOrder.getCreationDate().getValue().toString());

        // Map diagnostic aids
        List<DiagnosticAidOrderDTO> diagnosticAidDTOs = diagnosticAidOrder.getDiagnosticAids().stream()
            .map(this::mapDiagnosticAidItemToDTO)
            .toList();
        orderDTO.setDiagnosticAids(diagnosticAidDTOs);

        return orderDTO;
    }

    /**
     * Maps a MedicationItem to MedicationOrderDTO.
     */
    private MedicationOrderDTO mapMedicationItemToDTO(app.clinic.domain.model.MedicationItem medicationItem) {
        // Get inventory item details
        Optional<app.clinic.domain.model.InventoryItem> inventoryItem = inventoryRepository
            .findById(medicationItem.getMedicationId());

        String medicationName = inventoryItem.map(item -> item.getName().getValue()).orElse("Unknown");
        String cost = inventoryItem.map(item -> item.getCost().getValue().toString()).orElse("$0");

        return new MedicationOrderDTO(
            String.valueOf(medicationItem.getItemNumber().getValue()),
            medicationItem.getItemNumber().getValue(),
            medicationName,
            medicationItem.getDosage().getValue(),
            medicationItem.getDuration().getValue(),
            cost
        );
    }

    /**
     * Maps a ProcedureItem to ProcedureOrderDTO.
     */
    private ProcedureOrderDTO mapProcedureItemToDTO(app.clinic.domain.model.ProcedureItem procedureItem) {
        // Get inventory item details
        Optional<app.clinic.domain.model.InventoryItem> inventoryItem = inventoryRepository
            .findById(procedureItem.getProcedureId());

        String procedureName = inventoryItem.map(item -> item.getName().getValue()).orElse("Unknown");
        String cost = inventoryItem.map(item -> item.getCost().getValue().toString()).orElse("$0");

        return new ProcedureOrderDTO(
            String.valueOf(procedureItem.getItemNumber().getValue()),
            procedureItem.getItemNumber().getValue(),
            procedureName,
            procedureItem.getQuantity().getValue(),
            procedureItem.getFrequency().getValue(),
            cost,
            procedureItem.getRequiresSpecialistAssistance().isRequired(),
            procedureItem.getSpecialistType() != null ? procedureItem.getSpecialistType().name() : null
        );
    }

    /**
     * Maps a DiagnosticAidItem to DiagnosticAidOrderDTO.
     */
    private DiagnosticAidOrderDTO mapDiagnosticAidItemToDTO(app.clinic.domain.model.DiagnosticAidItem diagnosticAidItem) {
        // Get inventory item details
        Optional<app.clinic.domain.model.InventoryItem> inventoryItem = inventoryRepository
            .findById(diagnosticAidItem.getDiagnosticAidId());

        String diagnosticAidName = inventoryItem.map(item -> item.getName().getValue()).orElse("Unknown");
        String cost = inventoryItem.map(item -> item.getCost().getValue().toString()).orElse("$0");

        return new DiagnosticAidOrderDTO(
            String.valueOf(diagnosticAidItem.getItemNumber().getValue()),
            diagnosticAidItem.getItemNumber().getValue(),
            diagnosticAidName,
            diagnosticAidItem.getQuantity().getValue(),
            cost,
            diagnosticAidItem.getRequiresSpecialistAssistance().isRequired(),
            diagnosticAidItem.getSpecialistType() != null ? diagnosticAidItem.getSpecialistType().name() : null
        );
    }

    /**
     * DTO for order statistics.
     */
    public static class OrderStatisticsDTO {
        private int totalOrders;
        private int medicationOrders;
        private int procedureOrders;
        private int diagnosticAidOrders;

        // Constructors, getters and setters
        public OrderStatisticsDTO() {}

        public OrderStatisticsDTO(int totalOrders, int medicationOrders, int procedureOrders, int diagnosticAidOrders) {
            this.totalOrders = totalOrders;
            this.medicationOrders = medicationOrders;
            this.procedureOrders = procedureOrders;
            this.diagnosticAidOrders = diagnosticAidOrders;
        }

        // Getters and setters
        public int getTotalOrders() { return totalOrders; }
        public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
        public int getMedicationOrders() { return medicationOrders; }
        public void setMedicationOrders(int medicationOrders) { this.medicationOrders = medicationOrders; }
        public int getProcedureOrders() { return procedureOrders; }
        public void setProcedureOrders(int procedureOrders) { this.procedureOrders = procedureOrders; }
        public int getDiagnosticAidOrders() { return diagnosticAidOrders; }
        public void setDiagnosticAidOrders(int diagnosticAidOrders) { this.diagnosticAidOrders = diagnosticAidOrders; }
    }
}