package app.infrastructure.adapter.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.VitalSigns;
import app.infrastructure.adapter.entity.ClinicalHistoryDocument.ClinicalHistoryEntryDocument;
import app.infrastructure.adapter.entity.ClinicalHistoryDocument.VitalSignsDocument;

/**
 * Mapper para conversión entre entidades de dominio y documentos MongoDB
 * para historia clínica NoSQL
 */
@Component
public class ClinicalHistoryMapper {

    /**
     * Convertir entrada de dominio a documento MongoDB
     */
    public ClinicalHistoryEntryDocument toDocument(ClinicalHistoryEntry domainEntry) {
        if (domainEntry == null) {
            return null;
        }

        VitalSignsDocument vitalSignsDoc = null;
        if (domainEntry.getVitalSigns() != null) {
            VitalSigns vitalSigns = domainEntry.getVitalSigns();
            vitalSignsDoc = new VitalSignsDocument(
                vitalSigns.getBloodPressure(),
                vitalSigns.getTemperature(),
                vitalSigns.getPulse(),
                vitalSigns.getOxygenLevel(),
                null, // weight - no está en el modelo de dominio actual
                null  // height - no está en el modelo de dominio actual
            );
        }

        return new ClinicalHistoryEntryDocument(
            domainEntry.getDate(),
            domainEntry.getDoctorIdCard(),
            "", // doctorName - no está en el modelo de dominio actual
            domainEntry.getReason(),
            domainEntry.getSymptoms(),
            domainEntry.getDiagnosis(),
            vitalSignsDoc,
            domainEntry.getRelatedOrderNumbers(),
            "", // observations - no está en el modelo de dominio actual
            "", // treatment - no está en el modelo de dominio actual
            List.of() // medications - no está en el modelo de dominio actual
        );
    }

    /**
     * Convertir documento MongoDB a entrada de dominio
     */
    public ClinicalHistoryEntry toDomain(ClinicalHistoryEntryDocument documentEntry) {
        if (documentEntry == null) {
            return null;
        }

        VitalSigns vitalSigns = null;
        if (documentEntry.getVitalSigns() != null) {
            VitalSignsDocument vsDoc = documentEntry.getVitalSigns();
            vitalSigns = new VitalSigns(
                vsDoc.getBloodPressure(),
                vsDoc.getTemperature(),
                vsDoc.getPulse(),
                vsDoc.getOxygenLevel()
            );
        }

        return new ClinicalHistoryEntry(
            documentEntry.getDate(),
            documentEntry.getDoctorIdCard(),
            documentEntry.getReason(),
            documentEntry.getSymptoms(),
            documentEntry.getDiagnosis(),
            vitalSigns,
            documentEntry.getRelatedOrderNumbers()
        );
    }

    /**
     * Convertir lista de documentos a lista de entidades de dominio
     */
    public List<ClinicalHistoryEntry> toDomainList(List<ClinicalHistoryEntryDocument> documentEntries) {
        if (documentEntries == null) {
            return List.of();
        }

        return documentEntries.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    /**
     * Convertir lista de entidades de dominio a lista de documentos
     */
    public List<ClinicalHistoryEntryDocument> toDocumentList(List<ClinicalHistoryEntry> domainEntries) {
        if (domainEntries == null) {
            return List.of();
        }

        return domainEntries.stream()
            .map(this::toDocument)
            .collect(Collectors.toList());
    }
}