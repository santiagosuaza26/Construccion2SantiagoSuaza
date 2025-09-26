package app.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import app.application.dto.request.GenerateInvoiceRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.InvoiceResponse;
import app.application.mapper.InvoiceMapper;
import app.domain.model.Invoice;
import app.domain.model.OrderHeader;
import app.domain.model.Role;
import app.domain.port.InvoiceRepository;
import app.domain.port.OrderHeaderRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;
import app.domain.services.BillingService;

@Service
public class BillingApplicationService {
    
    private final BillingService billingService;
    private final InvoiceRepository invoiceRepository;
    private final OrderHeaderRepository orderHeaderRepository;
    private final InvoiceMapper invoiceMapper;
    
    public BillingApplicationService(BillingService billingService,
                                   InvoiceRepository invoiceRepository,
                                   OrderHeaderRepository orderHeaderRepository,
                                   InvoiceMapper invoiceMapper) {
        this.billingService = billingService;
        this.invoiceRepository = invoiceRepository;
        this.orderHeaderRepository = orderHeaderRepository;
        this.invoiceMapper = invoiceMapper;
    }
    
    public CommonResponse<InvoiceResponse> generateInvoice(GenerateInvoiceRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canGenerateInvoices(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_INVOICE");
                return CommonResponse.error("Access denied - Only Administrative staff can generate invoices", "BILL_001");
            }
            
            validateGenerateInvoiceRequest(request);
            invoiceMapper.validateGenerateInvoiceRequest(request);
            
            OrderHeader orderHeader = orderHeaderRepository.findByNumber(request.getOrderNumber())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + request.getOrderNumber()));
            
            if (!request.getPatientIdCard().equals(orderHeader.getPatientIdCard())) {
                return CommonResponse.error("Patient ID card does not match order", "BILL_002");
            }
            
            LocalDate invoiceDate = invoiceMapper.parseInvoiceDate(request);
            
            Invoice generatedInvoice = billingService.generateInvoice(
                request.getOrderNumber(),
                orderHeader,
                extractPatientName(request),
                request.getDoctorName(),
                invoiceDate
            );
            
            InvoiceResponse invoiceResponse = invoiceMapper.toInvoiceResponse(generatedInvoice);
            
            if (request.shouldPrint()) {
                handleInvoicePrinting(generatedInvoice, currentUser);
            }
            
            if (request.shouldSendByEmail()) {
                handleInvoiceEmail(generatedInvoice, currentUser);
            }
            
            logInvoiceGenerated(generatedInvoice, currentUser);
            
            return CommonResponse.success("Invoice generated successfully", invoiceResponse);
            
        } catch (IllegalArgumentException e) {
            logValidationError("generateInvoice", e, currentUser);
            return CommonResponse.error(e.getMessage(), "BILL_003");
        } catch (Exception e) {
            logSystemError("generateInvoice", e, currentUser);
            return CommonResponse.error("Internal error generating invoice", "BILL_004");
        }
    }
    
    public CommonResponse<InvoiceResponse> getInvoiceById(String invoiceId, AuthenticatedUser currentUser) {
        try {
            if (!canViewInvoices(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_INVOICE");
                return CommonResponse.error("Access denied - Cannot view invoices", "BILL_005");
            }
            
            if (invoiceId == null || invoiceId.isBlank()) {
                return CommonResponse.error("Invoice ID is required", "BILL_006");
            }
            
            Invoice invoice = findInvoiceById(invoiceId);
            if (invoice == null) {
                return CommonResponse.error("Invoice not found: " + invoiceId, "BILL_007");
            }
            
            if (currentUser.getRole() == Role.PATIENT && !currentUser.getIdCard().equals(invoice.getPatientIdCard())) {
                return CommonResponse.error("Patients can only view their own invoices", "BILL_008");
            }
            
            InvoiceResponse invoiceResponse = invoiceMapper.toInvoiceResponse(invoice);
            
            logInvoiceViewed(invoice, currentUser);
            
            return CommonResponse.success("Invoice retrieved successfully", invoiceResponse);
            
        } catch (Exception e) {
            logSystemError("getInvoiceById", e, currentUser);
            return CommonResponse.error("Internal error retrieving invoice", "BILL_009");
        }
    }
    
    public CommonResponse<List<InvoiceResponse>> getInvoiceHistory(String patientIdCard, Integer year, 
                                                                  AuthenticatedUser currentUser) {
        try {
            if (!canViewInvoiceHistory(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_INVOICE_HISTORY");
                return CommonResponse.error("Access denied - Cannot view invoice history", "BILL_010");
            }
            
            if (patientIdCard == null || patientIdCard.isBlank()) {
                return CommonResponse.error("Patient ID card is required", "BILL_011");
            }
            
            if (currentUser.getRole() == Role.PATIENT && !currentUser.getIdCard().equals(patientIdCard)) {
                return CommonResponse.error("Patients can only view their own invoice history", "BILL_012");
            }
            
            List<Invoice> invoices;
            if (year != null) {
                invoices = invoiceRepository.findByPatientAndYear(patientIdCard, year);
            } else {
                invoices = findInvoicesByPatient(patientIdCard);
            }
            
            List<InvoiceResponse> invoiceResponses = invoiceMapper.toInvoiceResponseList(invoices);
            
            logInvoiceHistoryViewed(patientIdCard, invoices.size(), currentUser);
            
            return CommonResponse.success(
                String.format("Retrieved %d invoices for patient %s", invoices.size(), patientIdCard),
                invoiceResponses
            );
            
        } catch (Exception e) {
            logSystemError("getInvoiceHistory", e, currentUser);
            return CommonResponse.error("Internal error retrieving invoice history", "BILL_013");
        }
    }
    
    public CommonResponse<InvoiceMapper.InvoiceSummary> calculateInvoiceSummary(String invoiceId, AuthenticatedUser currentUser) {
        try {
            if (!canViewInvoices(currentUser)) {
                logUnauthorizedAccess(currentUser, "CALCULATE_INVOICE_SUMMARY");
                return CommonResponse.error("Access denied - Cannot view invoice details", "BILL_014");
            }
            
            if (invoiceId == null || invoiceId.isBlank()) {
                return CommonResponse.error("Invoice ID is required", "BILL_015");
            }
            
            Invoice invoice = findInvoiceById(invoiceId);
            if (invoice == null) {
                return CommonResponse.error("Invoice not found: " + invoiceId, "BILL_016");
            }
            
            if (currentUser.getRole() == Role.PATIENT && !currentUser.getIdCard().equals(invoice.getPatientIdCard())) {
                return CommonResponse.error("Patients can only view their own invoice details", "BILL_017");
            }
            
            InvoiceMapper.InvoiceSummary summary = invoiceMapper.calculateInvoiceSummary(invoice);
            
            logInvoiceSummaryCalculated(invoice, currentUser);
            
            return CommonResponse.success("Invoice summary calculated", summary);
            
        } catch (Exception e) {
            logSystemError("calculateInvoiceSummary", e, currentUser);
            return CommonResponse.error("Internal error calculating invoice summary", "BILL_018");
        }
    }
    
    public CommonResponse<InvoiceMapper.InvoicingReport> generateInvoicingReport(String startDate, String endDate, 
                                                                                AuthenticatedUser currentUser) {
        try {
            if (!canGenerateReports(currentUser)) {
                logUnauthorizedAccess(currentUser, "GENERATE_INVOICING_REPORT");
                return CommonResponse.error("Access denied - Only Administrative staff can generate reports", "BILL_019");
            }
            
            LocalDate start = startDate != null ? LocalDate.parse(startDate, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
            
            if (start != null && end != null && start.isAfter(end)) {
                return CommonResponse.error("Start date cannot be after end date", "BILL_020");
            }
            
            List<Invoice> invoices = (start != null && end != null) ? 
                invoiceRepository.findByDateRange(start, end) : 
                findAllInvoices();
                
            InvoiceMapper.InvoicingReport report = invoiceMapper.createInvoicingReport(invoices, start, end);
            
            logInvoicingReportGenerated(report, currentUser);
            
            return CommonResponse.success("Invoicing report generated successfully", report);
            
        } catch (Exception e) {
            logSystemError("generateInvoicingReport", e, currentUser);
            return CommonResponse.error("Internal error generating invoicing report", "BILL_021");
        }
    }
    
    public CommonResponse<String> printInvoice(String invoiceId, AuthenticatedUser currentUser) {
        try {
            if (!canPrintInvoices(currentUser)) {
                logUnauthorizedAccess(currentUser, "PRINT_INVOICE");
                return CommonResponse.error("Access denied - Only Administrative staff can print invoices", "BILL_022");
            }
            
            if (invoiceId == null || invoiceId.isBlank()) {
                return CommonResponse.error("Invoice ID is required", "BILL_023");
            }
            
            Invoice invoice = findInvoiceById(invoiceId);
            if (invoice == null) {
                return CommonResponse.error("Invoice not found: " + invoiceId, "BILL_024");
            }
            
            handleInvoicePrinting(invoice, currentUser);
            
            logInvoicePrinted(invoice, currentUser);
            
            return CommonResponse.success("Invoice sent to printer successfully: " + invoiceId);
            
        } catch (Exception e) {
            logSystemError("printInvoice", e, currentUser);
            return CommonResponse.error("Internal error printing invoice", "BILL_025");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    // =============================================================================
    
    private void validateGenerateInvoiceRequest(GenerateInvoiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Generate invoice request cannot be null");
        }
        
        if (request.getOrderNumber() == null || request.getOrderNumber().isBlank()) {
            throw new IllegalArgumentException("Order number is required");
        }
        
        if (request.getPatientIdCard() == null || request.getPatientIdCard().isBlank()) {
            throw new IllegalArgumentException("Patient ID card is required");
        }
        
        if (request.getDoctorName() == null || request.getDoctorName().isBlank()) {
            throw new IllegalArgumentException("Doctor name is required for invoice");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE AUTORIZACIÓN
    // =============================================================================
    
    private boolean canGenerateInvoices(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.ADMINISTRATIVE && user.canGenerateInvoices();
    }
    
    private boolean canViewInvoices(AuthenticatedUser user) {
        return user != null && (user.getRole() == Role.ADMINISTRATIVE || user.getRole() == Role.PATIENT);
    }
    
    private boolean canViewInvoiceHistory(AuthenticatedUser user) {
        return user != null && (user.getRole() == Role.ADMINISTRATIVE || user.getRole() == Role.PATIENT);
    }
    
    private boolean canGenerateReports(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.ADMINISTRATIVE;
    }
    
    private boolean canPrintInvoices(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.ADMINISTRATIVE;
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // =============================================================================
    
    private String extractPatientName(GenerateInvoiceRequest request) {
        return "Patient " + request.getPatientIdCard();
    }
    
    private Invoice findInvoiceById(String invoiceId) {
        return null;
    }
    
    private List<Invoice> findInvoicesByPatient(String patientIdCard) {
        return List.of();
    }
    
    private List<Invoice> findAllInvoices() {
        return List.of();
    }
    
    private void handleInvoicePrinting(Invoice invoice, AuthenticatedUser currentUser) {
        System.out.printf("INVOICE PRINTING: Invoice %s queued for printing by %s at %s%n",
            invoice.getInvoiceId(), currentUser.getFullName(), java.time.LocalDateTime.now());
    }
    
    private void handleInvoiceEmail(Invoice invoice, AuthenticatedUser currentUser) {
        System.out.printf("INVOICE EMAIL: Invoice %s queued for email by %s at %s%n",
            invoice.getInvoiceId(), currentUser.getFullName(), java.time.LocalDateTime.now());
    }
    
    // =============================================================================
    // MÉTODOS DE LOGGING Y AUDITORÍA
    // =============================================================================
    
    private void logInvoiceGenerated(Invoice invoice, AuthenticatedUser currentUser) {
        System.out.printf("INVOICE GENERATED: %s generated invoice %s for patient %s, total: $%,d at %s%n",
            currentUser.getFullName(), invoice.getInvoiceId(), invoice.getPatientIdCard(),
            invoice.getTotal(), java.time.LocalDateTime.now());
    }
    
    private void logInvoiceViewed(Invoice invoice, AuthenticatedUser currentUser) {
        System.out.printf("INVOICE VIEWED: %s (%s) viewed invoice %s at %s%n",
            currentUser.getFullName(), currentUser.getRole(), invoice.getInvoiceId(),
            java.time.LocalDateTime.now());
    }
    
    private void logInvoiceHistoryViewed(String patientIdCard, int count, AuthenticatedUser currentUser) {
        System.out.printf("INVOICE HISTORY VIEWED: %s retrieved %d invoices for patient %s at %s%n",
            currentUser.getFullName(), count, patientIdCard, java.time.LocalDateTime.now());
    }
    
    private void logInvoiceSummaryCalculated(Invoice invoice, AuthenticatedUser currentUser) {
        System.out.printf("INVOICE SUMMARY CALCULATED: %s calculated summary for invoice %s at %s%n",
            currentUser.getFullName(), invoice.getInvoiceId(), java.time.LocalDateTime.now());
    }
    
    private void logInvoicingReportGenerated(InvoiceMapper.InvoicingReport report, AuthenticatedUser currentUser) {
        System.out.printf("INVOICING REPORT GENERATED: %s generated report with %d invoices at %s%n",
            currentUser.getFullName(), report.totalInvoices, java.time.LocalDateTime.now());
    }
    
    private void logInvoicePrinted(Invoice invoice, AuthenticatedUser currentUser) {
        System.out.printf("INVOICE PRINTED: %s printed invoice %s at %s%n",
            currentUser.getFullName(), invoice.getInvoiceId(), java.time.LocalDateTime.now());
    }
    
    private void logUnauthorizedAccess(AuthenticatedUser user, String operation) {
        System.err.printf("UNAUTHORIZED ACCESS: User %s (%s) role %s attempted %s at %s%n",
            user.getFullName(), user.getIdCard(), user.getRole(), operation,
            java.time.LocalDateTime.now());
    }
    
    private void logValidationError(String operation, Exception e, AuthenticatedUser user) {
        System.err.printf("VALIDATION ERROR in %s by %s: %s at %s%n",
            operation, user.getFullName(), e.getMessage(), java.time.LocalDateTime.now());
    }
    
    private void logSystemError(String operation, Exception e, AuthenticatedUser user) {
        System.err.printf("SYSTEM ERROR in %s by %s: %s at %s%n",
            operation, user.getFullName(), e.getMessage(), java.time.LocalDateTime.now());
    }
}