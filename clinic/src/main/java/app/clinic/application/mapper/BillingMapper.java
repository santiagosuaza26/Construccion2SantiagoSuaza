package app.clinic.application.mapper;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.application.dto.billing.BillingCalculationResultDTO;
import app.clinic.application.dto.billing.BillingDTO;
import app.clinic.application.dto.billing.InvoiceDTO;
import app.clinic.application.service.PatientApplicationService;
import app.clinic.infrastructure.entity.BillingDetailsEntity;
import app.clinic.infrastructure.entity.BillingSummaryEntity;
import app.clinic.infrastructure.entity.InvoiceEntity;

/**
 * Mapper service for converting between domain entities and DTOs.
 * Handles conversions for billing-related objects.
 */
@Component
public class BillingMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final PatientApplicationService patientApplicationService;

    public BillingMapper(PatientApplicationService patientApplicationService) {
        this.patientApplicationService = patientApplicationService;
    }

    /**
     * Converts InvoiceEntity to InvoiceDTO.
     */
    public InvoiceDTO toInvoiceDTO(InvoiceEntity entity) {
        if (entity == null) {
            return null;
        }

        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setPatientCedula(entity.getPatientCedula());

        // Get real patient name using PatientApplicationService
        String patientName = patientApplicationService.findPatientByCedula(entity.getPatientCedula())
                .map(patient -> patient.getFullName())
                .orElse("Paciente no encontrado");
        dto.setPatientName(patientName);

        dto.setTotalAmount(entity.getTotalAmount() != null ? entity.getTotalAmount().toString() : "0");
        dto.setCopaymentAmount(entity.getCopaymentAmount() != null ? entity.getCopaymentAmount().toString() : "0");
        dto.setInsuranceCoverage(entity.getInsuranceCoverage() != null ? entity.getInsuranceCoverage().toString() : "0");
        dto.setPatientResponsibility(entity.getPatientResponsibility() != null ? entity.getPatientResponsibility().toString() : "0");
        dto.setBillingDate(entity.getBillingDate() != null ? entity.getBillingDate().format(DATE_FORMATTER) : null);
        dto.setDueDate(entity.getDueDate() != null ? entity.getDueDate().format(DATE_FORMATTER) : null);
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().toString() : "PENDING");
        dto.setNotes(entity.getNotes());
        dto.setYear(entity.getYear());

        return dto;
    }

    /**
     * Converts a list of InvoiceEntity to a list of InvoiceDTO.
     */
    public List<InvoiceDTO> toInvoiceDTOList(List<InvoiceEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                      .map(this::toInvoiceDTO)
                      .collect(Collectors.toList());
    }

    /**
     * Converts BillingSummaryEntity to BillingDTO.
     */
    public BillingDTO toBillingDTO(BillingSummaryEntity entity) {
        if (entity == null) {
            return null;
        }

        BillingDTO dto = new BillingDTO();
        dto.setPatientCedula(entity.getPatientCedula());

        // Get real patient name using PatientApplicationService
        String patientName = patientApplicationService.findPatientByCedula(entity.getPatientCedula())
                .map(patient -> patient.getFullName())
                .orElse("Paciente no encontrado");
        dto.setPatientName(patientName);

        dto.setTotalAmount(entity.getTotalBilled() != null ? entity.getTotalBilled().toString() : "0");
        dto.setCopaymentAmount(entity.getTotalCopayment() != null ? entity.getTotalCopayment().toString() : "0");
        dto.setInsuranceCoverageAmount(entity.getTotalInsuranceCoverage() != null ? entity.getTotalInsuranceCoverage().toString() : "0");

        return dto;
    }

    /**
     * Converts BillingDetailsEntity to a detailed InvoiceDTO.
     */
    public InvoiceDTO toDetailedInvoiceDTO(BillingDetailsEntity entity) {
        if (entity == null) {
            return null;
        }

        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setPatientCedula(entity.getPatientCedula());

        // Get real patient name using PatientApplicationService
        String patientName = patientApplicationService.findPatientByCedula(entity.getPatientCedula())
                .map(patient -> patient.getFullName())
                .orElse("Paciente no encontrado");
        dto.setPatientName(patientName);

        dto.setTotalAmount(calculateTotalAmount(entity).toString());
        dto.setOrderSummaries(entity.getOrderSummaries());

        return dto;
    }

    /**
     * Creates a BillingCalculationResultDTO from calculation values.
     */
    public BillingCalculationResultDTO toBillingCalculationResultDTO(BigDecimal totalCost,
                                                                   BigDecimal copaymentAmount,
                                                                   BigDecimal insuranceCoverage,
                                                                   boolean requiresCopayment,
                                                                   boolean copaymentLimitExceeded) {
        BillingCalculationResultDTO dto = new BillingCalculationResultDTO();
        dto.setTotalCost(totalCost.toString());
        dto.setCopaymentAmount(copaymentAmount.toString());
        dto.setInsuranceCoverageAmount(insuranceCoverage.toString());
        dto.setRequiresCopayment(requiresCopayment);
        dto.setCopaymentLimitExceeded(copaymentLimitExceeded);

        // Set appropriate message based on copayment limit status
        if (copaymentLimitExceeded) {
            dto.setCopaymentLimitMessage("Paciente ha excedido el límite anual de copagos ($1,000,000). La aseguradora cubrirá el total.");
        } else {
            dto.setCopaymentLimitMessage("Paciente debe pagar copago de $" + copaymentAmount + ". El resto lo cubre la aseguradora.");
        }

        return dto;
    }

    /**
     * Calculates total amount from billing details entity.
     */
    private BigDecimal calculateTotalAmount(BillingDetailsEntity entity) {
        BigDecimal total = BigDecimal.ZERO;

        if (entity.getTreatmentCost() != null) {
            total = total.add(entity.getTreatmentCost());
        }
        if (entity.getMedicationCost() != null) {
            total = total.add(entity.getMedicationCost());
        }
        if (entity.getProcedureCost() != null) {
            total = total.add(entity.getProcedureCost());
        }
        if (entity.getDiagnosticAidCost() != null) {
            total = total.add(entity.getDiagnosticAidCost());
        }

        return total;
    }
}