package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.SupportTicket;
import app.clinic.domain.service.TechnicalSupportService;

@Service
public class ProvideTechnicalSupportUseCase {
    private final TechnicalSupportService technicalSupportService;

    public ProvideTechnicalSupportUseCase(TechnicalSupportService technicalSupportService) {
        this.technicalSupportService = technicalSupportService;
    }

    public SupportTicket execute(String userId, String issueDescription) {
        // Create and save the support ticket
        SupportTicket supportTicket = technicalSupportService.createSupportTicket(userId, issueDescription);

        // Log the support request with tracking number
        System.out.println("Support ticket created - ID: " + supportTicket.getId().getValue() +
                          ", User: " + userId + ", Issue: " + issueDescription);

        // In a real implementation, this would also:
        // - Send notification to support team
        // - Send confirmation email to user

        return supportTicket;
    }
}