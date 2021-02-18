package uk.gov.digital.ho.systemregister.io.api;

import uk.gov.digital.ho.systemregister.domain.SystemNameNotUniqueException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class SystemNameNotUniqueExceptionMapper implements ExceptionMapper<SystemNameNotUniqueException> {

    @Override
    public Response toResponse(SystemNameNotUniqueException exception) {
        return Response.status(BAD_REQUEST)
                .entity(Map.of("errors", Map.of("name", exception.getMessage()))).build();
    }
}
