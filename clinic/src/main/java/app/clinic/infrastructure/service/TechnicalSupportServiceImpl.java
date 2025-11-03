package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.SupportTicketRepository;
import app.clinic.domain.service.RoleBasedAccessService;
import app.clinic.domain.service.TechnicalSupportService;

@Service
public class TechnicalSupportServiceImpl extends TechnicalSupportService {

    public TechnicalSupportServiceImpl(SupportTicketRepository supportTicketRepository, RoleBasedAccessService roleBasedAccessService) {
        super(supportTicketRepository, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}