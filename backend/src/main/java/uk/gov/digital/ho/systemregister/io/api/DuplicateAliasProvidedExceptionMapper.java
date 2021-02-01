package uk.gov.digital.ho.systemregister.io.api;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.DuplicateAliasProvidedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class DuplicateAliasProvidedExceptionMapper implements ExceptionMapper<DuplicateAliasProvidedException> {

    @Override
    public Response toResponse(DuplicateAliasProvidedException exception) {
        return Response.status(BAD_REQUEST)
                .entity(Map.of("errors",
                        Map.of("alias", exception.getMessage() + ". Please amend or remove the duplicates.")))
                .build();
    }
}
