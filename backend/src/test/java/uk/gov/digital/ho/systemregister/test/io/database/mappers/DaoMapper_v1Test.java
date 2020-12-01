package uk.gov.digital.ho.systemregister.test.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.mappers.DaoMapper_v1;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;

import javax.json.bind.JsonbBuilder;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

public class DaoMapper_v1Test {
    private DaoMapper_v1 mapper;

    @BeforeEach
    void setUp() {
        mapper = new DaoMapper_v1(JsonbBuilder.create());
    }

    @Test
    void mapsSystemAddedEventToDao() {
        SystemAddedEvent evt = new SystemAddedEventBuilder().build();

        var actual = mapper.mapToDao(evt);

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("author")
                .isEqualTo(evt);
        assertThat(actual.author).hasFieldOrPropertyWithValue("name", "Corey logan");
    }

    @Test
    void rejectsEventsOtherThanSystemAdded() {
        var event = new SR_Event(new SR_Person("user", null,null,null), Instant.now());

        assertThatThrownBy(() -> mapper.mapToDao(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }

    @Test
    void mapsDaoToSystemAddedEvent() {
        SystemAddedEvent expectedEvent = new SystemAddedEventBuilder()
                .withId(123)
                .withSystemCalled("system")
                .withTimeStamp(Instant.parse("2020-10-09T08:07:06.050Z"))
                .withLastUpdated(Instant.parse("2020-10-09T08:07:05.000Z"))
                .build();
        String json = getResourceAsString("dao/v1/system-added-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }

    @Test
    void mapsSerialisedDomainObjectToSystemAddedEvent() {
        SystemAddedEvent expectedEvent = new SystemAddedEventBuilder()
                .withId(123)
                .withSystemCalled("system")
                .withTimeStamp(Instant.parse("2020-10-09T08:07:06.050Z"))
                .withLastUpdated(Instant.parse("2020-10-09T08:07:05.000Z"))
                .build();
        String json = getResourceAsString("dao/v1/system-added-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }
}
