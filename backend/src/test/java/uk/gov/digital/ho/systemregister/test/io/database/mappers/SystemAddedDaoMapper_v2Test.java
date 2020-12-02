package uk.gov.digital.ho.systemregister.test.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemAddedEventDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v2.SystemAddedEventDAO_v2;
import uk.gov.digital.ho.systemregister.io.database.mappers.SystemAddedDaoMapper_v2;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;

import java.time.Instant;
import javax.json.bind.JsonbBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder.aSystemAddedEvent;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

public class SystemAddedDaoMapper_v2Test {
    private SystemAddedDaoMapper_v2 mapper;

    @BeforeEach
    void setUp() {
        mapper = new SystemAddedDaoMapper_v2(JsonbBuilder.create());
    }

    @Test
    void supportsV2SystemAddedEvents() {
        boolean supported = mapper.supports(SystemAddedEventDAO_v2.class);

        assertThat(supported).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {SystemAddedEventDAO_v1.class, BaseDao.class})
    void doesNotSupportOtherTypes(Class<?> type) {
        boolean supported = mapper.supports(type);

        assertThat(supported).isFalse();
    }

    @Test
    void mapsSystemAddedEventToDao() {
        SystemAddedEvent evt = aSystemAddedEvent()
                .withAuthor(aPerson()
                        .withUsername("v2Person")
                        .withFirstName("v2FirstName")
                        .withSurname("v2Surname")
                        .withEmail("v2Email"))
                .build();

        var actual = mapper.mapToDao(evt);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(evt);
    }

    @Test
    void rejectsEventsOtherThanSystemAdded() {
        var event = new SR_Event(aPerson().build(), Instant.now());

        assertThatThrownBy(() -> mapper.mapToDao(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }

    @Test
    void mapsDaoToSystemAddedEvent() {
        SystemAddedEvent expectedEvent = aSystemAddedEvent()
                .withId(1234)
                .withSystemCalled("system")
                .withTimeStamp(Instant.parse("2020-10-09T08:07:06.050Z"))
                .withLastUpdated(Instant.parse("2020-10-09T08:07:05.000Z"))
                .withAuthor(aPerson()
                        .withUsername("user")
                        .withSurname("surname")
                        .withFirstName("forename")
                        .withEmail("email"))
                .build();
        String json = getResourceAsString("dao/v2/system-added-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }
}
