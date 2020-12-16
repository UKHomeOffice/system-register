package uk.gov.digital.ho.systemregister.io.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

class ConstraintViolationExceptionMapperTest {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private ConstraintViolationExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ConstraintViolationExceptionMapper();
    }

    @Test
    void mapsErrorsByFieldName() {
        Set<ConstraintViolation<Bean>> violations = VALIDATOR.validate(new Bean());
        ConstraintViolationException exception = new ConstraintViolationException(null, violations);

        Response response = mapper.toResponse(exception);

        assertThat(response)
                .extracting(Response::getStatusInfo)
                .isEqualTo(BAD_REQUEST);
        assertThat(response.readEntity(new GenericType<Map<String, Object>>() {}))
                .extracting("errors", map(String.class, String.class))
                .containsEntry("field", "reason-message");
    }

    @SuppressWarnings("unused")
    private static class Bean {
        @NotNull(message = "reason-message")
        private Object field;
    }
}
