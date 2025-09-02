package app.domain.services;

import java.time.LocalDate;
import java.util.List;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderHeader;
import app.domain.model.ProcedureOrderItem;
import app.domain.port.ClinicalHistoryRepository;
import app.domain.port.DiagnosticTestRepository;
import app.domain.port.MedicationRepository;
import app.domain.port.OrderHeaderRepository;
import app.domain.port.OrderItemRepository;
import app.domain.port.ProcedureTypeRepository;

public class DoctorService {
    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final MedicationRepository medicationRepository;
    private final ProcedureTypeRepository procedureTypeRepository;
    private final DiagnosticTestRepository diagnosticTestRepository;

    public DoctorService(OrderHeaderRepository orderHeaderRepository,
                        OrderItemRepository orderItemRepository,
                        ClinicalHistoryRepository clinicalHistoryRepository,
                        MedicationRepository medicationRepository,
                        ProcedureTypeRepository procedureTypeRepository,
                        DiagnosticTestRepository diagnosticTestRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
        this.orderItemRepository = orderItemRepository;
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.medicationRepository = medicationRepository;
        this.procedureTypeRepository = procedureTypeRepository;
        this.diagnosticTestRepository = diagnosticTestRepository;
    }

    public OrderHeader createOrderHeader(String orderNumber, String patientIdCard, String doctorIdCard, LocalDate date) {
        if (orderHeaderRepository.existsOrderNumber(orderNumber)) {
            throw new IllegalArgumentException("Order number must be unique");
        }
        OrderHeader header = new OrderHeader(orderNumber, patientIdCard, doctorIdCard, date);
        return orderHeaderRepository.save(header);
    }

    public void addMedicationItems(String orderNumber, List<MedicationOrderItem> items) {
        ensureNoDuplicateItemNumbers(items.stream().map(MedicationOrderItem::getItemNumber).toList());
        for (MedicationOrderItem item : items) {
            medicationRepository.findById(item.getMedicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medication not found: " + item.getMedicationId()));
            orderItemRepository.saveMedicationItem(item);
        }
    }

    public void addProcedureItems(String orderNumber, List<ProcedureOrderItem> items) {
        ensureNoDuplicateItemNumbers(items.stream().map(ProcedureOrderItem::getItemNumber).toList());
        for (ProcedureOrderItem item : items) {
            procedureTypeRepository.findById(item.getProcedureId())
                .orElseThrow(() -> new IllegalArgumentException("Procedure not found: " + item.getProcedureId()));
            orderItemRepository.saveProcedureItem(item);
        }
    }

    public void addDiagnosticItems(String orderNumber, List<DiagnosticOrderItem> items) {
        ensureNoDuplicateItemNumbers(items.stream().map(DiagnosticOrderItem::getItemNumber).toList());
        for (DiagnosticOrderItem item : items) {
            diagnosticTestRepository.findById(item.getDiagnosticId())
                .orElseThrow(() -> new IllegalArgumentException("Diagnostic test not found: " + item.getDiagnosticId()));
            orderItemRepository.saveDiagnosticItem(item);
        }
    }

    public void appendClinicalHistory(String patientIdCard, ClinicalHistoryEntry entry) {
        clinicalHistoryRepository.saveEntry(patientIdCard, entry);
    }

    private void ensureNoDuplicateItemNumbers(List<Integer> itemNumbers) {
        long distinct = itemNumbers.stream().distinct().count();
        if (distinct != itemNumbers.size()) {
            throw new IllegalArgumentException("Item number must be unique within the same order");
        }
    }
}