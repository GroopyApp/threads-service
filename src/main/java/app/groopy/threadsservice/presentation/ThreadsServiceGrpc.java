package app.groopy.threadsservice.presentation;

import app.groopy.protobuf.ThreadsServiceProto;
import app.groopy.threadsservice.application.ApplicationService;
import app.groopy.threadsservice.application.exceptions.ApplicationException;
import app.groopy.threadsservice.presentation.mapper.PresentationMapper;
import app.groopy.threadsservice.presentation.resolver.ApplicationExceptionResolver;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ThreadsServiceGrpc extends app.groopy.protobuf.ThreadsServiceGrpc.ThreadsServiceImplBase {

    private final Logger LOGGER = LoggerFactory.getLogger(ThreadsServiceGrpc.class);

    private final ApplicationService applicationService;
    private final PresentationMapper presentationMapper;

    @Autowired
    public ThreadsServiceGrpc(ApplicationService applicationService, PresentationMapper presentationMapper) {
        this.applicationService = applicationService;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public void postThread(ThreadsServiceProto.PostThreadRequest request, StreamObserver<ThreadsServiceProto.StatusResponse> responseObserver) {
        LOGGER.info("Processing PostThreadRequest {}", request);
        try {
            applicationService.postThread(presentationMapper.map(request));
            responseObserver.onNext(ThreadsServiceProto.StatusResponse.newBuilder()
                            .setStatus(200)
                    .build());
            responseObserver.onCompleted();
        } catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }
}
