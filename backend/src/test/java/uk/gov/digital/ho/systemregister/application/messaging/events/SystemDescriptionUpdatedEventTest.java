package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class SystemDescriptionUpdatedEventTest {
    @Test
    void updatesDescription() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withDescription("Some description")
                .build();

        SystemDescriptionUpdatedEvent event = new SystemDescriptionUpdatedEvent(0, "New description", null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withDescription("New description")
                        .build());
    }
}