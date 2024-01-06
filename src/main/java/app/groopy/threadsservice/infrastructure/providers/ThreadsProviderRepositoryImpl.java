package app.groopy.threadsservice.infrastructure.providers;

import app.groopy.threadsservice.domain.exceptions.EntityNotFoundException;
import app.groopy.threadsservice.infrastructure.models.PostThreadRequest;
import app.groopy.threadsservice.infrastructure.models.PostThreadResponse;
import app.groopy.threadsservice.infrastructure.providers.db.ThreadRepository;
import app.groopy.threadsservice.infrastructure.providers.db.models.ThreadEntity;
import app.groopy.threadsservice.infrastructure.providers.exceptions.DatabaseException;
import app.groopy.threadsservice.infrastructure.providers.mappers.ProviderMapper;
import app.groopy.threadsservice.infrastructure.repository.ThreadsProviderRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ThreadsProviderRepositoryImpl implements ThreadsProviderRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(ThreadsProviderRepositoryImpl.class);
    private final ThreadRepository threadRepository;
    private final ProviderMapper providerMapper;

    @SneakyThrows
    @Autowired
    public ThreadsProviderRepositoryImpl(ThreadRepository threadRepository,
                                         ProviderMapper providerMapper) {
        this.threadRepository = threadRepository;
        this.providerMapper = providerMapper;
    }

    @SneakyThrows
    public PostThreadResponse postThread(PostThreadRequest request) {
        if (StringUtils.hasText(request.getParentId())) {
            return saveComment(request);
        }
        return saveNewThread(request);
    }

    @SneakyThrows
    public void removeThread(String threadId) {
        var thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new EntityNotFoundException("THREAD", threadId));
        threadRepository.delete(thread);
    }

    @SneakyThrows
    private PostThreadResponse saveNewThread(PostThreadRequest request) {
        ThreadEntity threadEntity;
        try {
            threadEntity = threadRepository.save(ThreadEntity.builder()
                    .userId(request.getUserId())
                    .parentId(null)
                    .likes(0L)
                    .imageUrl(request.getImageUrl())
                    .body(request.getBody())
                    .audioRecord(request.getAudioRecord())
                    .dateTime(LocalDateTime.now())
                    .comments(new ArrayList<>())
                    .build());
        } catch (Exception e) {
            LOGGER.error("An error occurred trying to save the new thread: request={}", request, e);
            throw new DatabaseException(e.getMessage());
        }

        return PostThreadResponse.builder()
                .threadId(threadEntity.getId())
                .build();
    }

    @SneakyThrows
    private PostThreadResponse saveComment(PostThreadRequest request) {
        ThreadEntity threadEntity;
        try {
            threadEntity = threadRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("THREAD", request.getParentId()));
            threadEntity.getComments().add(ThreadEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(request.getUserId())
                    .parentId(request.getParentId())
                    .likes(0L)
                    .imageUrl(request.getImageUrl())
                    .body(request.getBody())
                    .audioRecord(request.getAudioRecord())
                    .dateTime(LocalDateTime.now())
                    .build());
            threadRepository.save(threadEntity);
        } catch (Exception e) {
            LOGGER.error("An error occurred trying to save the new thread: request={}", request, e);
            throw new DatabaseException(e.getMessage());
        }
        return PostThreadResponse.builder()
                .threadId(threadEntity.getId())
                .build();
    }
}
