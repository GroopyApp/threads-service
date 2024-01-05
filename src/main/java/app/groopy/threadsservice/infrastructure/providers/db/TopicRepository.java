package app.groopy.threadsservice.infrastructure.providers.db;

import app.groopy.threadsservice.infrastructure.providers.db.models.ThreadEntity;
import app.groopy.threadsservice.infrastructure.providers.db.models.TopicEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<TopicEntity, String> {

}
