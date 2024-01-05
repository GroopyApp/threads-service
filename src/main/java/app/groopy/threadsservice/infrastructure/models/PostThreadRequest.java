package app.groopy.threadsservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostThreadRequest {
    String userId;
    String parentId;
    String topicId;
    String eventId;
    String body;
    String imageUrl;
    byte[] audioRecord;
}
