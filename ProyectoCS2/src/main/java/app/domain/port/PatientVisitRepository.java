package app.domain.port;

import java.util.List;

import app.domain.model.PatientVisit;

public interface PatientVisitRepository {
    PatientVisit save(PatientVisit visit);
    List<PatientVisit> findByPatientIdCard(String patientIdCard);
    String nextVisitId();
}