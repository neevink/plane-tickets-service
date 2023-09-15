package se.ifmo.ru.web.errormapper;

import org.apache.commons.lang3.StringUtils;
import se.ifmo.ru.utils.ResponseUtils;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class EJBexceptionMapper implements ExceptionMapper<EJBException> {
    @Inject
    ResponseUtils responseUtils;

    @Override
    public Response toResponse(EJBException exception) {
        if (StringUtils.isEmpty(exception.getMessage())) {
            return responseUtils.buildResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    "Not Found"
            );
        }
        if (exception.getCause() != null) {
            if (exception.getCause().getClass() == NumberFormatException.class) {
                return responseUtils.buildResponseWithMessage(Response.Status.NOT_FOUND, exception.getCause().getMessage());
            }
        }

        exception.printStackTrace();
        return responseUtils.buildResponseWithMessage(Response.Status.NOT_FOUND, exception.getMessage());
    }
}
