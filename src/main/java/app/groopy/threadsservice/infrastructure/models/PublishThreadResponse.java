package app.groopy.threadsservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishThreadResponse {
    String threadId;
}
