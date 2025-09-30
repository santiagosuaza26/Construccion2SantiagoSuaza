package app.infrastructure.adapter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.port.ClinicalHistoryRepository;

/**
 * Adaptador para repositorio de historia clínica - Implementación en memoria
 *
 * Esta implementación funciona cuando MongoDB no está disponible,
 * almacenando los datos en memoria para desarrollo y pruebas.
 */
@Component
public class ClinicalHistoryRepositoryAdapter implements ClinicalHistoryRepository {

    // Almacenamiento en memoria para desarrollo cuando MongoDB no está disponible
    private final ConcurrentHashMap<String, List<ClinicalHistoryEntry>> clinicalHistoryStorage = new ConcurrentHashMap<>();

    @Override
    public void saveEntry(String patientIdCard, ClinicalHistoryEntry entry) {
        // Obtener lista existente o crear nueva
        List<ClinicalHistoryEntry> entries = clinicalHistoryStorage.computeIfAbsent(
            patientIdCard, k -> new CopyOnWriteArrayList<>()
        );

        // Agregar nueva entrada
        entries.add(entry);

        System.out.println("Historia clínica guardada en memoria para paciente: " + patientIdCard);
    }

    @Override
    public List<ClinicalHistoryEntry> findByPatient(String patientIdCard) {
        return clinicalHistoryStorage.getOrDefault(patientIdCard, new CopyOnWriteArrayList<>());
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
        return findByPatient(patientIdCard).stream()
            .filter(entry -> entry.getDoctorIdCard().equals(doctorIdCard))
            .collect(Collectors.toList());
    }

    /**
     * Buscar entradas relacionadas con un número de orden
     */
    public List<ClinicalHistoryEntry> findByRelatedOrderNumber(String orderNumber) {
        return clinicalHistoryStorage.values().stream()
            .flatMap(List::stream)
            .filter(entry -> entry.getRelatedOrderNumbers().contains(orderNumber))
            .collect(Collectors.toList());
    }

    /**
     * Verificar si existe historia clínica para un paciente
     */
    public boolean existsByPatientIdCard(String patientIdCard) {
        return clinicalHistoryStorage.containsKey(patientIdCard);
    }

    /**
     * Contar total de entradas para un paciente
     */
    public long countEntriesByPatient(String patientIdCard) {
        return findByPatient(patientIdCard).size();
    }

    /**
     * Buscar pacientes por diagnóstico
     */
    public List<String> findPatientsByDiagnosis(String diagnosis) {
        return clinicalHistoryStorage.entrySet().stream()
            .filter(entry -> entry.getValue().stream()
                .anyMatch(e -> e.getDiagnosis().toLowerCase().contains(diagnosis.toLowerCase())))
            .map(entry -> entry.getKey())
            .collect(Collectors.toList());
    }

    /**
     * Buscar entradas por síntomas
     */
    public List<ClinicalHistoryEntry> findBySymptoms(String symptoms) {
        return clinicalHistoryStorage.values().stream()
            .flatMap(List::stream)
            .filter(entry -> entry.getSymptoms() != null &&
                           entry.getSymptoms().toLowerCase().contains(symptoms.toLowerCase()))
            .collect(Collectors.toList());
    }

    /**
     * Eliminar historia clínica completa de un paciente
     */
    public void deleteByPatientIdCard(String patientIdCard) {
        clinicalHistoryStorage.remove(patientIdCard);
    }

    /**
     * Buscar entradas en un rango de fechas
     */
    public List<ClinicalHistoryEntry> findByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return clinicalHistoryStorage.values().stream()
            .flatMap(List::stream)
            .filter(entry -> !entry.getDate().isBefore(startDate) && !entry.getDate().isAfter(endDate))
            .collect(Collectors.toList());
    }
}