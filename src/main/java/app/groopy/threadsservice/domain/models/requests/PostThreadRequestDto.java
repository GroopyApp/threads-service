package app.groopy.threadsservice.domain.models.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostThreadRequestDto {
    String userId;
    String parentId;
    String topicId;
    String eventId;
    ContentDto content;
}
