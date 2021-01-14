package uk.gov.digital.ho.systemregister.io.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.SystemNameNotUniqueException;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

class SystemNameNotUniqueMapperTest {
    private SystemNameNotUniqueExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SystemNameNotUniqueExceptionMapper();
    }

    @Test
    void mapsErrors() {
        SystemNameNotUniqueException exception = new SystemNameNotUniqueException("x");

        Response response = mapper.toResponse(exception);

        assertThat(response)
                .extracting(Response::getStatusInfo)
                .isEqualTo(BAD_REQUEST);
        assertThat(response.readEntity(new GenericType<Map<String, Object>>() {}))
                .extracting("errors", map(String.class, String.class))
                .containsEntry("name", "There is already a system called x");
    }
}
