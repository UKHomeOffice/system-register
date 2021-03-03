package uk.gov.digital.ho.systemregister.io.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.RiskDoesNotExistException;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RiskDoesNotExistExceptionMapperTest {
    private static final int UNPROCESSABLE_ENTITY = 422;

    private RiskDoesNotExistExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RiskDoesNotExistExceptionMapper();
    }

    @Test
    void returnsNotFoundResponse() {
        RiskDoesNotExistException exception = new RiskDoesNotExistException("risk name");

        Response response = mapper.toResponse(exception);

        assertThat(response)
                .hasFieldOrPropertyWithValue("status", UNPROCESSABLE_ENTITY);
        assertThat(response.readEntity(new GenericType<Map<String, Object>>() {}))
                .containsEntry("name", "system does not have risk: risk name");
    }
}
