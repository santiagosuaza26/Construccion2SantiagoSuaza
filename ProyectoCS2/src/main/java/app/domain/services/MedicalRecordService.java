package app.domain.services;

import app.domain.model.MedicalRecord;

public class MedicalRecordService {

    public void addRecord(MedicalRecord record, String date, String doctorId, String notes) {
        record.getEntries().put(date, notes + " | Doctor: " + doctorId);
    }
}