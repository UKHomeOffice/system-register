package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class ServiceOwnerUpdatedEventTest {
    @Test
    void updatesServiceOwner() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withServiceOwner("An owner")
                .build();

        var event = new ServiceOwnerUpdatedEvent(0, "Another owner", null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withServiceOwner("Another owner")
                        .build());
    }
}
