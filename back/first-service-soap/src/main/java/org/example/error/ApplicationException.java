package org.example.error;

import lombok.Getter;
import org.example.model.common.ErrorResponse;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ApplicationException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}
