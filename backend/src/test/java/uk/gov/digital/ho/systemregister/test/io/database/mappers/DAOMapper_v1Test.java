package uk.gov.digital.ho.systemregister.test.io.database.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.database.mappers.DAOMapper_v1;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class DAOMapper_v1Test {

    DAOMapper_v1 mapper;

    @BeforeEach
    void setUp() {
        mapper = new DAOMapper_v1();
    }

    @Test
    void mapsCorrectly() {
        SR_Event evt = new SystemAddedEventBuilder().build();

        var actual = mapper.map(evt);

        assertThat(actual).usingRecursiveComparison().isEqualTo(evt);
    }
}
