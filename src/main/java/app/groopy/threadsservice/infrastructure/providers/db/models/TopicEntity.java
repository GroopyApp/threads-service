package app.groopy.threadsservice.infrastructure.providers.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("topic")
public class TopicEntity extends Entity {
    @DocumentReference(lazy = true)
    List<ThreadEntity> threads;
    @DocumentReference
    @Field("associatedEvents")
    List<EventEntity> events;
}
