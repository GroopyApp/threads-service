package app.groopy.threadsservice.infrastructure.repository;

import app.groopy.threadsservice.infrastructure.models.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadsProviderRepository {

    PostThreadResponse postThread(PostThreadRequest request);
}
