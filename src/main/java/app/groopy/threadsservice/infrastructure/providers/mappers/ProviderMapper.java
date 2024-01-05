package app.groopy.threadsservice.infrastructure.providers.mappers;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ProviderMapper {
//    @Mappings({
//            @Mapping(target = "threadsName", source = "channelName")})
//    ThreadsInfo map(ThreadsEntity input);
}
