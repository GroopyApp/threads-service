package app.groopy.threadsservice.domain.models.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentDto {
    String body;
    String imageUrl;
    Object audioRecord;
    long likes;
}
