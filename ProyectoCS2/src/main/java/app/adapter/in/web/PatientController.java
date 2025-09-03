package app.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.dto.PatientRequest;
import app.application.dto.PatientResponse;
import app.application.usecase.DeletePatientUseCase;
import app.application.usecase.RegisterPatientUseCase;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final RegisterPatientUseCase registerPatientUseCase;
    private final DeletePatientUseCase deletePatientUseCase;

    public PatientController(RegisterPatientUseCase registerPatientUseCase, DeletePatientUseCase deletePatientUseCase) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.deletePatientUseCase = deletePatientUseCase;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> registerPatient(@RequestBody PatientRequest request) {
        try {
            PatientResponse response = registerPatientUseCase.execute(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idCard}")
    public ResponseEntity<Void> deletePatient(@PathVariable String idCard) {
        deletePatientUseCase.execute(idCard);
        return ResponseEntity.noContent().build();
    }
}