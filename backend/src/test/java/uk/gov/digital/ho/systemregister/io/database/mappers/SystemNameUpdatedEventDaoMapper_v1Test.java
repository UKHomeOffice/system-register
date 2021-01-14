package uk.gov.digital.ho.systemregister.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemNameUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemNameUpdatedEventDAO_v1;

import javax.json.bind.JsonbBuilder;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SystemNameUpdatedEventBuilder.aSystemNameUpdatedEvent;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

class SystemNameUpdatedEventDaoMapper_v1Test {
    private SystemNameUpdatedEventDaoMapper_v1 mapper;

    @BeforeEach
    void setUp() {
        mapper = new SystemNameUpdatedEventDaoMapper_v1(JsonbBuilder.create());
    }

    @Test
    void supportsV1SystemNameUpdatedEvents() {
        boolean supported = mapper.supports(SystemNameUpdatedEventDAO_v1.class);

        assertThat(supported).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {BaseDao.class})
    void doesNotSupportOtherTypes(Class<?> type) {
        boolean supported = mapper.supports(type);

        assertThat(supported).isFalse();
    }

    @Test
    void rejectsEventsOtherThanSystemNameUpdated() {
        var event = new SR_Event(aPerson().build(), Instant.now());

        assertThatThrownBy(() -> mapper.mapToDao(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }

    @Test
    void mapsToSystemNameUpdatedEventDAO() {
        SystemNameUpdatedEvent event = aSystemNameUpdatedEvent()
                .withAuthor(aPerson()
                        .withEmail("some@email.com")
                        .withFirstName("John")
                        .withSurname("Doe")
                        .withUsername("johndoe"))
                .build();

        var actual = mapper.mapToDao(event);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(event);
    }

    @Test
    void mapsToSystemNameUpdatedEvent() {
        SystemNameUpdatedEvent expectedEvent = aSystemNameUpdatedEvent()
                .withId(9631)
                .withSystemName("updated system name")
                .withTimestamp(Instant.parse("2020-03-07T02:09:00Z"))
                .withAuthor(aPerson()
                        .withEmail("user@example.com")
                        .withFirstName("test_FirstName")
                        .withSurname("test_Surname")
                        .withUsername("test_Username"))
                .build();

        String json = getResourceAsString("dao/v1/system-name-updated-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }

}
