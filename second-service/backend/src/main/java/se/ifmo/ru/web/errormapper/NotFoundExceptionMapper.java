package se.ifmo.ru.web.errormapper;

import org.apache.commons.lang3.StringUtils;
import se.ifmo.ru.utils.ResponseUtils;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Inject
    ResponseUtils responseUtils;

    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response toResponse(NotFoundException exception) {
        if (StringUtils.isEmpty(exception.getMessage())) {
            return responseUtils.buildResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    "Not Found"
            );
        }
        if (exception.getCause() != null) {
            if (exception.getCause().getClass() == NumberFormatException.class) {
                return responseUtils.buildResponseWithMessage(Response.Status.BAD_REQUEST, "Invalid parameters supplied");
            }
        }
        return responseUtils.buildResponseWithMessage(Response.Status.NOT_FOUND, exception.getMessage());
    }
}