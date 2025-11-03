package app.clinic.infrastructure.persistence.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile("mongodb")
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    public MongoIndexConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void createIndexes() {
        // Crear índice en patientId para búsquedas rápidas
        mongoTemplate.indexOps("medical_records")
            .createIndex(new Index().on("patientId", org.springframework.data.domain.Sort.Direction.ASC));

        System.out.println("MongoDB indexes created for medical_records collection");
    }
}