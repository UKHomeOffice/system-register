package uk.gov.digital.ho.systemregister.io.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CommandHasNoEffectExceptionMapperTest {
    private static final int UNPROCESSABLE_ENTITY = 422;

    private CommandHasNoEffectExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CommandHasNoEffectExceptionMapper();
    }

    @Test
    void returnsNotFoundResponse() {
        CommandHasNoEffectException exception = new CommandHasNoEffectException("reason message");

        Response response = mapper.toResponse(exception);

        assertThat(response)
                .hasFieldOrPropertyWithValue("status", UNPROCESSABLE_ENTITY);
        assertThat(response.readEntity(new GenericType<Map<String, Object>>() {}))
                .containsEntry("reason", "reason message");
    }
}
