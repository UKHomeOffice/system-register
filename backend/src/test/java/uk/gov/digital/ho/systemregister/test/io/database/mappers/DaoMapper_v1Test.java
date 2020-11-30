package uk.gov.digital.ho.systemregister.test.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.mappers.DaoMapper_v1;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DaoMapper_v1Test {
    private DaoMapper_v1 mapper;

    @BeforeEach
    void setUp() {
        mapper = new DaoMapper_v1();
    }

    @Test
    void mapsSystemAddedEventToDao() {
        SystemAddedEvent evt = new SystemAddedEventBuilder().build();

        var actual = mapper.map(evt);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(evt);
    }

    @Test
    void rejectsEventsOtherThanSystemAdded() {
        var event = new SR_Event(new SR_Person("user"), Instant.now());

        assertThatThrownBy(() -> mapper.map(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event$");
    }
}
