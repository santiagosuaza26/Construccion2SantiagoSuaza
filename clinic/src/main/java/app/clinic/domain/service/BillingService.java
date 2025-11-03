package app.clinic.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.repository.BillingRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;

public class BillingService {
    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public BillingService(BillingRepository billingRepository, PatientRepository patientRepository, OrderRepository orderRepository, UserRepository userRepository, RoleBasedAccessService roleBasedAccessService) {
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public Billing generateBilling(String patientId, String doctorName, String orderNumber, double totalCost, String appliedMedications, String appliedProcedures, String appliedDiagnosticAids, String adminId) {
        validateAdminRole(adminId);

        Patient patient = patientRepository.findByIdentificationNumber(new Id(patientId))
            .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        BillingCalculationResult calculation = calculateBillingDetails(patient, totalCost);

        // Update patient's annual copay only if they actually pay
        if (calculation.hasActiveInsurance && calculation.copay > 0) {
            patient.addToAnnualCopayTotal(calculation.copay);
            patientRepository.save(patient);
        }

        Billing billing = createBillingObject(orderNumber, patient, doctorName, totalCost, calculation,
                                             appliedMedications, appliedProcedures, appliedDiagnosticAids, adminId);

        billingRepository.save(billing);
        return billing;
    }

    public Billing generateBillingFromOrder(String orderNumber, String adminId) {
        validateAdminRole(adminId);

        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        User doctor = userRepository.findByIdentificationNumber(new Id(order.getDoctorIdentificationNumber()))
            .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        // Use domain logic from Billing entity
        double totalCost = Billing.calculateTotalCostFromOrder(order);
        String appliedMedications = Billing.formatAppliedMedications(order);
        String appliedProcedures = Billing.formatAppliedProcedures(order);
        String appliedDiagnosticAids = Billing.formatAppliedDiagnosticAids(order);

        return generateBilling(order.getPatientIdentificationNumber(), doctor.getFullName(), orderNumber,
                              totalCost, appliedMedications, appliedProcedures, appliedDiagnosticAids, adminId);
    }

    // Método adicional para generar facturación con lógica específica según póliza activa/inactiva
    public Billing generateBillingWithInsuranceLogic(String patientId, String doctorName, String orderNumber, double totalCost, String appliedMedications, String appliedProcedures, String appliedDiagnosticAids, String adminId) {
        validateAdminRole(adminId);

        Patient patient = patientRepository.findByIdentificationNumber(new Id(patientId))
            .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        BillingCalculationResult calculation = calculateBillingDetails(patient, totalCost);

        // Actualizar copago anual si aplica
        if (calculation.hasActiveInsurance && calculation.copay > 0) {
            patient.addToAnnualCopayTotal(calculation.copay);
            patientRepository.save(patient);
        }

        Billing billing = createBillingObject(orderNumber, patient, doctorName, totalCost, calculation,
                                             appliedMedications, appliedProcedures, appliedDiagnosticAids, adminId);

        billingRepository.save(billing);
        return billing;
    }


    private void validateAdminRole(String adminId) {
        Id adminIdObj = new Id(adminId);
        User admin = userRepository.findByIdentificationNumber(adminIdObj).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        roleBasedAccessService.checkAccess(admin.getRole(), "billing");

        // Validación específica: Solo Personal Administrativo puede generar facturas
        if (admin.getRole() != Role.PERSONAL_ADMINISTRATIVO) {
            throw new IllegalAccessError("Only Administrative staff can generate billing");
        }
    }

    private static class BillingCalculationResult {
        final int age;
        final int validityDays;
        final boolean hasActiveInsurance;
        final double copay;
        final double insuranceCoverage;

        BillingCalculationResult(int age, int validityDays, boolean hasActiveInsurance, double copay, double insuranceCoverage) {
            this.age = age;
            this.validityDays = validityDays;
            this.hasActiveInsurance = hasActiveInsurance;
            this.copay = copay;
            this.insuranceCoverage = insuranceCoverage;
        }
    }

    private BillingCalculationResult calculateBillingDetails(Patient patient, double totalCost) {
        int age = Period.between(patient.getDateOfBirth().getValue(), LocalDate.now()).getYears();
        int validityDays = patient.getInsurance() != null ? Period.between(LocalDate.now(), patient.getInsurance().getValidityDate()).getDays() : 0;

        boolean hasActiveInsurance = patient.getInsurance() != null &&
                                   patient.getInsurance().isActive() &&
                                   patient.getInsurance().getValidityDate() != null &&
                                   patient.getInsurance().getValidityDate().isAfter(LocalDate.now());

        double copay = Billing.calculateCopay(totalCost, hasActiveInsurance, patient.getAnnualCopayTotal());
        double insuranceCoverage = hasActiveInsurance ? totalCost - copay : 0.0;

        return new BillingCalculationResult(age, validityDays, hasActiveInsurance, copay, insuranceCoverage);
    }

    private Billing createBillingObject(String orderNumber, Patient patient, String doctorName, double totalCost,
                                       BillingCalculationResult calculation, String appliedMedications,
                                       String appliedProcedures, String appliedDiagnosticAids, String adminId) {
        return new Billing(
            new OrderNumber(orderNumber),
            patient.getFullName(),
            calculation.age,
            patient.getIdentificationNumber().getValue(),
            doctorName,
            patient.getInsurance() != null ? patient.getInsurance().getCompanyName() : "",
            patient.getInsurance() != null ? patient.getInsurance().getPolicyNumber() : "",
            calculation.validityDays,
            patient.getInsurance() != null ? patient.getInsurance().getValidityDate() : null,
            totalCost,
            calculation.copay,
            calculation.insuranceCoverage,
            appliedMedications,
            appliedProcedures,
            appliedDiagnosticAids,
            LocalDateTime.now(),
            adminId
        );
    }

    public List<Billing> findBillingByPatientId(String patientId) {
        return billingRepository.findByPatientIdentificationNumber(patientId);
    }
}