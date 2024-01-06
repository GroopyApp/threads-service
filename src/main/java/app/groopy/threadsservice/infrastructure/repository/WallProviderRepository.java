package app.groopy.threadsservice.infrastructure.repository;

import app.groopy.threadsservice.infrastructure.models.PublishThreadRequest;
import app.groopy.threadsservice.infrastructure.models.PublishThreadResponse;
import app.groopy.threadsservice.infrastructure.providers.exceptions.WallServiceException;

public interface WallProviderRepository {

    PublishThreadResponse publishThread(PublishThreadRequest request) throws WallServiceException;
}
