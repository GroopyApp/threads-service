package app.groopy.threadsservice.domain.resolver;

import app.groopy.threadsservice.application.exceptions.ApplicationException;
import app.groopy.threadsservice.domain.models.ErrorMetadataDto;

public class InfrastructureExceptionResolver {

    public static ApplicationException resolve(Exception e) {
//        if (e instanceof EntityAlreadyExistsException) {
//            var ex = (EntityAlreadyExistsException) e;
//            return new ApplicationAlreadyExistsException(ErrorMetadataDto.builder()
//                    .errorDescription(ex.getLocalizedMessage())
//                    .existingEntityId(ex.getId())
//                    .entityName(ex.getEntityName())
//                    .build());

        return new ApplicationException(ErrorMetadataDto.builder()
                .errorDescription("Unmapped exception")
                .build());
    }
}
