package app.domain.services;

import java.time.LocalDate;

import app.domain.exception.DomainValidationException;
import app.domain.model.InsurancePolicy;

/**
 * Servicio especializado para validaciones de pólizas de seguro
 * Sigue principio SRP - responsabilidad única: validar pólizas de seguro
 */
public class InsurancePolicyValidationService {

    /**
     * Valida póliza de seguro completa
     */
    public void validateInsurancePolicy(InsurancePolicy insurancePolicy) {
        if (insurancePolicy == null) {
            throw new DomainValidationException("Insurance policy cannot be null");
        }

        validateEndDate(insurancePolicy.getEndDate());
        validateCompany(insurancePolicy.getCompany());
        validatePolicyNumber(insurancePolicy.getPolicyNumber());
        validatePolicyStatus(insurancePolicy);
    }

    /**
     * Valida compañía de seguros
     */
    private void validateCompany(String company) {
        if (company == null || company.trim().isEmpty()) {
            throw new DomainValidationException("Insurance company is required");
        }

        if (company.length() > 100) {
            throw new DomainValidationException("Insurance company name cannot exceed 100 characters");
        }

        // Validar caracteres permitidos (solo letras, números y espacios)
        if (!company.matches("^[A-Za-z0-9\\s]+$")) {
            throw new DomainValidationException("Insurance company name can only contain letters, numbers and spaces");
        }
    }

    /**
     * Valida número de póliza
     */
    private void validatePolicyNumber(String policyNumber) {
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new DomainValidationException("Policy number is required");
        }

        if (policyNumber.length() > 50) {
            throw new DomainValidationException("Policy number cannot exceed 50 characters");
        }

        // Validar formato básico (letras, números y algunos caracteres especiales)
        if (!policyNumber.matches("^[A-Za-z0-9/-]+$")) {
            throw new DomainValidationException("Policy number can only contain letters, numbers, hyphens and forward slashes");
        }
    }

    /**
     * Valida fecha de vencimiento
     */
    private void validateEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new DomainValidationException("Policy end date is required");
        }

        if (endDate.isBefore(LocalDate.now())) {
            throw new DomainValidationException("Policy end date cannot be in the past");
        }

        // Validar que la póliza no expire en más de 10 años
        if (endDate.isAfter(LocalDate.now().plusYears(10))) {
            throw new DomainValidationException("Policy end date cannot be more than 10 years in the future");
        }
    }

    /**
     * Valida estado de la póliza
     */
    private void validatePolicyStatus(InsurancePolicy insurancePolicy) {
        // Si la póliza está marcada como activa, debe tener fecha de vencimiento futura
        if (insurancePolicy.isActive() && insurancePolicy.getEndDate() != null) {
            if (insurancePolicy.getEndDate().isBefore(LocalDate.now())) {
                throw new DomainValidationException("Active policy cannot have end date in the past");
            }
        }

        // Si la póliza está marcada como inactiva, debe tener fecha de vencimiento pasada
        if (!insurancePolicy.isActive() && insurancePolicy.getEndDate() != null) {
            if (insurancePolicy.getEndDate().isAfter(LocalDate.now())) {
                throw new DomainValidationException("Inactive policy cannot have end date in the future");
            }
        }
    }

    /**
     * Valida que la póliza esté activa
     */
    public void validatePolicyIsActive(InsurancePolicy insurancePolicy) {
        if (insurancePolicy == null) {
            throw new DomainValidationException("Insurance policy is required");
        }

        if (!insurancePolicy.isActive()) {
            throw new DomainValidationException("Insurance policy must be active");
        }

        if (insurancePolicy.getEndDate() != null && insurancePolicy.getEndDate().isBefore(LocalDate.now())) {
            throw new DomainValidationException("Insurance policy has expired");
        }
    }

    /**
     * Valida cobertura mínima requerida
     */
    public void validateMinimumCoverage(InsurancePolicy insurancePolicy) {
        validateInsurancePolicy(insurancePolicy);

        // Validar que la póliza tenga cobertura para servicios básicos
        if (insurancePolicy.getCompany() != null) {
            String company = insurancePolicy.getCompany().toLowerCase();
            if (company.contains("básica") || company.contains("mínima")) {
                throw new DomainValidationException("Insurance policy must provide comprehensive coverage");
            }
        }
    }

    /**
     * Valida renovación de póliza
     */
    public void validatePolicyRenewal(InsurancePolicy currentPolicy, InsurancePolicy renewedPolicy) {
        if (currentPolicy == null || renewedPolicy == null) {
            throw new DomainValidationException("Both current and renewed policies are required");
        }

        if (!currentPolicy.getCompany().equals(renewedPolicy.getCompany())) {
            throw new DomainValidationException("Cannot change insurance company during renewal");
        }

        if (currentPolicy.getEndDate() != null && renewedPolicy.getEndDate() != null) {
            if (!renewedPolicy.getEndDate().isAfter(currentPolicy.getEndDate())) {
                throw new DomainValidationException("Renewed policy must have end date after current policy");
            }
        }
    }
}