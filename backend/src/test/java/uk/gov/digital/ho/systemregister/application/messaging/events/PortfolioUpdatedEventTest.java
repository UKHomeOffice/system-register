package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class PortfolioUpdatedEventTest {
    @Test
    void updatesPortfolio() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withPortfolio("A portfolio")
                .build();

        var event = new PortfolioUpdatedEvent(0, "Another portfolio", null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withPortfolio("Another portfolio")
                        .build());
    }
}
