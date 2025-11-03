package app.clinic.domain.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import app.clinic.domain.model.valueobject.OrderNumber;

public class Billing {
    private final OrderNumber orderNumber;
    private final String patientName;
    private final int age;
    private final String identificationNumber;
    private final String doctorName;
    private final String company;
    private final String policyNumber;
    private final int validityDays;
    private final LocalDate validityDate;
    private final double totalCost;
    private final double copay;
    private final double insuranceCoverage;
    private final String appliedMedications;
    private final String appliedProcedures;
    private final String appliedDiagnosticAids;
    private final LocalDateTime generatedAt;
    private final String generatedBy;

    // Business logic methods
    public static Billing create(OrderNumber orderNumber, String patientName, int age, String identificationNumber,
                                String doctorName, String company, String policyNumber, int validityDays,
                                LocalDate validityDate, double totalCost, String appliedMedications,
                                String appliedProcedures, String appliedDiagnosticAids, String generatedBy) {

        // Calculate copay and insurance coverage based on business rules
        double copay = 0.0;
        double insuranceCoverage = 0.0;

        if (company != null && !company.trim().isEmpty() && validityDate != null && validityDate.isAfter(LocalDate.now())) {
            // Active insurance: $50,000 copay
            copay = 50000.0;
            insuranceCoverage = totalCost - copay;
        } else {
            // No active insurance: patient pays full amount
            copay = totalCost;
        }

        return new Billing(orderNumber, patientName, age, identificationNumber, doctorName, company,
                          policyNumber, validityDays, validityDate, totalCost, copay, insuranceCoverage,
                          appliedMedications, appliedProcedures, appliedDiagnosticAids,
                          LocalDateTime.now(), generatedBy);
    }

    // Method to calculate copay considering annual limit
    public static double calculateCopay(double totalCost, boolean hasActiveInsurance, double currentAnnualCopayTotal) {
        if (!hasActiveInsurance) {
            return totalCost;
        }

        double copay = 50000.0;

        // Check if adding this copay would exceed annual limit of $1,000,000
        if (currentAnnualCopayTotal + copay > 1000000.0) {
            // If annual copay exceeds $1,000,000, patient pays nothing for this billing
            return 0.0;
        }

        return copay;
    }

    public static double calculateTotalCostFromOrder(Order order) {
        return order.getMedications().stream().mapToDouble(MedicationOrder::getCost).sum() +
               order.getProcedures().stream().mapToDouble(ProcedureOrder::getCost).sum() +
               order.getDiagnosticAids().stream().mapToDouble(DiagnosticAidOrder::getCost).sum();
    }

    public static String formatAppliedMedications(Order order) {
        StringBuilder sb = new StringBuilder();
        for (MedicationOrder med : order.getMedications()) {
            sb.append(med.getMedicationName()).append(" - ").append(med.getDosage())
              .append(" - ").append(med.getDuration()).append("; ");
        }
        return sb.toString();
    }

    public static String formatAppliedProcedures(Order order) {
        StringBuilder sb = new StringBuilder();
        for (ProcedureOrder proc : order.getProcedures()) {
            sb.append(proc.getProcedureName()).append(" - ").append(proc.getQuantity())
              .append(" - ").append(proc.getFrequency()).append("; ");
        }
        return sb.toString();
    }

    public static String formatAppliedDiagnosticAids(Order order) {
        StringBuilder sb = new StringBuilder();
        for (DiagnosticAidOrder aid : order.getDiagnosticAids()) {
            sb.append(aid.getDiagnosticAidName()).append(" - ").append(aid.getQuantity()).append("; ");
        }
        return sb.toString();
    }

    public Billing(OrderNumber orderNumber, String patientName, int age, String identificationNumber, String doctorName, String company, String policyNumber, int validityDays, LocalDate validityDate, double totalCost, double copay, double insuranceCoverage, String appliedMedications, String appliedProcedures, String appliedDiagnosticAids, LocalDateTime generatedAt, String generatedBy) {
        this.orderNumber = orderNumber;
        this.patientName = patientName;
        this.age = age;
        this.identificationNumber = identificationNumber;
        this.doctorName = doctorName;
        this.company = company;
        this.policyNumber = policyNumber;
        this.validityDays = validityDays;
        this.validityDate = validityDate;
        this.totalCost = totalCost;
        this.copay = copay;
        this.insuranceCoverage = insuranceCoverage;
        this.appliedMedications = appliedMedications;
        this.appliedProcedures = appliedProcedures;
        this.appliedDiagnosticAids = appliedDiagnosticAids;
        this.generatedAt = generatedAt;
        this.generatedBy = generatedBy;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getAge() {
        return age;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getCompany() {
        return company;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getCopay() {
        return copay;
    }

    public double getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public String getAppliedMedications() {
        return appliedMedications;
    }

    public String getAppliedProcedures() {
        return appliedProcedures;
    }

    public String getAppliedDiagnosticAids() {
        return appliedDiagnosticAids;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Billing billing = (Billing) o;
        return orderNumber.equals(billing.orderNumber);
    }

    @Override
    public int hashCode() {
        return orderNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Billing for order " + orderNumber;
    }
}