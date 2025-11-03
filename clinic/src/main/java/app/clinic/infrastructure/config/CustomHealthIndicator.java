package app.clinic.infrastructure.config;

import java.time.Duration;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public CustomHealthIndicator(JdbcTemplate jdbcTemplate, RedisTemplate<String, String> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    // Constructor for development without Redis
    public CustomHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = null;
    }

    // Default constructor for Spring
    public CustomHealthIndicator() {
        this.jdbcTemplate = null;
        this.redisTemplate = null;
    }

    @Override
    public Health health() {
        try {
            // Check database connectivity
            jdbcTemplate.execute("SELECT 1");

            // Check Redis connectivity only if available
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set("health-check", "ok", Duration.ofSeconds(10));
                String value = redisTemplate.opsForValue().get("health-check");

                if ("ok".equals(value)) {
                    return Health.up()
                        .withDetail("database", "available")
                        .withDetail("redis", "available")
                        .build();
                } else {
                    return Health.down()
                        .withDetail("database", "available")
                        .withDetail("redis", "unavailable")
                        .build();
                }
            } else {
                // Redis not available (development mode)
                return Health.up()
                    .withDetail("database", "available")
                    .withDetail("redis", "not configured")
                    .build();
            }

        } catch (Exception e) {
            return Health.down(e)
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}