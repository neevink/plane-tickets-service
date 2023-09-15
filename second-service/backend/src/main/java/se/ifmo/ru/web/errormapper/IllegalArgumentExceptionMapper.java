package se.ifmo.ru.web.errormapper;

import org.apache.commons.lang3.StringUtils;
import se.ifmo.ru.utils.ResponseUtils;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    @Inject
    ResponseUtils responseUtils;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        if (StringUtils.isEmpty(exception.getMessage())) {
            return responseUtils.buildResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    "Bad request"
            );
        }
        return responseUtils.buildResponseWithMessage(Response.Status.BAD_REQUEST, exception.getMessage());
    }
}
