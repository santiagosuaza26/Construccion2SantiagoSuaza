package app.clinic.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.MedicalRecordDocument;

/**
 * MongoDB repository interface for medical record documents.
 * Provides CRUD operations for unstructured clinical history storage.
 */
@Repository
public interface MedicalRecordMongoRepository extends MongoRepository<MedicalRecordDocument, String> {

    /**
     * Finds a medical record document by patient national ID.
     */
    Optional<MedicalRecordDocument> findByPatientNationalId(String patientNationalId);

    /**
     * Checks if a medical record exists for a patient.
     */
    boolean existsByPatientNationalId(String patientNationalId);

    /**
     * Deletes a medical record by patient national ID.
     */
    void deleteByPatientNationalId(String patientNationalId);

    /**
     * Finds medical records by doctor national ID in any record entry.
     */
    @Query("{ 'records.doctorNationalId': ?0 }")
    List<MedicalRecordDocument> findByDoctorNationalId(String doctorNationalId);

    /**
     * Finds medical records within a date range.
     */
    @Query("{ 'records.date': { $gte: ?0, $lte: ?1 } }")
    List<MedicalRecordDocument> findByDateRange(String startDate, String endDate);

    /**
     * Counts medical records by patient national ID pattern.
     */
    long countByPatientNationalIdRegex(String pattern);
}