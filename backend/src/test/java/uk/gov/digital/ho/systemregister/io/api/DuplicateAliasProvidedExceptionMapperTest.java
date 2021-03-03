package uk.gov.digital.ho.systemregister.io.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.DuplicateAliasProvidedException;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

class DuplicateAliasProvidedExceptionMapperTest {
    private DuplicateAliasProvidedExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DuplicateAliasProvidedExceptionMapper();
    }

    @Test
    void mapsErrors() {
        DuplicateAliasProvidedException exception = new DuplicateAliasProvidedException(Set.of("duplicate alias"));

        Response response = mapper.toResponse(exception);

        assertThat(response)
                .extracting(Response::getStatusInfo)
                .isEqualTo(BAD_REQUEST);
        assertThat(response.readEntity(new GenericType<Map<String, Object>>() {}))
                .extracting("errors", map(String.class, String.class))
                .containsEntry("aliases", "You have entered duplicate aliases: duplicate alias. Please amend or remove the duplicates.");
    }

    @Test
    void isDiscoverableByQuarkus() {
        assertThat(RiskDoesNotExistExceptionMapper.class).hasAnnotation(Provider.class);
    }
}
