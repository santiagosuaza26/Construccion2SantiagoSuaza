package app.clinic.domain.service;

import java.time.LocalDate;
import java.time.Period;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.OrderNumber;
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

    public Billing generateBilling(String patientId, String doctorName, String orderNumber, double totalCost, String appliedMedications, String appliedProcedures, String appliedDiagnosticAids) {
        Patient patient = patientRepository.findByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(patientId)).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        double copay = 0.0;
        double insuranceCoverage = 0.0;

        if (patient.getInsurance() != null && patient.getInsurance().isActive()) {
            LocalDate validity = patient.getInsurance().getValidityDate();
            if (validity.isAfter(LocalDate.now())) {
                copay = 50000.0; // $50,000 copago
                insuranceCoverage = totalCost - copay;
                patient.addToAnnualCopayTotal(copay);
                if (patient.getAnnualCopayTotal() > 1000000.0) { // $1,000,000
                    copay = 0.0;
                    insuranceCoverage = totalCost;
                }
            } else {
                copay = totalCost; // Póliza inactiva
            }
        } else {
            copay = totalCost; // Sin póliza
        }

        int age = Period.between(patient.getDateOfBirth().getValue(), LocalDate.now()).getYears();
        int validityDays = patient.getInsurance() != null ? Period.between(LocalDate.now(), patient.getInsurance().getValidityDate()).getDays() : 0;

        Billing billing = new Billing(new OrderNumber(orderNumber), patient.getFullName(), age, patientId, doctorName, patient.getInsurance() != null ? patient.getInsurance().getCompanyName() : "", patient.getInsurance() != null ? patient.getInsurance().getPolicyNumber() : "", validityDays, patient.getInsurance() != null ? patient.getInsurance().getValidityDate() : null, totalCost, copay, insuranceCoverage, appliedMedications, appliedProcedures, appliedDiagnosticAids);
        billingRepository.save(billing);
        patientRepository.save(patient); // Update annual copay
        return billing;
    }

    public Billing generateBillingFromOrder(String orderNumber, String adminId) {
        validateAdminRole(adminId);
        app.clinic.domain.model.entities.Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber)).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        Patient patient = patientRepository.findByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(order.getPatientIdentificationNumber())).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        User doctor = userRepository.findByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(order.getDoctorIdentificationNumber())).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        double totalCost = calculateTotalCost(order);
        String appliedMedications = getAppliedMedications(order);
        String appliedProcedures = getAppliedProcedures(order);
        String appliedDiagnosticAids = getAppliedDiagnosticAids(order);

        return generateBilling(order.getPatientIdentificationNumber(), doctor.getFullName(), orderNumber, totalCost, appliedMedications, appliedProcedures, appliedDiagnosticAids);
    }

    private double calculateTotalCost(app.clinic.domain.model.entities.Order order) {
        double total = 0.0;
        for (var med : order.getMedications()) {
            total += med.getCost();
        }
        for (var proc : order.getProcedures()) {
            total += proc.getCost();
        }
        for (var aid : order.getDiagnosticAids()) {
            total += aid.getCost();
        }
        return total;
    }

    private String getAppliedMedications(app.clinic.domain.model.entities.Order order) {
        StringBuilder sb = new StringBuilder();
        for (var med : order.getMedications()) {
            sb.append(med.getMedicationName()).append(" - ").append(med.getDosage()).append(" - ").append(med.getDuration()).append("; ");
        }
        return sb.toString();
    }

    private String getAppliedProcedures(app.clinic.domain.model.entities.Order order) {
        StringBuilder sb = new StringBuilder();
        for (var proc : order.getProcedures()) {
            sb.append(proc.getProcedureName()).append(" - ").append(proc.getQuantity()).append(" - ").append(proc.getFrequency()).append("; ");
        }
        return sb.toString();
    }

    private String getAppliedDiagnosticAids(app.clinic.domain.model.entities.Order order) {
        StringBuilder sb = new StringBuilder();
        for (var aid : order.getDiagnosticAids()) {
            sb.append(aid.getDiagnosticAidName()).append(" - ").append(aid.getQuantity()).append("; ");
        }
        return sb.toString();
    }

    private void validateAdminRole(String adminId) {
        app.clinic.domain.model.valueobject.Id adminIdObj = new app.clinic.domain.model.valueobject.Id(adminId);
        User admin = userRepository.findByIdentificationNumber(adminIdObj).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        roleBasedAccessService.checkAccess(admin.getRole(), "billing");

        // Validación específica: Solo Personal Administrativo puede generar facturas
        if (admin.getRole() != app.clinic.domain.model.valueobject.Role.PERSONAL_ADMINISTRATIVO) {
            throw new IllegalAccessError("Only Administrative staff can generate billing");
        }
    }
}