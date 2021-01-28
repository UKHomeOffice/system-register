package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class SystemAliasesUpdatedEventTest {
    @Test
    void updatesAliases() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withAliases("some alias")
                .build();

        SystemAliasesUpdatedEvent event = new SystemAliasesUpdatedEvent(0, Set.of("a different alias"), null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withAliases("a different alias")
                        .build());
    }
}