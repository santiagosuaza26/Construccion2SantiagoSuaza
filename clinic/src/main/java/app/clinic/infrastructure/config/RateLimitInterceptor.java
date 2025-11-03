package app.clinic.infrastructure.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    @Qualifier("generalBucket")
    private Bucket generalBucket;

    @Autowired
    @Qualifier("authBucket")
    private Bucket authBucket;

    @Autowired
    @Qualifier("userManagementBucket")
    private Bucket userManagementBucket;

    @Autowired
    @Qualifier("patientManagementBucket")
    private Bucket patientManagementBucket;

    @Autowired
    @Qualifier("medicalOperationsBucket")
    private Bucket medicalOperationsBucket;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        Bucket bucket;

        String uri = request.getRequestURI();

        // Seleccionar bucket según el tipo de operación
        if (uri.startsWith("/api/auth")) {
            bucket = authBucket;
        } else if (uri.startsWith("/api/users")) {
            bucket = userManagementBucket;
        } else if (uri.startsWith("/api/patients")) {
            bucket = patientManagementBucket;
        } else if (uri.startsWith("/api/medical") || uri.startsWith("/api/nurse") || uri.startsWith("/api/billing")) {
            bucket = medicalOperationsBucket;
        } else {
            bucket = generalBucket;
        }

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // Agregar headers de rate limit
            response.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            response.setHeader("X-Rate-Limit-Retry-After-Seconds",
                String.valueOf(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())));
            return true;
        } else {
            // Rate limit excedido
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Try again later.\",\"retryAfter\":" +
                TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()) + "}");
            return false;
        }
    }
}