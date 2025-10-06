package app.clinic.application.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

/**
 * Mapper class for converting between Order DTOs and domain entities.
 */
public class OrderMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converts a CreateOrderDTO to DiagnosticAidOrder domain entity.
     */
    public static DiagnosticAidOrder toDiagnosticAidOrderDomainEntity(CreateOrderDTO createOrderDTO) {
        String orderNumber = createOrderDTO.getOrderNumber();

        List<DiagnosticAidItem> diagnosticAids = createOrderDTO.getItems().stream()
                .filter(item -> "DIAGNOSTIC_AID".equals(item.getType()))
                .map(OrderMapper::convertToDiagnosticAidItem)
                .collect(Collectors.toList());

        return DiagnosticAidOrder.of(
            OrderNumber.of(orderNumber),
            PatientCedula.of(createOrderDTO.getPatientCedula()),
            DoctorCedula.of(createOrderDTO.getDoctorCedula()),
            OrderCreationDate.of(LocalDateTime.now()),
            OrderStatus.CREADA,
            diagnosticAids
        );
    }

    /**
     * Converts a CreateOrderDTO to MedicationOrder domain entity.
     */
    public static MedicationOrder toMedicationOrderDomainEntity(CreateOrderDTO createOrderDTO) {
        String orderNumber = createOrderDTO.getOrderNumber();

        List<MedicationItem> medications = createOrderDTO.getItems().stream()
                .filter(item -> "MEDICATION".equals(item.getType()))
                .map(OrderMapper::convertToMedicationItem)
                .collect(Collectors.toList());

        return MedicationOrder.of(
            OrderNumber.of(orderNumber),
            PatientCedula.of(createOrderDTO.getPatientCedula()),
            DoctorCedula.of(createOrderDTO.getDoctorCedula()),
            OrderCreationDate.of(LocalDateTime.now()),
            OrderStatus.CREADA,
            medications
        );
    }

    /**
     * Converts a CreateOrderDTO to ProcedureOrder domain entity.
     */
    public static ProcedureOrder toProcedureOrderDomainEntity(CreateOrderDTO createOrderDTO) {
        String orderNumber = createOrderDTO.getOrderNumber();

        List<ProcedureItem> procedures = createOrderDTO.getItems().stream()
                .filter(item -> "PROCEDURE".equals(item.getType()))
                .map(OrderMapper::convertToProcedureItem)
                .collect(Collectors.toList());

        return ProcedureOrder.of(
            OrderNumber.of(orderNumber),
            PatientCedula.of(createOrderDTO.getPatientCedula()),
            DoctorCedula.of(createOrderDTO.getDoctorCedula()),
            OrderCreationDate.of(LocalDateTime.now()),
            OrderStatus.CREADA,
            procedures
        );
    }

    /**
     * Converts a DiagnosticAidOrder domain entity to DTO.
     */
    public static DiagnosticAidOrderDTO toDiagnosticAidOrderDTO(DiagnosticAidOrder diagnosticAidOrder) {
        DiagnosticAidOrderDTO dto = new DiagnosticAidOrderDTO();
        dto.setOrderNumber(diagnosticAidOrder.getOrderNumber().getValue());
        dto.setDiagnosticAidName(""); // Not available in domain entity
        dto.setQuantity(0); // Not available in domain entity
        dto.setCost("0"); // Not available in domain entity
        dto.setRequiresSpecialistAssistance(false); // Not available in domain entity
        dto.setSpecialistTypeId(""); // Not available in domain entity
        return dto;
    }

    /**
     * Converts a MedicationOrder domain entity to DTO.
     */
    public static MedicationOrderDTO toMedicationOrderDTO(MedicationOrder medicationOrder) {
        MedicationOrderDTO dto = new MedicationOrderDTO();
        dto.setOrderNumber(medicationOrder.getOrderNumber().getValue());
        dto.setMedicationName(""); // Not available in domain entity
        dto.setDosage(""); // Not available in domain entity
        dto.setTreatmentDuration(""); // Not available in domain entity
        dto.setCost("0"); // Not available in domain entity
        return dto;
    }

    /**
     * Converts a ProcedureOrder domain entity to DTO.
     */
    public static ProcedureOrderDTO toProcedureOrderDTO(ProcedureOrder procedureOrder) {
        ProcedureOrderDTO dto = new ProcedureOrderDTO();
        dto.setOrderNumber(procedureOrder.getOrderNumber().getValue());
        dto.setProcedureName(""); // Not available in domain entity
        dto.setNumberOfTimes(0); // Not available in domain entity
        dto.setFrequency(""); // Not available in domain entity
        dto.setCost("0"); // Not available in domain entity
        dto.setRequiresSpecialistAssistance(false); // Not available in domain entity
        dto.setSpecialistTypeId(""); // Not available in domain entity
        return dto;
    }

    /**
     * Converts a DiagnosticAidOrder domain entity to OrderDTO.
     */
    public static OrderDTO toOrderDTOFromDiagnosticAidOrder(DiagnosticAidOrder diagnosticAidOrder) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderNumber(diagnosticAidOrder.getOrderNumber().getValue());
        dto.setPatientCedula(diagnosticAidOrder.getPatientCedula().getValue());
        dto.setDoctorCedula(diagnosticAidOrder.getDoctorCedula().getValue());
        dto.setCreationDate(diagnosticAidOrder.getCreationDate().getValue().format(DATE_TIME_FORMATTER));

        List<DiagnosticAidOrderDTO> diagnosticAids = diagnosticAidOrder.getDiagnosticAids().stream()
                .map(OrderMapper::convertFromDiagnosticAidItemToDTO)
                .collect(Collectors.toList());

        dto.setDiagnosticAids(diagnosticAids);
        dto.setMedications(List.of()); // Empty for diagnostic aid orders
        dto.setProcedures(List.of()); // Empty for diagnostic aid orders
        return dto;
    }

    /**
     * Converts a MedicationOrder domain entity to OrderDTO.
     */
    public static OrderDTO toOrderDTOFromMedicationOrder(MedicationOrder medicationOrder) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderNumber(medicationOrder.getOrderNumber().getValue());
        dto.setPatientCedula(medicationOrder.getPatientCedula().getValue());
        dto.setDoctorCedula(medicationOrder.getDoctorCedula().getValue());
        dto.setCreationDate(medicationOrder.getCreationDate().getValue().format(DATE_TIME_FORMATTER));

        List<MedicationOrderDTO> medications = medicationOrder.getMedications().stream()
                .map(OrderMapper::convertFromMedicationItemToDTO)
                .collect(Collectors.toList());

        dto.setMedications(medications);
        dto.setDiagnosticAids(List.of()); // Empty for medication orders
        dto.setProcedures(List.of()); // Empty for medication orders
        return dto;
    }

    /**
     * Converts a ProcedureOrder domain entity to OrderDTO.
     */
    public static OrderDTO toOrderDTOFromProcedureOrder(ProcedureOrder procedureOrder) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderNumber(procedureOrder.getOrderNumber().getValue());
        dto.setPatientCedula(procedureOrder.getPatientCedula().getValue());
        dto.setDoctorCedula(procedureOrder.getDoctorCedula().getValue());
        dto.setCreationDate(procedureOrder.getCreationDate().getValue().format(DATE_TIME_FORMATTER));

        List<ProcedureOrderDTO> procedures = procedureOrder.getProcedures().stream()
                .map(OrderMapper::convertFromProcedureItemToDTO)
                .collect(Collectors.toList());

        dto.setProcedures(procedures);
        dto.setDiagnosticAids(List.of()); // Empty for procedure orders
        dto.setMedications(List.of()); // Empty for procedure orders
        return dto;
    }

    /**
     * Converts a list of DiagnosticAidOrder entities to DTOs.
     */
    public static List<DiagnosticAidOrderDTO> toDiagnosticAidOrderDTOList(List<DiagnosticAidOrder> diagnosticAidOrders) {
        return diagnosticAidOrders.stream()
                .map(OrderMapper::toDiagnosticAidOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of MedicationOrder entities to DTOs.
     */
    public static List<MedicationOrderDTO> toMedicationOrderDTOList(List<MedicationOrder> medicationOrders) {
        return medicationOrders.stream()
                .map(OrderMapper::toMedicationOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of ProcedureOrder entities to DTOs.
     */
    public static List<ProcedureOrderDTO> toProcedureOrderDTOList(List<ProcedureOrder> procedureOrders) {
        return procedureOrders.stream()
                .map(OrderMapper::toProcedureOrderDTO)
                .collect(Collectors.toList());
    }

    // Private helper methods for conversion

    private static DiagnosticAidItem convertToDiagnosticAidItem(OrderItemDTO itemDTO) {
        String itemId = UUID.randomUUID().toString();
        return DiagnosticAidItem.of(
            ItemNumber.of(1), // Default item number
            InventoryItemId.of(itemDTO.getInventoryItemId()),
            Quantity.of(itemDTO.getQuantity()),
            RequiresSpecialistAssistance.of(itemDTO.getRequiresSpecialistAssistance()),
            itemDTO.getSpecialistTypeId() != null ? MedicalSpecialty.valueOf(itemDTO.getSpecialistTypeId()) : MedicalSpecialty.GENERAL
        );
    }

    private static MedicationItem convertToMedicationItem(OrderItemDTO itemDTO) {
        String itemId = UUID.randomUUID().toString();
        return MedicationItem.of(
            ItemNumber.of(1), // Default item number
            InventoryItemId.of(itemDTO.getInventoryItemId()),
            itemDTO.getDosage() != null ? Dosage.of(itemDTO.getDosage()) : Dosage.of(""),
            itemDTO.getTreatmentDuration() != null ? TreatmentDuration.of(itemDTO.getTreatmentDuration()) : TreatmentDuration.of("")
        );
    }

    private static ProcedureItem convertToProcedureItem(OrderItemDTO itemDTO) {
        String itemId = UUID.randomUUID().toString();
        return ProcedureItem.of(
            ItemNumber.of(1), // Default item number
            InventoryItemId.of(itemDTO.getInventoryItemId()),
            Quantity.of(itemDTO.getQuantity()),
            itemDTO.getFrequency() != null ? Frequency.of(itemDTO.getFrequency()) : Frequency.of(""),
            RequiresSpecialistAssistance.of(itemDTO.getRequiresSpecialistAssistance()),
            itemDTO.getSpecialistTypeId() != null ? MedicalSpecialty.valueOf(itemDTO.getSpecialistTypeId()) : MedicalSpecialty.GENERAL
        );
    }

    private static DiagnosticAidOrderDTO convertFromDiagnosticAidItemToDTO(DiagnosticAidItem item) {
        DiagnosticAidOrderDTO dto = new DiagnosticAidOrderDTO();
        dto.setOrderNumber(""); // Not available in item
        dto.setItemNumber(item.getItemNumber().getValue());
        dto.setDiagnosticAidName(""); // Not available in item
        dto.setQuantity(item.getQuantity().getValue());
        dto.setCost("0"); // Not available in item
        dto.setRequiresSpecialistAssistance(item.getRequiresSpecialistAssistance().isRequired());
        dto.setSpecialistTypeId(item.getSpecialistType() != null ? item.getSpecialistType().name() : "");
        return dto;
    }

    private static MedicationOrderDTO convertFromMedicationItemToDTO(MedicationItem item) {
        MedicationOrderDTO dto = new MedicationOrderDTO();
        dto.setOrderNumber(""); // Not available in item
        dto.setItemNumber(item.getItemNumber().getValue());
        dto.setMedicationName(""); // Not available in item
        dto.setDosage(item.getDosage().getValue());
        dto.setTreatmentDuration(item.getDuration().getValue());
        dto.setCost("0"); // Not available in item
        return dto;
    }

    private static ProcedureOrderDTO convertFromProcedureItemToDTO(ProcedureItem item) {
        ProcedureOrderDTO dto = new ProcedureOrderDTO();
        dto.setOrderNumber(""); // Not available in item
        dto.setItemNumber(item.getItemNumber().getValue());
        dto.setProcedureName(""); // Not available in item
        dto.setNumberOfTimes(item.getQuantity().getValue());
        dto.setFrequency(item.getFrequency().getValue());
        dto.setCost("0"); // Not available in item
        dto.setRequiresSpecialistAssistance(item.getRequiresSpecialistAssistance().isRequired());
        dto.setSpecialistTypeId(item.getSpecialistType() != null ? item.getSpecialistType().name() : "");
        return dto;
    }
}