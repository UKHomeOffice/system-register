package uk.gov.digital.ho.systemregister.io.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

class NoSuchSystemExceptionMapperTest {
    private NoSuchSystemExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NoSuchSystemExceptionMapper();
    }

    @Test
    void returnsNotFoundResponse() {
        NoSuchSystemException exception = new NoSuchSystemException(654);

        Response response = mapper.toResponse(exception);

        assertThat(response)
                .extracting(Response::getStatusInfo)
                .isEqualTo(NOT_FOUND);
    }

    @Test
    void isDiscoverableByQuarkus() {
        assertThat(NoSuchSystemExceptionMapper.class).hasAnnotation(Provider.class);
    }
}
