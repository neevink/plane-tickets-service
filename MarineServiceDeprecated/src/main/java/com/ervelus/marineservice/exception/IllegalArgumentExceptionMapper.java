package com.ervelus.marineservice.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    String message = "Validation failed";

    @Override
    public Response toResponse(IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }
}
