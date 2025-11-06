package app.clinic.infrastructure.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.OrderNotFoundException;
import app.clinic.domain.model.PatientNotFoundException;
import app.clinic.domain.model.UserNotFoundException;
import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.BillingRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.BillingService;

@Service
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public BillingServiceImpl(BillingRepository billingRepository, PatientRepository patientRepository,
                              OrderRepository orderRepository, UserRepository userRepository) {
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Billing generateBilling(Order order, Patient patient, User doctor, String generatedBy) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        if (generatedBy == null || generatedBy.trim().isEmpty()) {
            throw new IllegalArgumentException("GeneratedBy cannot be null or empty");
        }

        double totalCost = Billing.calculateTotalCostFromOrder(order);
        double copay = calculateCopay(totalCost, patient.getInsurance() != null && patient.getInsurance().isActive(), patient.getAnnualCopayTotal());

        int validityDays = 0;
        LocalDate validityDate = null;
        if (patient.getInsurance() != null && patient.getInsurance().isActive()) {
            validityDate = patient.getInsurance().getValidityDate();
            validityDays = (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), validityDate);
        }

        Billing billing = new Billing(
            order.getOrderNumber(),
            patient.getFullName(),
            patient.calculateAge(),
            patient.getIdentificationNumber().getValue(),
            doctor.getFullName(),
            patient.getInsurance() != null ? patient.getInsurance().getCompanyName() : null,
            patient.getInsurance() != null ? patient.getInsurance().getPolicyNumber() : null,
            validityDays,
            validityDate,
            totalCost,
            copay,
            (patient.getInsurance() != null && patient.getInsurance().isActive()) ? totalCost - copay : 0.0,
            Billing.formatAppliedMedications(order),
            Billing.formatAppliedProcedures(order),
            Billing.formatAppliedDiagnosticAids(order),
            LocalDateTime.now(),
            generatedBy
        );

        // Save billing and update patient
        billingRepository.save(billing);
        patient.addToAnnualCopayTotal(copay);
        patientRepository.save(patient);

        return billing;
    }

    @Override
    public String generatePrintableInvoice(Billing billing) {
        StringBuilder invoice = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        invoice.append("========================================\n");
        invoice.append("         FACTURA MÉDICA CLÍNICA\n");
        invoice.append("========================================\n\n");

        invoice.append("INFORMACIÓN DEL PACIENTE:\n");
        invoice.append("Nombre: ").append(billing.getPatientName()).append("\n");
        invoice.append("Edad: ").append(billing.getAge()).append(" años\n");
        invoice.append("Cédula: ").append(billing.getIdentificationNumber()).append("\n\n");

        invoice.append("INFORMACIÓN MÉDICA:\n");
        invoice.append("Médico Tratante: ").append(billing.getDoctorName()).append("\n");
        invoice.append("Compañía de Seguro: ").append(billing.getCompany() != null ? billing.getCompany() : "N/A").append("\n");
        invoice.append("Número de Póliza: ").append(billing.getPolicyNumber() != null ? billing.getPolicyNumber() : "N/A").append("\n");
        invoice.append("Días de Vigencia: ").append(billing.getValidityDays()).append("\n");
        if (billing.getValidityDate() != null) {
            invoice.append("Fecha de Finalización: ").append(billing.getValidityDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        }
        invoice.append("\n");

        invoice.append("SERVICIOS PRESTADOS:\n");
        if (!billing.getAppliedMedications().isEmpty()) {
            invoice.append("Medicamentos:\n").append(billing.getAppliedMedications()).append("\n");
        }
        if (!billing.getAppliedProcedures().isEmpty()) {
            invoice.append("Procedimientos:\n").append(billing.getAppliedProcedures()).append("\n");
        }
        if (!billing.getAppliedDiagnosticAids().isEmpty()) {
            invoice.append("Ayudas Diagnósticas:\n").append(billing.getAppliedDiagnosticAids()).append("\n");
        }
        invoice.append("\n");

        invoice.append("RESUMEN DE COSTOS:\n");
        invoice.append("Costo Total: $").append(String.format("%,.0f", billing.getTotalCost())).append("\n");
        invoice.append("Copago Paciente: $").append(String.format("%,.0f", billing.getCopay())).append("\n");
        invoice.append("Cobertura Seguro: $").append(String.format("%,.0f", billing.getInsuranceCoverage())).append("\n\n");

        invoice.append("Fecha de Generación: ").append(billing.getGeneratedAt().format(formatter)).append("\n");
        invoice.append("Generado por: ").append(billing.getGeneratedBy()).append("\n\n");

        invoice.append("========================================\n");
        invoice.append("         ¡Gracias por su visita!\n");
        invoice.append("========================================\n");

        return invoice.toString();
    }

    @Override
    public double calculateCopay(double totalCost, boolean hasActiveInsurance, double currentAnnualCopayTotal) {
        return Billing.calculateCopay(totalCost, hasActiveInsurance, currentAnnualCopayTotal);
    }

    @Override
    public Billing generateBillingFromOrder(String orderNumber, String adminId) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be null or empty");
        }
        if (adminId == null || adminId.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin ID cannot be null or empty");
        }

        // 1. Find the order by orderNumber
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
            .orElseThrow(() -> new OrderNotFoundException("Order not found with number: " + orderNumber));

        // 2. Find the patient by order.patientId
        Patient patient = patientRepository.findByIdentificationNumber(new Id(order.getPatientIdentificationNumber()))
            .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + order.getPatientIdentificationNumber()));

        // 3. Find the doctor by order.doctorId
        User doctor = userRepository.findByIdentificationNumber(new Id(order.getDoctorIdentificationNumber()))
            .orElseThrow(() -> new UserNotFoundException("Doctor not found with ID: " + order.getDoctorIdentificationNumber()));

        // 4. Generate billing using the existing method
        return generateBilling(order, patient, doctor, adminId);
    }

    @Override
    public boolean isCopayLimitExceeded(double currentAnnualCopayTotal) {
        return currentAnnualCopayTotal >= 1000000.0;
    }

    @Override
    public java.util.List<Billing> findBillingByPatientId(String patientId) {
        // This would require a BillingRepository to be properly implemented
        // For now, return empty list
        return new java.util.ArrayList<>();
    }
}