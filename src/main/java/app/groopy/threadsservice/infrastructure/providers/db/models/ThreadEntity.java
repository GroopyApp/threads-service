package app.groopy.threadsservice.infrastructure.providers.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("threads")
public class ThreadEntity extends Entity {
    String userId;
    String parentId;
    String body;
    String imageUrl;
    byte[] audioRecord;
    long likes;
    LocalDateTime dateTime;
    List<ThreadEntity> comments;
}
