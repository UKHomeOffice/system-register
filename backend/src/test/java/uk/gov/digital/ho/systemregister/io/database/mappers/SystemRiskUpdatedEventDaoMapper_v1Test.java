package uk.gov.digital.ho.systemregister.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemRiskUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemRiskUpdatedEventDAO_v1;

import javax.json.bind.JsonbBuilder;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SystemRiskUpdatedEventBuilder.aSystemRiskUpdatedEvent;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

class SystemRiskUpdatedEventDaoMapper_v1Test {
    private SystemRiskUpdatedEventDaoMapper_v1 mapper;

    @BeforeEach
    void setUp() {
        mapper = new SystemRiskUpdatedEventDaoMapper_v1(JsonbBuilder.create());
    }

    @Test
    void supportsV1SystemRisksUpdatedEvents() {
        boolean supported = mapper.supports(SystemRiskUpdatedEventDAO_v1.class);

        assertThat(supported).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {BaseDao.class})
    void doesNotSupportOtherTypes(Class<?> type) {
        boolean supported = mapper.supports(type);

        assertThat(supported).isFalse();
    }

    @Test
    void mapsToSystemRisksUpdatedEventDAO() {
        SystemRiskUpdatedEvent event = aSystemRiskUpdatedEvent()
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
    void rejectsEventsOtherThanSystemRisksUpdated() {
        var event = new SR_Event(aPerson().build(), Instant.now());

        assertThatThrownBy(() -> mapper.mapToDao(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }

    @Test
    void mapsToSystemRisksUpdatedEvent() {
        SystemRiskUpdatedEvent expectedEvent = aSystemRiskUpdatedEvent()
                .withId(789)
                .withRisk(new SR_Risk("some risk", "low", "some reason"))
                .withTimestamp(Instant.parse("2020-01-02T03:04:05Z"))
                .withAuthor(aPerson()
                        .withEmail("user@example.com")
                        .withFirstName("test_FirstName")
                        .withSurname("test_Surname")
                        .withUsername("test_Username"))
                .build();

        String json = getResourceAsString("dao/v1/system-risk-updated-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }
}
