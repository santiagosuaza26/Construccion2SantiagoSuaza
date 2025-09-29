package app.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.port.ClinicalHistoryRepository;
import app.infrastructure.adapter.entity.ClinicalHistoryDocument;
import app.infrastructure.adapter.entity.ClinicalHistoryDocument.ClinicalHistoryEntryDocument;
import app.infrastructure.adapter.mapper.ClinicalHistoryMapper;
import app.infrastructure.adapter.repository.ClinicalHistoryMongoRepository;

/**
 * Adaptador para repositorio de historia clínica NoSQL (MongoDB)
 *
 * Implementa el puerto del dominio para acceso a datos NoSQL
 * siguiendo los requerimientos del documento:
 * - Clave = cédula paciente
 * - Subclave = fecha de atención
 * - Datos: motivo, diagnóstico, tratamientos, medicamentos, etc.
 */
@Component
public class ClinicalHistoryRepositoryAdapter implements ClinicalHistoryRepository {

    private final ClinicalHistoryMongoRepository mongoRepository;
    private final ClinicalHistoryMapper mapper;

    public ClinicalHistoryRepositoryAdapter(ClinicalHistoryMongoRepository mongoRepository,
                                            ClinicalHistoryMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public void saveEntry(String patientIdCard, ClinicalHistoryEntry entry) {
        // Buscar documento existente o crear nuevo
        ClinicalHistoryDocument document = mongoRepository.findByPatientIdCard(patientIdCard)
            .orElse(new ClinicalHistoryDocument(patientIdCard, "", List.of()));

        // Convertir entrada de dominio a documento
        ClinicalHistoryEntryDocument entryDocument = mapper.toDocument(entry);

        // Agregar nueva entrada
        List<ClinicalHistoryEntryDocument> entries = document.getEntries();
        entries.add(entryDocument);

        // Guardar documento actualizado
        mongoRepository.save(document);
    }

    @Override
    public List<ClinicalHistoryEntry> findByPatient(String patientIdCard) {
        Optional<ClinicalHistoryDocument> document = mongoRepository.findByPatientIdCard(patientIdCard);

        if (document.isEmpty()) {
            return List.of();
        }

        return document.get().getEntries().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    /**
     * Buscar entradas por paciente y fecha específica
     */
    public List<ClinicalHistoryEntry> findByPatientAndDate(String patientIdCard, java.time.LocalDate date) {
        return findByPatient(patientIdCard).stream()
            .filter(entry -> entry.getDate().equals(date))
            .collect(Collectors.toList());
    }

    /**
     * Buscar entradas por paciente y médico
     */
    public List<ClinicalHistoryEntry> findByPatientAndDoctor(String patientIdCard, String doctorIdCard) {
        Optional<ClinicalHistoryDocument> document = mongoRepository.findByPatientIdCard(patientIdCard);

        if (document.isEmpty()) {
            return List.of();
        }

        return document.get().getEntries().stream()
            .filter(entry -> entry.getDoctorIdCard().equals(doctorIdCard))
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    /**
     * Buscar entradas relacionadas con un número de orden
     */
    public List<ClinicalHistoryEntry> findByRelatedOrderNumber(String orderNumber) {
        List<ClinicalHistoryDocument> documents = mongoRepository.findByRelatedOrderNumber(orderNumber);

        return documents.stream()
            .flatMap(doc -> doc.getEntries().stream())
            .filter(entry -> entry.getRelatedOrderNumbers().contains(orderNumber))
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    /**
     * Verificar si existe historia clínica para un paciente
     */
    public boolean existsByPatientIdCard(String patientIdCard) {
        return mongoRepository.existsByPatientIdCard(patientIdCard);
    }

    /**
     * Contar total de entradas para un paciente
     */
    public long countEntriesByPatient(String patientIdCard) {
        return mongoRepository.countEntriesByPatientIdCard(patientIdCard);
    }

    /**
     * Buscar pacientes por diagnóstico
     */
    public List<String> findPatientsByDiagnosis(String diagnosis) {
        List<ClinicalHistoryDocument> documents = mongoRepository.findByDiagnosisContainingIgnoreCase(diagnosis);

        return documents.stream()
            .map(ClinicalHistoryDocument::getPatientIdCard)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * Buscar entradas por síntomas
     */
    public List<ClinicalHistoryEntry> findBySymptoms(String symptoms) {
        List<ClinicalHistoryDocument> documents = mongoRepository.findBySymptomsContainingIgnoreCase(symptoms);

        return documents.stream()
            .flatMap(doc -> doc.getEntries().stream())
            .filter(entry -> entry.getSymptoms() != null &&
                           entry.getSymptoms().toLowerCase().contains(symptoms.toLowerCase()))
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    /**
     * Eliminar historia clínica completa de un paciente
     */
    public void deleteByPatientIdCard(String patientIdCard) {
        mongoRepository.deleteByPatientIdCard(patientIdCard);
    }

    /**
     * Buscar entradas en un rango de fechas
     */
    public List<ClinicalHistoryEntry> findByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        List<ClinicalHistoryDocument> documents = mongoRepository.findByEntriesDateBetween(startDate, endDate);

        return documents.stream()
            .flatMap(doc -> doc.getEntries().stream())
            .filter(entry -> !entry.getDate().isBefore(startDate) && !entry.getDate().isAfter(endDate))
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
}