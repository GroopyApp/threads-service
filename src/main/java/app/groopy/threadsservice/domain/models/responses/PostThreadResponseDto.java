package app.groopy.threadsservice.domain.models.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostThreadResponseDto {
    String threadId;
}
