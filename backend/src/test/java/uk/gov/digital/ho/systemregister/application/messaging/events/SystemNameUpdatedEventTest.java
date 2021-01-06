package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class SystemNameUpdatedEventTest {

    @Test
    void updatesSystemName() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withName("original system name")
                .build();

        SystemNameUpdatedEvent event = new SystemNameUpdatedEvent(0, "updated system name", null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withName("updated system name")
                        .build());
    }
}