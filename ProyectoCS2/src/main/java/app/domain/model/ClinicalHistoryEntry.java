package app.domain.model;

import java.time.LocalDate;
import java.util.List;

public class ClinicalHistoryEntry {
    private final LocalDate date;
    private final String doctorIdCard;
    private final String reason;
    private final String symptoms;
    private final String diagnosis;
    private final VitalSigns vitalSigns;
    private final List<String> relatedOrderNumbers;

    public ClinicalHistoryEntry(LocalDate date, String doctorIdCard,
                                String reason, String symptoms, String diagnosis,
                                VitalSigns vitalSigns, List<String> relatedOrderNumbers) {
        this.date = date;
        this.doctorIdCard = doctorIdCard;
        this.reason = reason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.vitalSigns = vitalSigns;
        this.relatedOrderNumbers = relatedOrderNumbers;
        validate();
    }

    public LocalDate getDate() { return date; }
    public String getDoctorIdCard() { return doctorIdCard; }
    public String getReason() { return reason; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public VitalSigns getVitalSigns() { return vitalSigns; }
    public List<String> getRelatedOrderNumbers() { return relatedOrderNumbers; }

    private void validate() {
        if (date == null) throw new IllegalArgumentException("Date required");
        if (doctorIdCard == null || !doctorIdCard.matches("\\d{1,10}")) throw new IllegalArgumentException("Invalid doctor id");
    }
}