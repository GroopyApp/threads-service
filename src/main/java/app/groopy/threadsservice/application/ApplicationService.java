package app.groopy.threadsservice.application;

import app.groopy.threadsservice.application.exceptions.ApplicationException;
import app.groopy.threadsservice.domain.models.requests.PostThreadRequestDto;
import app.groopy.threadsservice.domain.resolver.InfrastructureExceptionResolver;
import app.groopy.threadsservice.domain.services.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private final DomainService domainService;

    @Autowired
    public ApplicationService(DomainService domainService) {
        this.domainService = domainService;
    }

    public void postThread(PostThreadRequestDto request) throws ApplicationException {
        try {
            //TODO validate fields
            var result = domainService.postThread(request);
            LOGGER.info("thread posted successfully: {}", result);
        } catch (Exception e) {
            throw InfrastructureExceptionResolver.resolve(e);
        }
    }
}
