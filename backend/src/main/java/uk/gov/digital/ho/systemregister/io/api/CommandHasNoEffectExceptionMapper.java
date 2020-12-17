package uk.gov.digital.ho.systemregister.io.api;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;

import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CommandHasNoEffectExceptionMapper implements ExceptionMapper<CommandHasNoEffectException> {
    @Override
    public Response toResponse(CommandHasNoEffectException exception) {
        return Response.status(422, "Unprocessable Entity")
                .entity(Map.of("reason", exception.getMessage()))
                .build();
    }
}
