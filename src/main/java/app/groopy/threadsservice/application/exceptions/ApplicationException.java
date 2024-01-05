package app.groopy.threadsservice.application.exceptions;

import app.groopy.threadsservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationException extends Exception {
    private final ErrorMetadataDto errorResponse;

    public ApplicationException(ErrorMetadataDto errorResponse) {
        this.errorResponse = errorResponse;
    }
}
