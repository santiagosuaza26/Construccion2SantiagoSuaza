package app.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.application.usecase.RecordVitalSignsUseCase;
import app.domain.model.VitalSigns;

@RestController
@RequestMapping("/api/nurse")
public class NurseController {
    private final RecordVitalSignsUseCase recordVitalSignsUseCase;

    public NurseController(RecordVitalSignsUseCase recordVitalSignsUseCase) {
        this.recordVitalSignsUseCase = recordVitalSignsUseCase;
    }

    @PostMapping("/patients/{patientId}/vitals")
    public ResponseEntity<Void> recordVitalSigns(@PathVariable String patientId,
                                                @RequestParam String nurseId,
                                                @RequestBody VitalSigns signs) {
        recordVitalSignsUseCase.execute(patientId, nurseId, signs);
        return ResponseEntity.ok().build();
    }
}