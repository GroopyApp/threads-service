package app.groopy.threadsservice.application.exceptions;

import app.groopy.threadsservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationBadRequestException extends ApplicationException {

    public ApplicationBadRequestException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
