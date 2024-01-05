package app.groopy.threadsservice.domain.services;

import app.groopy.threadsservice.application.mapper.ApplicationMapper;
import app.groopy.threadsservice.domain.models.requests.PostThreadRequestDto;
import app.groopy.threadsservice.domain.models.responses.PostThreadResponseDto;
import app.groopy.threadsservice.infrastructure.models.PostThreadRequest;
import app.groopy.threadsservice.infrastructure.repository.ThreadsProviderRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
public class DomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainService.class);

    private final ApplicationMapper applicationMapper;
    private final ThreadsProviderRepository threadsProviderRepository;

    @Autowired
    public DomainService(ApplicationMapper applicationMapper, ThreadsProviderRepository threadsProviderRepository) {
        this.applicationMapper = applicationMapper;
        this.threadsProviderRepository = threadsProviderRepository;
    }

    @SneakyThrows
    public PostThreadResponseDto postThread(PostThreadRequestDto request) {
        LOGGER.info("trying to post a thread: {}", request);
        var result = threadsProviderRepository.postThread(PostThreadRequest.builder()
                        .topicId(request.getTopicId())
                        .eventId(request.getEventId())
                        .parentId(request.getParentId())
                        .userId(request.getUserId())
                        .body(request.getContent().getBody())
                        .imageUrl(request.getContent().getImageUrl())
                        .audioRecord(SerializationUtils.serialize(request.getContent().getAudioRecord()))
                .build());
        return PostThreadResponseDto.builder()
                .threadId(result.getThreadId())
                .build();
    }
}
