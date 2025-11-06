package app.clinic.infrastructure.persistence.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClients;

@Configuration
@Profile("mongodb")
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "clinic_db");
    }
}