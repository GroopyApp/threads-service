package app.groopy.threadsservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishThreadRequest {
    String topicId;
    String eventId;
    String threadId;
}
