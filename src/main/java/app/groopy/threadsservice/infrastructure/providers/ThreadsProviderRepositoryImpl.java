package app.groopy.threadsservice.infrastructure.providers;

import app.groopy.threadsservice.domain.exceptions.EntityNotFoundException;
import app.groopy.threadsservice.infrastructure.models.PostThreadRequest;
import app.groopy.threadsservice.infrastructure.models.PostThreadResponse;
import app.groopy.threadsservice.infrastructure.providers.db.EventRepository;
import app.groopy.threadsservice.infrastructure.providers.db.ThreadRepository;
import app.groopy.threadsservice.infrastructure.providers.db.TopicRepository;
import app.groopy.threadsservice.infrastructure.providers.db.models.Entity;
import app.groopy.threadsservice.infrastructure.providers.db.models.EventEntity;
import app.groopy.threadsservice.infrastructure.providers.db.models.ThreadEntity;
import app.groopy.threadsservice.infrastructure.providers.db.models.TopicEntity;
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

@Service
public class ThreadsProviderRepositoryImpl implements ThreadsProviderRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(ThreadsProviderRepositoryImpl.class);
    private final ThreadRepository threadRepository;
    private final TopicRepository topicRepository;
    private final EventRepository eventRepository;
    private final ProviderMapper providerMapper;

    @SneakyThrows
    @Autowired
    public ThreadsProviderRepositoryImpl(ThreadRepository threadRepository,
                                         TopicRepository topicRepository,
                                         EventRepository eventRepository,
                                         ProviderMapper providerMapper) {
        this.threadRepository = threadRepository;
        this.topicRepository = topicRepository;
        this.eventRepository = eventRepository;
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
    private PostThreadResponse saveNewThread(PostThreadRequest request) {
        ThreadEntity threadEntity;
        try {
            threadEntity = threadRepository.save(ThreadEntity.builder()
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

        saveThreadReference(request, threadEntity);

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

    private void saveThreadReference(PostThreadRequest request, ThreadEntity threadEntity) throws EntityNotFoundException, DatabaseException {
        TopicEntity topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new EntityNotFoundException("TOPIC", request.getTopicId()));
        if (StringUtils.hasText(request.getEventId())) {
            EventEntity eventEntity = topic.getEvents().stream().filter(event -> event.getId().equals(request.getEventId())).findAny()
                    .orElseThrow(() -> new EntityNotFoundException("EVENT", request.getEventId()));
            eventEntity.getThreads().add(threadEntity);
            saveReferenceOrRollback(eventEntity, threadEntity);
        } else {
            topic.getThreads().add(threadEntity);
            saveReferenceOrRollback(topic, threadEntity);
        }
    }

    private void saveReferenceOrRollback(Entity entity, ThreadEntity threadEntity) throws DatabaseException {
        try {
            if (entity instanceof EventEntity) {
                eventRepository.save((EventEntity) entity);
            } else if (entity instanceof TopicEntity) {
                topicRepository.save((TopicEntity) entity);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred trying to save the new thread reference: thread={}", threadEntity, e);
            threadRepository.delete(threadEntity);
            throw new DatabaseException(e.getMessage());
        }
    }
}
