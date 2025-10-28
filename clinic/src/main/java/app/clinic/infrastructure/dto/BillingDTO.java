package app.clinic.infrastructure.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingDTO {
    private String id;
    private String patientId;
    private String patientName;
    private String doctorName;
    private String orderNumber;
    private double totalCost;
    private double copayAmount;
    private double insuranceCoverage;
    private double finalAmount;
    private String appliedMedications;
    private String appliedProcedures;
    private String appliedDiagnosticAids;
    private LocalDateTime generatedAt;
    private String generatedBy;
}