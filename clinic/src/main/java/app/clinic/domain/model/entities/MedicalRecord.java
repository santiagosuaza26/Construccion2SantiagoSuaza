package app.clinic.domain.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalRecord {
    private final String patientIdentificationNumber;
    private final Map<LocalDate, List<MedicalEntry>> typedRecords;

    // Mantener compatibilidad con el formato anterior para migración
    private final Map<LocalDate, Map<String, Object>> legacyRecords;

    public MedicalRecord(String patientIdentificationNumber) {
        this.patientIdentificationNumber = patientIdentificationNumber;
        this.typedRecords = new HashMap<>();
        this.legacyRecords = new HashMap<>();
    }

    public String getPatientIdentificationNumber() {
        return patientIdentificationNumber;
    }

    // Nuevo método con tipado fuerte
    public Map<LocalDate, List<MedicalEntry>> getTypedRecords() {
        return typedRecords;
    }

    // Mantener compatibilidad con código existente
    public Map<LocalDate, Map<String, Object>> getRecords() {
        return legacyRecords;
    }

    // Nuevos métodos con tipado fuerte
    public void addDiagnosisEntry(DiagnosisEntry entry) {
        typedRecords.computeIfAbsent(entry.getDate(), k -> new ArrayList<>()).add(entry);
        // También actualizar legacy para compatibilidad
        updateLegacyRecord(entry.getDate(), entry);
    }

    public void addMedicationEntry(MedicationEntry entry) {
        typedRecords.computeIfAbsent(entry.getDate(), k -> new ArrayList<>()).add(entry);
        updateLegacyRecord(entry.getDate(), entry);
    }

    public void addProcedureEntry(ProcedureEntry entry) {
        typedRecords.computeIfAbsent(entry.getDate(), k -> new ArrayList<>()).add(entry);
        updateLegacyRecord(entry.getDate(), entry);
    }

    public void addDiagnosticAidEntry(DiagnosticAidEntry entry) {
        typedRecords.computeIfAbsent(entry.getDate(), k -> new ArrayList<>()).add(entry);
        updateLegacyRecord(entry.getDate(), entry);
    }

    private void updateLegacyRecord(LocalDate date, MedicalEntry entry) {
        Map<String, Object> record = legacyRecords.computeIfAbsent(date, k -> new HashMap<>());
        if (entry instanceof DiagnosisEntry) {
            DiagnosisEntry de = (DiagnosisEntry) entry;
            record.put("fecha", date);
            record.put("cedula_medico", de.getDoctorId());
            record.put("motivo_consulta", ""); // Mantener estructura legacy
            record.put("sintomatologia", de.getSymptoms());
            record.put("diagnostico", de.getDiagnosis());
        } else if (entry instanceof MedicationEntry) {
            MedicationEntry me = (MedicationEntry) entry;
            Map<String, Object> medication = new HashMap<>();
            medication.put("numero_orden", me.getOrderNumber());
            medication.put("id_medicamento", me.getMedicationId());
            medication.put("dosis", me.getDosage());
            medication.put("duracion_tratamiento", me.getDuration());
            record.put("medicamento", medication);
        } else if (entry instanceof ProcedureEntry) {
            ProcedureEntry pe = (ProcedureEntry) entry;
            Map<String, Object> procedure = new HashMap<>();
            procedure.put("numero_orden", pe.getOrderNumber());
            procedure.put("id_procedimiento", pe.getProcedureId());
            procedure.put("cantidad", pe.getQuantity());
            procedure.put("frecuencia", pe.getFrequency());
            procedure.put("requiere_especialista", pe.isRequiresSpecialist());
            procedure.put("id_tipo_especialidad", pe.getSpecialistId());
            record.put("procedimiento", procedure);
        } else if (entry instanceof DiagnosticAidEntry) {
            DiagnosticAidEntry dae = (DiagnosticAidEntry) entry;
            Map<String, Object> aid = new HashMap<>();
            aid.put("numero_orden", dae.getOrderNumber());
            aid.put("id_ayuda_diagnostica", dae.getDiagnosticAidId());
            aid.put("cantidad", dae.getQuantity());
            aid.put("requiere_especialista", dae.isRequiresSpecialist());
            aid.put("id_tipo_especialidad", dae.getSpecialistId());
            record.put("ayuda_diagnostica", aid);
        }
    }

    // Métodos legacy para compatibilidad (marcados como deprecated)
    @Deprecated
    public void addRecord(LocalDate date, String doctorIdentificationNumber, String reason, String symptoms, String diagnosis) {
        DiagnosisEntry entry = new DiagnosisEntry(date, doctorIdentificationNumber, diagnosis, symptoms);
        addDiagnosisEntry(entry);
    }

    @Deprecated
    public void addMedicationToRecord(LocalDate date, String orderNumber, String medicationId, String dosage, String duration) {
        MedicationEntry entry = new MedicationEntry(date, "", orderNumber, medicationId, dosage, duration);
        addMedicationEntry(entry);
    }

    @Deprecated
    public void addProcedureToRecord(LocalDate date, String orderNumber, String procedureId, String quantity, String frequency, boolean requiresSpecialist, String specialistId) {
        ProcedureEntry entry = new ProcedureEntry(date, "", orderNumber, procedureId, quantity, frequency, requiresSpecialist, specialistId);
        addProcedureEntry(entry);
    }

    @Deprecated
    public void addDiagnosticAidToRecord(LocalDate date, String orderNumber, String diagnosticAidId, String quantity, boolean requiresSpecialist, String specialistId) {
        DiagnosticAidEntry entry = new DiagnosticAidEntry(date, "", orderNumber, diagnosticAidId, quantity, requiresSpecialist, specialistId);
        addDiagnosticAidEntry(entry);
    }

    @Deprecated
    public void addOrderToRecord(LocalDate date, String orderNumber, String diagnosis) {
        if (diagnosis != null && !diagnosis.trim().isEmpty()) {
            DiagnosisEntry entry = new DiagnosisEntry(date, "", diagnosis, "");
            addDiagnosisEntry(entry);
        }
        // Para order number, se podría agregar a una entrada separada si es necesario
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return patientIdentificationNumber.equals(that.patientIdentificationNumber);
    }

    @Override
    public int hashCode() {
        return patientIdentificationNumber.hashCode();
    }

    @Override
    public String toString() {
        return "MedicalRecord for " + patientIdentificationNumber;
    }
}