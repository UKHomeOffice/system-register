package uk.gov.digital.ho.systemregister.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SunsetUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SunsetUpdatedEventDAO_v1;

import javax.json.bind.JsonbBuilder;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder.aSunset;
import static uk.gov.digital.ho.systemregister.helpers.builders.SunsetUpdatedEventBuilder.aSunsetUpdatedEvent;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

class SunsetUpdatedEventDaoMapper_v1Test {
    private SunsetUpdatedEventDaoMapper_v1 mapper;

    @BeforeEach
    void setUp() {
        mapper = new SunsetUpdatedEventDaoMapper_v1(JsonbBuilder.create());
    }

    @Test
    void supportsV1SunsetUpdatedEvents() {
        boolean supported = mapper.supports(SunsetUpdatedEventDAO_v1.class);

        assertThat(supported).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {BaseDao.class})
    void doesNotSupportOtherTypes(Class<?> type) {
        boolean supported = mapper.supports(type);

        assertThat(supported).isFalse();
    }

    @Test
    void mapsToSunsetUpdatedEventDAO() {
        SunsetUpdatedEvent event = aSunsetUpdatedEvent()
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
    void rejectsEventsOtherThanSunsetUpdated() {
        var event = new SR_Event(aPerson().build(), Instant.now());

        assertThatThrownBy(() -> mapper.mapToDao(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }

    @Test
    void mapsToSunsetUpdatedEvent() {
        SunsetUpdatedEvent expectedEvent = aSunsetUpdatedEvent()
                .withId(789)
                .withSunset(aSunset().withDate(LocalDate.parse("2021-06-01")).withAdditionalInformation("sunset info"))
                .withTimestamp(Instant.parse("2020-01-02T03:04:05Z"))
                .withAuthor(aPerson()
                        .withEmail("user@example.com")
                        .withFirstName("test_FirstName")
                        .withSurname("test_Surname")
                        .withUsername("test_Username"))
                .build();

        String json = getResourceAsString("dao/v1/sunset-updated-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }

    @Test
    void mapsUnknownOwnerToSunsetUpdatedEvent() {
        SunsetUpdatedEvent expectedEvent = aSunsetUpdatedEvent()
                .withId(890)
                .withSunset(aSunset().withDate(null).withAdditionalInformation(null))
                .withTimestamp(Instant.parse("2020-04-01T12:21:22Z"))
                .withAuthor(aPerson()
                        .withEmail("user@example.com")
                        .withFirstName("test_FirstName")
                        .withSurname("test_Surname")
                        .withUsername("test_Username"))
                .build();

        String json = getResourceAsString("dao/v1/unknown-sunset-updated-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }
}
