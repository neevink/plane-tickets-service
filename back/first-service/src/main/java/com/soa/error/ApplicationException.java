package com.soa.error;

import com.soa.model.common.ErrorResponse;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ApplicationException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}
