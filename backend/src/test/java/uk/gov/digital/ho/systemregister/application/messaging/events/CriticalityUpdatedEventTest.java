package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class CriticalityUpdatedEventTest {

    @Test
    void updatesCriticality() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withCriticality("medium")
                .build();

        CriticalityUpdatedEvent event = new CriticalityUpdatedEvent(null, null, 0, "cni");
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withCriticality("cni")
                        .build());
    }
}