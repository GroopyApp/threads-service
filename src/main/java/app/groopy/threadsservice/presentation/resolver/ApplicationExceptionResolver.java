package app.groopy.threadsservice.presentation.resolver;

import app.groopy.protobuf.ThreadsServiceProto;
import app.groopy.threadsservice.application.exceptions.ApplicationBadRequestException;
import app.groopy.threadsservice.application.exceptions.ApplicationException;
import app.groopy.threadsservice.domain.models.ErrorMetadataDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.rpc.Code;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.protobuf.StatusProto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationExceptionResolver {

    public static StatusRuntimeException resolve(ApplicationException exception) {
        var errorResponseKey = ProtoUtils.keyForProto(ThreadsServiceProto.ErrorResponse.getDefaultInstance());
        Metadata metadata = new Metadata();

        var errorResponse = ThreadsServiceProto.ErrorResponse.newBuilder()
                .putAllParameters(resolveMetadata(exception.getErrorResponse())).build();
        metadata.put(errorResponseKey, errorResponse);

            return StatusProto.toStatusRuntimeException(com.google.rpc.Status.newBuilder()
            .setCode(resolveCode(exception))
            .setMessage(exception.getErrorResponse().getErrorDescription())
            .build(), metadata);
    }

    private static Map<String, String> resolveMetadata(ErrorMetadataDto errorMetadataDto) {
        return new ObjectMapper().convertValue(errorMetadataDto, new TypeReference<HashMap<String, String>>() {})
                .entrySet().stream().filter(entry -> entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static int resolveCode(ApplicationException exception) {
        if (exception instanceof ApplicationBadRequestException) {
            return Code.INVALID_ARGUMENT.getNumber();
        }
        return Code.UNKNOWN.getNumber();
    }
}
