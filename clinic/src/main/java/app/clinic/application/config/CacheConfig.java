package app.clinic.application.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configuration class for Redis caching in the clinic management system.
 * Provides centralized cache management with custom configurations for different domain entities.
 * Implements performance optimization through intelligent caching strategies.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.database:0}")
    private int redisDatabase;

    /**
     * Redis connection factory configuration.
     * Creates connection to Redis server with authentication and database selection.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        if (!redisPassword.isEmpty()) {
            factory.setPassword(redisPassword);
        }
        factory.setDatabase(redisDatabase);
        return factory;
    }

    /**
     * Redis template for low-level Redis operations.
     * Configured with JSON serialization for complex objects.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Configure serializers
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Cache manager with custom configurations for different cache types.
     * Implements different TTL strategies based on data access patterns.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Patient data - moderate volatility, cache for 5 minutes
        RedisCacheConfiguration patientsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues();

        // Appointment data - high volatility, cache for 3 minutes
        RedisCacheConfiguration appointmentsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(3))
                .disableCachingNullValues();

        // Medical records - low volatility, cache for 15 minutes
        RedisCacheConfiguration medicalRecordsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15))
                .disableCachingNullValues();

        // Billing data - moderate volatility, cache for 2 minutes
        RedisCacheConfiguration billingCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(2))
                .disableCachingNullValues();

        // User data - low volatility, cache for 10 minutes
        RedisCacheConfiguration usersCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();

        // Inventory data - moderate volatility, cache for 5 minutes
        RedisCacheConfiguration inventoryCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration("patients", patientsCacheConfig)
                .withCacheConfiguration("appointments", appointmentsCacheConfig)
                .withCacheConfiguration("medical-records", medicalRecordsCacheConfig)
                .withCacheConfiguration("billing", billingCacheConfig)
                .withCacheConfiguration("users", usersCacheConfig)
                .withCacheConfiguration("inventory", inventoryCacheConfig)
                .build();
    }
}