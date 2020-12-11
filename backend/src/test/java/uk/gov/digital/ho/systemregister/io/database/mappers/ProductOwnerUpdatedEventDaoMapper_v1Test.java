package uk.gov.digital.ho.systemregister.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.ProductOwnerUpdatedEventDAO_v1;

import java.time.Instant;
import javax.json.bind.JsonbBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.ProductOwnerUpdatedEventBuilder.aProductOwnerUpdatedEvent;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

class ProductOwnerUpdatedEventDaoMapper_v1Test {

    private ProductOwnerUpdatedEventDaoMapper_v1 mapper;


    @BeforeEach
    void setUp() {
        mapper = new ProductOwnerUpdatedEventDaoMapper_v1(JsonbBuilder.create());
    }

    @Test
    void supportsV1ProductOwnerUpdatedEvents() {
        boolean supported = mapper.supports(ProductOwnerUpdatedEventDAO_v1.class);

        assertThat(supported).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {BaseDao.class})
    void doesNotSupportOtherTypes(Class<?> type) {
        boolean supported = mapper.supports(type);

        assertThat(supported).isFalse();
    }

    @Test
    void rejectsEventsOtherThanProductOwnerUpdated() {
        var event = new SR_Event(aPerson().build(), Instant.now());

        assertThatThrownBy(() -> mapper.mapToDao(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }

    @Test
    void mapsToProductOwnerUpdatedEventDAO() {
        ProductOwnerUpdatedEvent event = aProductOwnerUpdatedEvent()
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
    void mapsToProductOwnerUpdatedEvent() {
        ProductOwnerUpdatedEvent expectedEvent = aProductOwnerUpdatedEvent()
                .withId(5678)
                .withProductOwner("Jasmine Levy")
                .withTimestamp(Instant.parse("2020-03-07T02:09:00Z"))
                .withAuthor(
                        aPerson()
                                .withEmail("test@email.com")
                                .withFirstName("Amy")
                                .withSurname("Green")
                                .withUsername("amgreen"))
                .build();

        String json = getResourceAsString("dao/v1/product-owner-updated-event.json");

        SR_Event event = mapper.mapToDomain(json);

        assertThat(event).usingRecursiveComparison()
                .isEqualTo(expectedEvent);
    }
}