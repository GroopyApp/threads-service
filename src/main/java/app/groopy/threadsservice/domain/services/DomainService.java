package app.groopy.threadsservice.domain.services;

import app.groopy.threadsservice.application.mapper.ApplicationMapper;
import app.groopy.threadsservice.domain.models.requests.PostThreadRequestDto;
import app.groopy.threadsservice.domain.models.responses.PostThreadResponseDto;
import app.groopy.threadsservice.infrastructure.models.PostThreadRequest;
import app.groopy.threadsservice.infrastructure.models.PublishThreadRequest;
import app.groopy.threadsservice.infrastructure.providers.exceptions.WallServiceException;
import app.groopy.threadsservice.infrastructure.repository.ThreadsProviderRepository;
import app.groopy.threadsservice.infrastructure.repository.WallProviderRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

@Component
public class DomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainService.class);

    private final ApplicationMapper applicationMapper;
    private final ThreadsProviderRepository threadsProviderRepository;
    private final WallProviderRepository wallProviderRepository;


    @Autowired
    public DomainService(ApplicationMapper applicationMapper,
                         ThreadsProviderRepository threadsProviderRepository,
                         WallProviderRepository wallProviderRepository) {
        this.applicationMapper = applicationMapper;
        this.threadsProviderRepository = threadsProviderRepository;
        this.wallProviderRepository = wallProviderRepository;
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
        if (!StringUtils.hasText(request.getParentId())) {
            try {
                wallProviderRepository.publishThread(PublishThreadRequest.builder()
                        .topicId(request.getTopicId())
                        .eventId(request.getEventId())
                        .threadId(result.getThreadId())
                        .build());

            } catch (WallServiceException e) {
                LOGGER.error("An error occurred trying to publish new thread. Rolling back operation...");
                threadsProviderRepository.removeThread(result.getThreadId());
                throw e;
            }
        }
        return PostThreadResponseDto.builder()
                .threadId(result.getThreadId())
                .build();
    }
}
