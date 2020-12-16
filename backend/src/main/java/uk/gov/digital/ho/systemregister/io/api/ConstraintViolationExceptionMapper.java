package uk.gov.digital.ho.systemregister.io.api;

import java.util.Map;
import java.util.function.Function;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static java.util.stream.Collectors.toMap;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Function<ConstraintViolation<?>, String> FIELD_NAME = violation -> {
        Path path = violation.getPropertyPath();
        return lastSegment(path);
    };

    private static String lastSegment(Iterable<Path.Node> path) {
        String name = null;
        for (Path.Node node : path) {
            name = node.getName();
        }
        return name;
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = exception.getConstraintViolations()
                .stream()
                .collect(toMap(FIELD_NAME, ConstraintViolation::getMessage));
        return Response.status(BAD_REQUEST).entity(Map.of("errors", errors)).build();
    }
}
