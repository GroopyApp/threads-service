package app.groopy.threadsservice.infrastructure.providers.db;

import app.groopy.threadsservice.infrastructure.providers.db.models.EventEntity;
import app.groopy.threadsservice.infrastructure.providers.db.models.ThreadEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<EventEntity, String> {

}
