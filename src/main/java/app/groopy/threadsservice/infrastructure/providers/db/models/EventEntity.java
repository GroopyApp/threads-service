package app.groopy.threadsservice.infrastructure.providers.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("events")
public class EventEntity extends Entity {
    @DocumentReference(lazy = true)
    List<ThreadEntity> threads;
}
