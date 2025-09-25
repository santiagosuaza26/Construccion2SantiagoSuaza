package app.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.application.dto.request.GenerateInvoiceRequest;
import app.application.dto.response.InvoiceResponse;
import app.domain.model.Invoice;
import app.domain.model.InvoiceLine;

/**
 * InvoiceMapper - Mapper profesional para facturas y facturación
 * 
 * RESPONSABILIDADES:
 * - Convertir GenerateInvoiceRequest → datos para BillingService
 * - Convertir Invoice (Domain) → InvoiceResponse
 * - Formatear información de facturación según documento
 * - Manejar cálculos de copagos y seguros
 * - Aplicar reglas de facturación médica
 * 
 * REGLAS DE FACTURACIÓN IMPLEMENTADAS:
 * - Información completa del paciente (nombre, edad, cédula)
 * - Información del médico tratante
 * - Detalles del seguro y vigencia
 * - Copago de $50,000 con límite anual $1,000,000
 * - Facturas detalladas por tipo de servicio
 */
@Component
public class InvoiceMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Convierte Invoice (Domain) → InvoiceResponse (DTO)
     * 
     * TRANSFORMACIONES COMPLEJAS:
     * - LocalDate → String (DD/MM/YYYY)
     * - Formatear monedas con separadores
     * - Crear líneas detalladas de facturación
     * - Calcular porcentajes de copago/cobertura
     * - Mostrar estado del seguro médico
     * 
     * @param invoice Domain model de la factura
     * @return InvoiceResponse DTO completo para el cliente
     */
    public InvoiceResponse toInvoiceResponse(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        
        try {
            InvoiceResponse response = new InvoiceResponse();
            
            // Información básica de la factura
            response.setInvoiceId(invoice.getInvoiceId());
            response.setPatientName(invoice.getPatientName());
            response.setPatientAge(invoice.getPatientAge());
            response.setPatientIdCard(invoice.getPatientIdCard());
            response.setDoctorName(invoice.getDoctorName());
            
            // Fecha de la factura (usar fecha actual como fecha de generación)
            response.setInvoiceDate(LocalDate.now().format(DATE_FORMATTER));
            
            // Información del seguro médico
            response.setInsuranceCompany(invoice.getInsuranceCompany());
            response.setPolicyNumber(invoice.getPolicyNumber());
            response.setPolicyRemainingDays(invoice.getPolicyRemainingDays());
            
            if (invoice.getPolicyEndDate() != null) {
                response.setPolicyEndDate(invoice.getPolicyEndDate().format(DATE_FORMATTER));
            }
            
            // Convertir líneas de facturación
            if (invoice.getLines() != null && !invoice.getLines().isEmpty()) {
                List<InvoiceResponse.InvoiceLineInfo> lineInfos = createInvoiceLineInfoList(invoice.getLines());
                response.setLines(lineInfos);
            }
            
            // Totales y cálculos financieros
            response.setSubtotal(invoice.getTotal());
            response.setCopay(invoice.getCopay());
            response.setInsuranceCoverage(invoice.getTotal() - invoice.getCopay());
            response.setTotalPatientPayment(invoice.getCopay());
            
            // Estado de la factura
            response.setStatus(determineInvoiceStatus(invoice));
            response.setPaid(false); // Por defecto no pagada
            
            return response;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting Invoice to InvoiceResponse: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte lista de Invoices → lista de InvoiceResponses
     * Optimizado para consultas de historial de facturación
     */
    public List<InvoiceResponse> toInvoiceResponseList(List<Invoice> invoices) {
        if (invoices == null) {
            throw new IllegalArgumentException("Invoice list cannot be null");
        }
        
        return invoices.stream()
                .map(this::toInvoiceResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Crear InvoiceResponse básico para listas (información esencial)
     * Útil para historiales donde no se necesita detalle completo
     */
    public InvoiceResponse toBasicInvoiceResponse(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        
        return new InvoiceResponse(
            invoice.getInvoiceId(),
            invoice.getPatientName(),
            invoice.getPatientAge(),
            invoice.getPatientIdCard(),
            invoice.getDoctorName(),
            LocalDate.now().format(DATE_FORMATTER),
            invoice.getTotal(),
            invoice.getCopay()
        );
    }
    
    /**
     * Parsear GenerateInvoiceRequest para extraer información necesaria
     * Útil para validaciones antes de generar factura
     */
    public LocalDate parseInvoiceDate(GenerateInvoiceRequest request) {
        if (request == null || request.getInvoiceDate() == null) {
            return LocalDate.now();
        }
        
        try {
            return LocalDate.parse(request.getInvoiceDate(), DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid invoice date format. Use DD/MM/YYYY", e);
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // =============================================================================
    
    /**
     * Crear lista de líneas de facturación detalladas
     * Según documento: incluir nombre, costo y detalles por tipo
     */
    private List<InvoiceResponse.InvoiceLineInfo> createInvoiceLineInfoList(List<InvoiceLine> lines) {
        return lines.stream()
                .map(this::toInvoiceLineInfo)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertir InvoiceLine → InvoiceLineInfo con información enriquecida
     */
    private InvoiceResponse.InvoiceLineInfo toInvoiceLineInfo(InvoiceLine line) {
        InvoiceResponse.InvoiceLineInfo lineInfo = new InvoiceResponse.InvoiceLineInfo();
        
        lineInfo.setDescription(line.getDescription());
        lineInfo.setAmount(line.getAmount());
        
        // Determinar tipo de item y extraer detalles
        String description = line.getDescription().toLowerCase();
        
        if (description.contains("medicamento") || description.contains("medication")) {
            lineInfo.setItemType("MEDICATION");
            lineInfo.setItemName(extractItemName(line.getDescription(), "Medicamento:", "Medication:"));
            lineInfo.setDetails(extractMedicationDetails(line.getDescription()));
        } else if (description.contains("procedimiento") || description.contains("procedure")) {
            lineInfo.setItemType("PROCEDURE");
            lineInfo.setItemName(extractItemName(line.getDescription(), "Procedimiento:", "Procedure:"));
            lineInfo.setDetails(extractProcedureDetails(line.getDescription()));
        } else if (description.contains("ayuda diagnóstica") || description.contains("diagnostic")) {
            lineInfo.setItemType("DIAGNOSTIC");
            lineInfo.setItemName(extractItemName(line.getDescription(), "Ayuda Diagnóstica:", "Diagnostic:"));
            lineInfo.setDetails(extractDiagnosticDetails(line.getDescription()));
        } else {
            lineInfo.setItemType("OTHER");
            lineInfo.setItemName("Servicio Médico");
            lineInfo.setDetails(line.getDescription());
        }
        
        return lineInfo;
    }
    
    /**
     * Extraer nombre del item de la descripción
     */
    private String extractItemName(String description, String... prefixes) {
        for (String prefix : prefixes) {
            int startIndex = description.indexOf(prefix);
            if (startIndex >= 0) {
                int nameStart = startIndex + prefix.length();
                int nameEnd = description.indexOf(" - ", nameStart);
                if (nameEnd == -1) nameEnd = description.indexOf("(", nameStart);
                if (nameEnd == -1) nameEnd = description.length();
                
                return description.substring(nameStart, nameEnd).trim();
            }
        }
        return "Servicio Médico";
    }
    
    /**
     * Extraer detalles específicos de medicamentos
     * Formato esperado: "Medicamento: Nombre - Dosis: X - Duración: Y"
     */
    private String extractMedicationDetails(String description) {
        StringBuilder details = new StringBuilder();
        
        if (description.contains("Dosis:") || description.contains("Dosage:")) {
            String dosage = extractDetailValue(description, "Dosis:", "Dosage:");
            if (dosage != null) details.append("Dosis: ").append(dosage).append(" ");
        }
        
        if (description.contains("Duración:") || description.contains("Duration:")) {
            String duration = extractDetailValue(description, "Duración:", "Duration:");
            if (duration != null) details.append("Duración: ").append(duration);
        }
        
        return details.toString().trim();
    }
    
    /**
     * Extraer detalles específicos de procedimientos
     * Formato esperado: "Procedimiento: Nombre - Cantidad: X - Frecuencia: Y"
     */
    private String extractProcedureDetails(String description) {
        StringBuilder details = new StringBuilder();
        
        if (description.contains("Cantidad:") || description.contains("Qty:")) {
            String quantity = extractDetailValue(description, "Cantidad:", "Qty:");
            if (quantity != null) details.append("Cantidad: ").append(quantity).append(" ");
        }
        
        if (description.contains("Frecuencia:") || description.contains("Frequency:")) {
            String frequency = extractDetailValue(description, "Frecuencia:", "Frequency:");
            if (frequency != null) details.append("Frecuencia: ").append(frequency);
        }
        
        return details.toString().trim();
    }
    
    /**
     * Extraer detalles específicos de ayudas diagnósticas
     * Formato esperado: "Ayuda Diagnóstica: Nombre - Cantidad: X"
     */
    private String extractDiagnosticDetails(String description) {
        StringBuilder details = new StringBuilder();
        
        if (description.contains("Cantidad:") || description.contains("Qty:")) {
            String quantity = extractDetailValue(description, "Cantidad:", "Qty:");
            if (quantity != null) details.append("Cantidad: ").append(quantity);
        }
        
        return details.toString().trim();
    }
    
    /**
     * Extraer valor de detalle de la descripción
     */
    private String extractDetailValue(String description, String... keys) {
        for (String key : keys) {
            int startIndex = description.indexOf(key);
            if (startIndex >= 0) {
                int valueStart = startIndex + key.length();
                int valueEnd = description.indexOf(" - ", valueStart);
                if (valueEnd == -1) valueEnd = description.indexOf(",", valueStart);
                if (valueEnd == -1) valueEnd = description.indexOf(")", valueStart);
                if (valueEnd == -1) valueEnd = description.length();
                
                return description.substring(valueStart, valueEnd).trim();
            }
        }
        return null;
    }
    
    /**
     * Determinar estado de la factura basado en información disponible
     */
    private String determineInvoiceStatus(Invoice invoice) {
        if (invoice == null) return "UNKNOWN";
        
        // Lógica básica para determinar estado
        if (invoice.getTotal() > 0) {
            if (invoice.getCopay() == invoice.getTotal() && !hasInsurance(invoice)) {
                return "PENDING_PAYMENT"; // Paciente paga todo
            } else if (invoice.getCopay() < invoice.getTotal() && hasInsurance(invoice)) {
                return "PENDING_INSURANCE"; // Esperando procesamiento del seguro
            } else if (invoice.getCopay() == 0) {
                return "COVERED_BY_INSURANCE"; // Cubierto completamente por seguro
            } else {
                return "PARTIAL_PAYMENT"; // Copago parcial
            }
        }
        
        return "PROCESSED";
    }
    
    /**
     * Verificar si la factura tiene información de seguro válida
     */
    private boolean hasInsurance(Invoice invoice) {
        return invoice.getInsuranceCompany() != null && 
               !invoice.getInsuranceCompany().isBlank() &&
               !"SIN SEGURO".equals(invoice.getInsuranceCompany()) &&
               invoice.getPolicyNumber() != null && 
               !"N/A".equals(invoice.getPolicyNumber());
    }
    
    // =============================================================================
    // MÉTODOS DE UTILIDAD PARA VALIDACIONES Y CÁLCULOS
    // =============================================================================
    
    /**
     * Validar GenerateInvoiceRequest antes del procesamiento
     */
    public void validateGenerateInvoiceRequest(GenerateInvoiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("GenerateInvoiceRequest cannot be null");
        }

        // Validar campos obligatorios
        if (request.getOrderNumber() == null || request.getOrderNumber().isBlank()) {
            throw new IllegalArgumentException("Order number is required");
        }

        if (request.getPatientIdCard() == null || request.getPatientIdCard().isBlank()) {
            throw new IllegalArgumentException("Patient ID card is required");
        }

        if (request.getDoctorName() == null || request.getDoctorName().isBlank()) {
            throw new IllegalArgumentException("Doctor name is required for invoice");
        }

        // Validar formato de número de orden
        if (!request.getOrderNumber().matches("\\d{1,6}")) {
            throw new IllegalArgumentException("Order number must be numeric with maximum 6 digits");
        }
        
        // Validar fecha de factura si está presente
        if (request.getInvoiceDate() != null && !request.getInvoiceDate().isBlank()) {
            try {
                LocalDate invoiceDate = LocalDate.parse(request.getInvoiceDate(), DATE_FORMATTER);
                
                // No permitir facturas con fecha futura
                if (invoiceDate.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Invoice date cannot be in the future");
                }
                
                // No permitir facturas muy antiguas (más de 1 año)
                if (invoiceDate.isBefore(LocalDate.now().minusYears(1))) {
                    throw new IllegalArgumentException("Invoice date cannot be more than 1 year old");
                }
                
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid invoice date format. Use DD/MM/YYYY", e);
            }
        }
    }
    
    /**
     * Calcular información de resumen para el invoice
     */
    public InvoiceSummary calculateInvoiceSummary(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        
        InvoiceSummary summary = new InvoiceSummary();
        
        // Totales básicos
        summary.subtotal = invoice.getTotal();
        summary.copay = invoice.getCopay();
        summary.insuranceCoverage = invoice.getTotal() - invoice.getCopay();
        summary.patientOwes = invoice.getCopay();
        
        // Porcentajes
        if (invoice.getTotal() > 0) {
            summary.copayPercentage = (double) invoice.getCopay() / invoice.getTotal() * 100;
            summary.insurancePercentage = (double) summary.insuranceCoverage / invoice.getTotal() * 100;
        }
        
        // Información del seguro
        summary.hasInsurance = hasInsurance(invoice);
        summary.insuranceActive = summary.hasInsurance && invoice.getPolicyRemainingDays() > 0;
        summary.policyExpiringSoon = summary.hasInsurance && 
                                   invoice.getPolicyRemainingDays() > 0 && 
                                   invoice.getPolicyRemainingDays() <= 30;
        
        // Estado de pago
        summary.requiresPayment = invoice.getCopay() > 0;
        summary.fullyInsured = invoice.getCopay() == 0 && invoice.getTotal() > 0;
        summary.noInsurance = !summary.hasInsurance;
        
        return summary;
    }
    
    /**
     * Crear reporte de facturación para período específico
     */
    public InvoicingReport createInvoicingReport(List<Invoice> invoices, LocalDate startDate, LocalDate endDate) {
        if (invoices == null) {
            throw new IllegalArgumentException("Invoice list cannot be null");
        }
        
        InvoicingReport report = new InvoicingReport();
        report.startDate = startDate != null ? startDate.format(DATE_FORMATTER) : "N/A";
        report.endDate = endDate != null ? endDate.format(DATE_FORMATTER) : "N/A";
        report.totalInvoices = invoices.size();
        
        if (!invoices.isEmpty()) {
            // Cálculos agregados
            report.totalRevenue = invoices.stream().mapToLong(Invoice::getTotal).sum();
            report.totalCopays = invoices.stream().mapToLong(Invoice::getCopay).sum();
            report.totalInsuranceCoverage = report.totalRevenue - report.totalCopays;
            
            // Estadísticas
            report.invoicesWithInsurance = (int) invoices.stream()
                .filter(this::hasInsurance)
                .count();
            
            report.averageInvoiceAmount = invoices.stream()
                .mapToLong(Invoice::getTotal)
                .average()
                .orElse(0.0);
            
            report.averageCopay = invoices.stream()
                .mapToLong(Invoice::getCopay)
                .average()
                .orElse(0.0);
        }
        
        return report;
    }
    
    /**
     * Crear factura de prueba para testing
     */
    public GenerateInvoiceRequest createTestInvoiceRequest(String orderNumber, String patientIdCard, String doctorName) {
        return new GenerateInvoiceRequest(
            orderNumber,
            patientIdCard,
            doctorName,
            LocalDate.now().format(DATE_FORMATTER),
            "Test invoice generation",
            false,
            false
        );
    }
    
    /**
     * Método para debugging de facturas
     */
    public String invoiceToDebugString(Invoice invoice) {
        if (invoice == null) return "Invoice[null]";
        
        return String.format("Invoice[id=%s, patient=%s, doctor=%s, total=%s, copay=%s, hasInsurance=%s, lines=%d]",
            invoice.getInvoiceId(),
            invoice.getPatientName(),
            invoice.getDoctorName(),
            String.format("$%,d", invoice.getTotal()),
            String.format("$%,d", invoice.getCopay()),
            hasInsurance(invoice),
            invoice.getLines() != null ? invoice.getLines().size() : 0
        );
    }
    
    // =============================================================================
    // CLASES INTERNAS DE UTILIDAD
    // =============================================================================
    
    /**
     * Clase para resumen de factura con cálculos útiles
     */
    public static class InvoiceSummary {
        public long subtotal;
        public long copay;
        public long insuranceCoverage;
        public long patientOwes;
        public double copayPercentage;
        public double insurancePercentage;
        public boolean hasInsurance;
        public boolean insuranceActive;
        public boolean policyExpiringSoon;
        public boolean requiresPayment;
        public boolean fullyInsured;
        public boolean noInsurance;
        
        @Override
        public String toString() {
            return String.format("InvoiceSummary[total=%s, copay=%s, coverage=%s, hasInsurance=%s]",
                String.format("$%,d", subtotal),
                String.format("$%,d", copay),
                String.format("$%,d", insuranceCoverage),
                hasInsurance
            );
        }
    }
    
    /**
     * Clase para reporte de facturación por período
     */
    public static class InvoicingReport {
        public String startDate;
        public String endDate;
        public int totalInvoices;
        public long totalRevenue;
        public long totalCopays;
        public long totalInsuranceCoverage;
        public int invoicesWithInsurance;
        public double averageInvoiceAmount;
        public double averageCopay;
        
        @Override
        public String toString() {
            return String.format("InvoicingReport[period=%s to %s, invoices=%d, revenue=%s, avgAmount=%s]",
                startDate, endDate, totalInvoices,
                String.format("$%,d", totalRevenue),
                String.format("$%,.2f", averageInvoiceAmount)
            );
        }
    }
}