package uk.gov.digital.ho.systemregister.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.events.InformationAssetOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.InformationAssetOwnerUpdatedEventDAO_v1;

import javax.json.bind.JsonbBuilder;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.InformationAssetOwnerUpdatedEventBuilder.anInformationAssetOwnerUpdatedEvent;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

class InformationAssetOwnerUpdatedEventDaoMapper_v1Test {
    private InformationAssetOwnerUpdatedEventDaoMapper_v1 mapper;

    @BeforeEach
    void setUp() {
        mapper = new InformationAssetOwnerUpdatedEventDaoMapper_v1(JsonbBuilder.create());
    }

    @Test
    void supportsV1InformationAssetOwnerUpdatedEvents() {
        boolean supported = mapper.supports(InformationAssetOwnerUpdatedEventDAO_v1.class);

        assertThat(supported).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {BaseDao.class})
    void doesNotSupportOtherTypes(Class<?> type) {
        boolean supported = mapper.supports(type);

        assertThat(supported).isFalse();
    }

    @Test
    void rejectsEventsOtherThanInformationAssetOwnerUpdated() {
        var event = new SR_Event(aPerson().build(), Instant.now());

        assertThatThrownBy(() -> mapper.mapToDao(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }

    @Test
    void mapsToProductOwnerUpdatedEventDAO() {
        InformationAssetOwnerUpdatedEvent event = anInformationAssetOwnerUpdatedEvent()
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
    void mapsToInformationAssetOwnerUpdatedEvent() {
        InformationAssetOwnerUpdatedEvent expectedEvent = anInformationAssetOwnerUpdatedEvent()
                .withId(5678)
                .withInformationAssetOwner("Gharial Croc")
                .withTimestamp(Instant.parse("2020-03-07T02:09:00Z"))
                .withAuthor(aPerson()
                        .withEmail("user@example.com")
                        .withFirstName("test_FirstName")
                        .withSurname("test_Surname")
                        .withUsername("test_Username"))
                .build();

        String json = getResourceAsString("dao/v1/information-asset-owner-updated-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }

    @Test
    void mapsUnknownOwnerToInformationAssetOwnerUpdatedEvent() {
        InformationAssetOwnerUpdatedEvent expectedEvent = anInformationAssetOwnerUpdatedEvent()
                .withId(789)
                .withInformationAssetOwner(null)
                .withTimestamp(Instant.parse("2020-03-07T02:09:00Z"))
                .withAuthor(aPerson()
                        .withEmail("user@example.com")
                        .withFirstName("test_FirstName")
                        .withSurname("test_Surname")
                        .withUsername("test_Username"))
                .build();

        String json = getResourceAsString("dao/v1/unknown-information-asset-owner-updated-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }
}
