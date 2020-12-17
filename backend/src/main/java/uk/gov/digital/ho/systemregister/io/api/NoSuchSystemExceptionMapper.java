package uk.gov.digital.ho.systemregister.io.api;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class NoSuchSystemExceptionMapper implements ExceptionMapper<NoSuchSystemException> {
    @Override
    public Response toResponse(NoSuchSystemException exception) {
        return Response.status(NOT_FOUND).build();
    }
}
