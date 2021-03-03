package uk.gov.digital.ho.systemregister.io.api;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.RiskDoesNotExistException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Map;

public class RiskDoesNotExistExceptionMapper implements ExceptionMapper<RiskDoesNotExistException> {
    @Override
    public Response toResponse(RiskDoesNotExistException exception) {
        return Response.status(422, "Unprocessable Entity")
                .entity(Map.of("name", exception.getMessage()))
                .build();
    }
}
