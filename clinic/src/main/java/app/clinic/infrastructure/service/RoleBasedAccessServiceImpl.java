package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.RoleBasedAccessService;

@Service
public class RoleBasedAccessServiceImpl extends RoleBasedAccessService {

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}