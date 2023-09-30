package com.ervelus.marineservice.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    String message = "Item not found";

    @Override
    public Response toResponse(NotFoundException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }
}
