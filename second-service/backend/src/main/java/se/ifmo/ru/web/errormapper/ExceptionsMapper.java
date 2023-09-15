package se.ifmo.ru.web.errormapper;

import org.apache.commons.lang3.StringUtils;
import se.ifmo.ru.utils.ResponseUtils;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionsMapper implements ExceptionMapper<Throwable> {
    @Inject
    ResponseUtils responseUtils;

    @Override
    public Response toResponse(Throwable exception) {
        if (StringUtils.isEmpty(exception.getMessage())) {
            return responseUtils.buildResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    "Not found"
            );
        }
        exception.printStackTrace();
        return responseUtils.buildResponseWithMessage(Response.Status.NOT_FOUND, exception.getMessage());
    }
}
