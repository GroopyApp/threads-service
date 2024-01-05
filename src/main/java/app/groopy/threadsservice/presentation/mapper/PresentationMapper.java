package app.groopy.threadsservice.presentation.mapper;

import app.groopy.protobuf.ThreadsServiceProto;
import app.groopy.threadsservice.domain.models.requests.ContentDto;
import app.groopy.threadsservice.domain.models.requests.PostThreadRequestDto;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {

    //PROTO to DTO
//    CreateThreadsRoomRequestDto map(ThreadsServiceProto.CreateThreadsRoomRequest input);

    default LocalDateTime toLocalDate(String input) {
        return LocalDateTime.parse(input);
    }

    default String toDateString(LocalDateTime input) {
        return input.toString();
    }

    PostThreadRequestDto map(ThreadsServiceProto.PostThreadRequest input);

    ContentDto map(ThreadsServiceProto.ContentMessage input);
}
