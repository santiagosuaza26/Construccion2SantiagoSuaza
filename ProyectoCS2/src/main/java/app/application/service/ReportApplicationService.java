package app.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.dto.response.CommonResponse;
import app.application.dto.response.InventoryResponse;
import app.application.dto.response.InvoiceResponse;
import app.application.dto.response.PatientResponse;
import app.application.mapper.InvoiceMapper;
import app.application.mapper.PatientMapper;
import app.domain.model.Invoice;
import app.domain.model.Patient;
import app.domain.model.Role;
import app.domain.port.InvoiceRepository;
import app.domain.port.PatientRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Servicio de aplicación para reportes y consultas
 *
 * Implementa casos de uso de reportes según requerimientos:
 * - Reportes de facturación por paciente, fecha o número de orden
 * - Reportes de pacientes por fecha de registro
 * - Reportes de inventario (stock bajo, vencimientos, etc.)
 * - Estadísticas generales del sistema
 * - Reportes médicos por diagnóstico, médico, etc.
 */
@Service
public class ReportApplicationService {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final InvoiceMapper invoiceMapper;
    private final PatientMapper patientMapper;

    public ReportApplicationService(InvoiceRepository invoiceRepository,
                                   PatientRepository patientRepository,
                                   InvoiceMapper invoiceMapper,
                                   PatientMapper patientMapper) {
        this.invoiceRepository = invoiceRepository;
        this.patientRepository = patientRepository;
        this.invoiceMapper = invoiceMapper;
        this.patientMapper = patientMapper;
    }

    /**
     * REPORTE DE FACTURACIÓN POR PERÍODO
     * Incluye totales, copagos, cobertura de seguros
     */
    public CommonResponse<BillingReport> generateBillingReport(String startDate, String endDate,
                                                               AuthenticatedUser currentUser) {
        try {
            if (!canGenerateReports(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_BILLING_REPORT");
                return CommonResponse.error("Access denied - Only Administrative staff can generate reports", "REP_001");
            }

            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (start.isAfter(end)) {
                return CommonResponse.error("Start date cannot be after end date", "REP_002");
            }

            List<Invoice> invoices = invoiceRepository.findByDateRange(start, end);

            BillingReport report = createBillingReport(invoices, start, end);

            logBillingReportGenerated(report, currentUser);

            return CommonResponse.success("Billing report generated successfully", report);

        } catch (Exception e) {
            logSystemError("generateBillingReport", e, currentUser);
            return CommonResponse.error("Internal error generating billing report", "REP_003");
        }
    }

    /**
     * REPORTE DE FACTURACIÓN POR PACIENTE
     * Historial completo de facturación de un paciente
     */
    public CommonResponse<PatientBillingReport> generatePatientBillingReport(String patientIdCard, Integer year,
                                                                            AuthenticatedUser currentUser) {
        try {
            if (!canGenerateReports(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_PATIENT_BILLING_REPORT");
                return CommonResponse.error("Access denied - Only Administrative staff can generate reports", "REP_004");
            }

            if (patientIdCard == null || patientIdCard.isBlank()) {
                return CommonResponse.error("Patient ID card is required", "REP_005");
            }

            int reportYear = year != null ? year : LocalDate.now().getYear();
            List<Invoice> invoices = invoiceRepository.findByPatientAndYear(patientIdCard, reportYear);

            PatientBillingReport report = createPatientBillingReport(patientIdCard, reportYear, invoices);

            logPatientBillingReportGenerated(report, currentUser);

            return CommonResponse.success("Patient billing report generated successfully", report);

        } catch (Exception e) {
            logSystemError("generatePatientBillingReport", e, currentUser);
            return CommonResponse.error("Internal error generating patient billing report", "REP_006");
        }
    }

    /**
     * REPORTE DE PACIENTES REGISTRADOS
     * Por período de tiempo
     */
    public CommonResponse<PatientRegistrationReport> generatePatientRegistrationReport(String startDate, String endDate,
                                                                                      AuthenticatedUser currentUser) {
        try {
            if (!canGenerateReports(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_PATIENT_REGISTRATION_REPORT");
                return CommonResponse.error("Access denied - Only Administrative staff can generate reports", "REP_007");
            }

            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (start.isAfter(end)) {
                return CommonResponse.error("Start date cannot be after end date", "REP_008");
            }

            List<Patient> patients = patientRepository.findByRegistrationDateRange(start, end);

            PatientRegistrationReport report = createPatientRegistrationReport(patients, start, end);

            logPatientRegistrationReportGenerated(report, currentUser);

            return CommonResponse.success("Patient registration report generated successfully", report);

        } catch (Exception e) {
            logSystemError("generatePatientRegistrationReport", e, currentUser);
            return CommonResponse.error("Internal error generating patient registration report", "REP_009");
        }
    }

    /**
     * REPORTE DE INVENTARIO
     * Estado actual del inventario con estadísticas
     */
    public CommonResponse<InventoryReport> generateInventoryReport(AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_INVENTORY_REPORT");
                return CommonResponse.error("Access denied - Only Support staff can generate inventory reports", "REP_010");
            }

            // TODO: Implementar cuando esté disponible el servicio de inventario
            List<InventoryResponse> medications = List.of();
            List<InventoryResponse> procedures = List.of();
            List<InventoryResponse> diagnostics = List.of();

            InventoryReport report = createInventoryReport(medications, procedures, diagnostics);

            logInventoryReportGenerated(report, currentUser);

            return CommonResponse.success("Inventory report generated successfully", report);

        } catch (Exception e) {
            logSystemError("generateInventoryReport", e, currentUser);
            return CommonResponse.error("Internal error generating inventory report", "REP_011");
        }
    }

    /**
     * REPORTE MÉDICO POR DIAGNÓSTICO
     * Estadísticas de diagnósticos más comunes
     */
    public CommonResponse<MedicalReport> generateMedicalReport(String startDate, String endDate,
                                                              AuthenticatedUser currentUser) {
        try {
            if (!canAccessMedicalReports(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_MEDICAL_REPORT");
                return CommonResponse.error("Access denied - Only medical staff can generate medical reports", "REP_012");
            }

            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (start.isAfter(end)) {
                return CommonResponse.error("Start date cannot be after end date", "REP_013");
            }

            // TODO: Implementar cuando esté disponible la historia clínica
            List<Invoice> invoices = invoiceRepository.findByDateRange(start, end);

            MedicalReport report = createMedicalReport(invoices, start, end);

            logMedicalReportGenerated(report, currentUser);

            return CommonResponse.success("Medical report generated successfully", report);

        } catch (Exception e) {
            logSystemError("generateMedicalReport", e, currentUser);
            return CommonResponse.error("Internal error generating medical report", "REP_014");
        }
    }

    /**
     * ESTADÍSTICAS GENERALES DEL SISTEMA
     * Dashboard con métricas principales
     */
    public CommonResponse<SystemStatistics> generateSystemStatistics(AuthenticatedUser currentUser) {
        try {
            if (!canGenerateReports(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_SYSTEM_STATISTICS");
                return CommonResponse.error("Access denied - Only Administrative staff can view system statistics", "REP_015");
            }

            // Obtener estadísticas básicas
            long totalPatients = patientRepository.count();
            long totalInvoices = invoiceRepository.count();
            long totalInvoicesThisMonth = invoiceRepository.countInvoicesThisMonth();
            double averageInvoiceAmount = invoiceRepository.getAverageInvoiceAmount();

            SystemStatistics statistics = new SystemStatistics(
                totalPatients, totalInvoices, totalInvoicesThisMonth, averageInvoiceAmount
            );

            logSystemStatisticsGenerated(statistics, currentUser);

            return CommonResponse.success("System statistics generated successfully", statistics);

        } catch (Exception e) {
            logSystemError("generateSystemStatistics", e, currentUser);
            return CommonResponse.error("Internal error generating system statistics", "REP_016");
        }
    }

    // =============================================================================
    // MÉTODOS PRIVADOS DE CREACIÓN DE REPORTES
    // =============================================================================

    private BillingReport createBillingReport(List<Invoice> invoices, LocalDate start, LocalDate end) {
        long totalInvoices = invoices.size();
        long totalAmount = invoices.stream().mapToLong(Invoice::getTotal).sum();
        long totalCopay = invoices.stream().mapToLong(Invoice::getCopay).sum();
        long totalInsurance = totalAmount - totalCopay;

        return new BillingReport(
            start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            totalInvoices, totalAmount, totalCopay, totalInsurance,
            invoiceMapper.toInvoiceResponseList(invoices)
        );
    }

    private PatientBillingReport createPatientBillingReport(String patientIdCard, int year, List<Invoice> invoices) {
        long totalInvoices = invoices.size();
        long totalAmount = invoices.stream().mapToLong(Invoice::getTotal).sum();
        long totalCopay = invoices.stream().mapToLong(Invoice::getCopay).sum();
        long totalInsurance = totalAmount - totalCopay;

        return new PatientBillingReport(
            patientIdCard, year, totalInvoices, totalAmount, totalCopay, totalInsurance,
            invoiceMapper.toInvoiceResponseList(invoices)
        );
    }

    private PatientRegistrationReport createPatientRegistrationReport(List<Patient> patients, LocalDate start, LocalDate end) {
        Map<String, Long> patientsByGender = patients.stream()
            .collect(Collectors.groupingBy(Patient::getGender, Collectors.counting()));

        Map<Integer, Long> patientsByAgeGroup = patients.stream()
            .collect(Collectors.groupingBy(
                p -> java.time.Period.between(p.getBirthDate(), LocalDate.now()).getYears() / 10 * 10,
                Collectors.counting()
            ));

        return new PatientRegistrationReport(
            start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            patients.size(), patientsByGender, patientsByAgeGroup,
            patientMapper.toPatientResponseList(patients)
        );
    }

    private InventoryReport createInventoryReport(List<InventoryResponse> medications,
                                                List<InventoryResponse> procedures,
                                                List<InventoryResponse> diagnostics) {
        long totalMedications = medications.size();
        long totalProcedures = procedures.size();
        long totalDiagnostics = diagnostics.size();
        long lowStockItems = medications.stream().mapToLong(m -> m.getItems().stream().mapToLong(i -> i.getStock() < 10 ? 1 : 0).sum()).sum();

        return new InventoryReport(totalMedications, totalProcedures, totalDiagnostics, lowStockItems);
    }

    private MedicalReport createMedicalReport(List<Invoice> invoices, LocalDate start, LocalDate end) {
        // TODO: Implementar análisis de diagnósticos cuando esté disponible la historia clínica
        Map<String, Long> diagnoses = Map.of("En análisis", 0L);
        Map<String, Long> topDoctors = invoices.stream()
            .collect(Collectors.groupingBy(inv -> "Dr. " + inv.getDoctorName(), Collectors.counting()));

        return new MedicalReport(
            start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            diagnoses, topDoctors
        );
    }

    // =============================================================================
    // MÉTODOS PRIVADOS DE AUTORIZACIÓN
    // =============================================================================

    private boolean canGenerateReports(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.ADMINISTRATIVE;
    }

    private boolean canManageInventory(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.SUPPORT && user.canManageInventory();
    }

    private boolean canAccessMedicalReports(AuthenticatedUser user) {
        return user != null && (user.getRole() == Role.DOCTOR || user.getRole() == Role.NURSE);
    }

    // =============================================================================
    // MÉTODOS DE LOGGING Y AUDITORÍA
    // =============================================================================

    private void logBillingReportGenerated(BillingReport report, AuthenticatedUser currentUser) {
        System.out.printf("BILLING REPORT GENERATED: %s generated report for period %s to %s with %d invoices at %s%n",
            currentUser.getFullName(), report.startDate, report.endDate, report.totalInvoices,
            java.time.LocalDateTime.now());
    }

    private void logPatientBillingReportGenerated(PatientBillingReport report, AuthenticatedUser currentUser) {
        System.out.printf("PATIENT BILLING REPORT GENERATED: %s generated report for patient %s year %d at %s%n",
            currentUser.getFullName(), report.patientIdCard, report.year, java.time.LocalDateTime.now());
    }

    private void logPatientRegistrationReportGenerated(PatientRegistrationReport report, AuthenticatedUser currentUser) {
        System.out.printf("PATIENT REGISTRATION REPORT GENERATED: %s generated report for period %s to %s with %d patients at %s%n",
            currentUser.getFullName(), report.startDate, report.endDate, report.totalPatients,
            java.time.LocalDateTime.now());
    }

    private void logInventoryReportGenerated(InventoryReport report, AuthenticatedUser currentUser) {
        System.out.printf("INVENTORY REPORT GENERATED: %s generated inventory report at %s%n",
            currentUser.getFullName(), java.time.LocalDateTime.now());
    }

    private void logMedicalReportGenerated(MedicalReport report, AuthenticatedUser currentUser) {
        System.out.printf("MEDICAL REPORT GENERATED: %s generated medical report for period %s to %s at %s%n",
            currentUser.getFullName(), report.startDate, report.endDate, java.time.LocalDateTime.now());
    }

    private void logSystemStatisticsGenerated(SystemStatistics statistics, AuthenticatedUser currentUser) {
        System.out.printf("SYSTEM STATISTICS GENERATED: %s generated statistics - %d patients, %d invoices at %s%n",
            currentUser.getFullName(), statistics.totalPatients, statistics.totalInvoices,
            java.time.LocalDateTime.now());
    }

    private void logUnauthorizedAccess(AuthenticatedUser user, String operation) {
        System.err.printf("UNAUTHORIZED ACCESS: User %s (%s) role %s attempted %s at %s%n",
            user.getFullName(), user.getIdCard(), user.getRole(), operation,
            java.time.LocalDateTime.now());
    }

    private void logSystemError(String operation, Exception e, AuthenticatedUser user) {
        System.err.printf("SYSTEM ERROR in %s by %s: %s at %s%n",
            operation, user.getFullName(), e.getMessage(), java.time.LocalDateTime.now());
    }

    // =============================================================================
    // CLASES INTERNAS PARA REPORTES
    // =============================================================================

    public static class BillingReport {
        public final String startDate;
        public final String endDate;
        public final long totalInvoices;
        public final long totalAmount;
        public final long totalCopay;
        public final long totalInsurance;
        public final List<InvoiceResponse> invoices;

        public BillingReport(String startDate, String endDate, long totalInvoices, long totalAmount,
                           long totalCopay, long totalInsurance, List<InvoiceResponse> invoices) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalInvoices = totalInvoices;
            this.totalAmount = totalAmount;
            this.totalCopay = totalCopay;
            this.totalInsurance = totalInsurance;
            this.invoices = invoices;
        }
    }

    public static class PatientBillingReport {
        public final String patientIdCard;
        public final int year;
        public final long totalInvoices;
        public final long totalAmount;
        public final long totalCopay;
        public final long totalInsurance;
        public final List<InvoiceResponse> invoices;

        public PatientBillingReport(String patientIdCard, int year, long totalInvoices, long totalAmount,
                                  long totalCopay, long totalInsurance, List<InvoiceResponse> invoices) {
            this.patientIdCard = patientIdCard;
            this.year = year;
            this.totalInvoices = totalInvoices;
            this.totalAmount = totalAmount;
            this.totalCopay = totalCopay;
            this.totalInsurance = totalInsurance;
            this.invoices = invoices;
        }
    }

    public static class PatientRegistrationReport {
        public final String startDate;
        public final String endDate;
        public final long totalPatients;
        public final Map<String, Long> patientsByGender;
        public final Map<Integer, Long> patientsByAgeGroup;
        public final List<PatientResponse> patients;

        public PatientRegistrationReport(String startDate, String endDate, long totalPatients,
                                       Map<String, Long> patientsByGender, Map<Integer, Long> patientsByAgeGroup,
                                       List<PatientResponse> patients) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalPatients = totalPatients;
            this.patientsByGender = patientsByGender;
            this.patientsByAgeGroup = patientsByAgeGroup;
            this.patients = patients;
        }
    }

    public static class InventoryReport {
        public final long totalMedications;
        public final long totalProcedures;
        public final long totalDiagnostics;
        public final long lowStockItems;

        public InventoryReport(long totalMedications, long totalProcedures, long totalDiagnostics, long lowStockItems) {
            this.totalMedications = totalMedications;
            this.totalProcedures = totalProcedures;
            this.totalDiagnostics = totalDiagnostics;
            this.lowStockItems = lowStockItems;
        }
    }

    public static class MedicalReport {
        public final String startDate;
        public final String endDate;
        public final Map<String, Long> diagnoses;
        public final Map<String, Long> topDoctors;

        public MedicalReport(String startDate, String endDate, Map<String, Long> diagnoses, Map<String, Long> topDoctors) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.diagnoses = diagnoses;
            this.topDoctors = topDoctors;
        }
    }

    public static class SystemStatistics {
        public final long totalPatients;
        public final long totalInvoices;
        public final long totalInvoicesThisMonth;
        public final double averageInvoiceAmount;

        public SystemStatistics(long totalPatients, long totalInvoices, long totalInvoicesThisMonth, double averageInvoiceAmount) {
            this.totalPatients = totalPatients;
            this.totalInvoices = totalInvoices;
            this.totalInvoicesThisMonth = totalInvoicesThisMonth;
            this.averageInvoiceAmount = averageInvoiceAmount;
        }
    }
}