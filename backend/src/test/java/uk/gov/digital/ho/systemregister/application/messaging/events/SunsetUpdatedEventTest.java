package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder.aSunset;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class SunsetUpdatedEventTest {
    @Test
    void updatesSunset() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withSunset(aSunset().withDate(LocalDate.parse("2021-06-01")).withAdditionalInformation("sunset info"))
                .build();

        var event = new SunsetUpdatedEvent(
                0,
                aSunset().withDate(LocalDate.parse("2021-09-01")).withAdditionalInformation("updated sunset info").build(),
                null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withSunset(aSunset().withDate(LocalDate.parse("2021-09-01")).withAdditionalInformation("updated sunset info"))
                        .build());
    }
}
