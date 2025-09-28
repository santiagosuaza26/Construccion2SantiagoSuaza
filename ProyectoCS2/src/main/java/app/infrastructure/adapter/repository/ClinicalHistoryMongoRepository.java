package app.infrastructure.adapter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import app.infrastructure.adapter.entity.ClinicalHistoryDocument;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio MongoDB para historia clínica NoSQL
 *
 * Implementa el patrón Repository para acceso a datos NoSQL
 * siguiendo los requerimientos del documento:
 * - Clave = cédula paciente
 * - Subclave = fecha de atención
 */
@Repository
public interface ClinicalHistoryMongoRepository extends MongoRepository<ClinicalHistoryDocument, String> {

    /**
     * Buscar historia clínica completa por cédula del paciente
     */
    Optional<ClinicalHistoryDocument> findByPatientIdCard(String patientIdCard);

    /**
     * Buscar historias clínicas que contengan entradas en un rango de fechas
     */
    @Query("{ 'entries.date' : { $gte: ?0, $lte: ?1 } }")
    List<ClinicalHistoryDocument> findByEntriesDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);

    /**
     * Buscar historias clínicas por paciente y médico
     */
    @Query("{ 'patientIdCard': ?0, 'entries.doctorIdCard': ?1 }")
    List<ClinicalHistoryDocument> findByPatientIdCardAndDoctorIdCard(String patientIdCard, String doctorIdCard);

    /**
     * Buscar entradas específicas por número de orden relacionado
     */
    @Query("{ 'entries.relatedOrderNumbers': ?0 }")
    List<ClinicalHistoryDocument> findByRelatedOrderNumber(String orderNumber);

    /**
     * Verificar si existe historia clínica para un paciente
     */
    boolean existsByPatientIdCard(String patientIdCard);

    /**
     * Eliminar historia clínica por cédula del paciente
     */
    void deleteByPatientIdCard(String patientIdCard);

    /**
     * Buscar pacientes por diagnóstico
     */
    @Query("{ 'entries.diagnosis': { $regex: ?0, $options: 'i' } }")
    List<ClinicalHistoryDocument> findByDiagnosisContainingIgnoreCase(String diagnosis);

    /**
     * Buscar entradas por síntomas
     */
    @Query("{ 'entries.symptoms': { $regex: ?0, $options: 'i' } }")
    List<ClinicalHistoryDocument> findBySymptomsContainingIgnoreCase(String symptoms);

    /**
     * Contar total de entradas para un paciente
     */
    @Query(value = "{ 'patientIdCard': ?0 }", count = true)
    long countEntriesByPatientIdCard(String patientIdCard);
}