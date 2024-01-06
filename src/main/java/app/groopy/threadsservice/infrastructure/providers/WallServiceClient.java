package app.groopy.threadsservice.infrastructure.providers;

import app.groopy.protobuf.WallServiceGrpc;
import app.groopy.protobuf.WallServiceProto;
import app.groopy.threadsservice.infrastructure.models.PublishThreadRequest;
import app.groopy.threadsservice.infrastructure.models.PublishThreadResponse;
import app.groopy.threadsservice.infrastructure.providers.exceptions.WallServiceException;
import app.groopy.threadsservice.infrastructure.repository.WallProviderRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.util.StringUtils;


@Service
public class WallServiceClient implements WallProviderRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(WallServiceClient.class);

    @GrpcClient("wallService")
    WallServiceGrpc.WallServiceBlockingStub wallServiceStub;

    public PublishThreadResponse publishThread(PublishThreadRequest request) throws WallServiceException {
        try {
            var builder = WallServiceProto.PublishThreadRequest.newBuilder()
                    .setTopicId(request.getTopicId())
                    .setThreadId(request.getThreadId());

            if (StringUtils.hasText(request.getEventId())) {
                builder.setEventId(request.getEventId());
            }

            WallServiceProto.PublishThreadResponse response = wallServiceStub.publishThread(builder.build());
            if (response.getStatus() == 200) {
                return PublishThreadResponse.builder()
                        .threadId(request.getThreadId())
                        .build();
            } else {
                LOGGER.error("Unable to publish new thread with id: {}", request.getThreadId());
                throw new WallServiceException("An error occurred trying to publish new thread with id: " + request.getThreadId());
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred trying to connect with wall-service");
            throw new WallServiceException(e.getMessage());
        } catch (WallServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
