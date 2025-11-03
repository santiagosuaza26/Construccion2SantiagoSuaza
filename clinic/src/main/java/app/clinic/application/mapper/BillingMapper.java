package app.clinic.application.mapper;

import app.clinic.domain.model.entities.Billing;
import app.clinic.infrastructure.dto.BillingDTO;

public class BillingMapper {

    public static BillingDTO toDTO(Billing billing) {
        BillingDTO dto = new BillingDTO();
        dto.setId(billing.getOrderNumber().getValue());
        dto.setPatientId(billing.getIdentificationNumber());
        dto.setPatientName(billing.getPatientName());
        dto.setDoctorName(billing.getDoctorName());
        dto.setOrderNumber(billing.getOrderNumber().getValue());
        dto.setTotalCost(billing.getTotalCost());
        dto.setCopayAmount(billing.getCopay());
        dto.setInsuranceCoverage(billing.getInsuranceCoverage());
        dto.setFinalAmount(billing.getTotalCost() - billing.getCopay() - billing.getInsuranceCoverage());
        dto.setAppliedMedications(billing.getAppliedMedications());
        dto.setAppliedProcedures(billing.getAppliedProcedures());
        dto.setAppliedDiagnosticAids(billing.getAppliedDiagnosticAids());
        dto.setGeneratedAt(billing.getGeneratedAt());
        dto.setGeneratedBy(billing.getGeneratedBy());
        return dto;
    }
}