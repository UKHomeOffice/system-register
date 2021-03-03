package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_RiskBuilder;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class SystemRiskUpdatedEventTest {
    @Test
    void updatesRisks() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withRisks(SR_RiskBuilder.aHighRisk().withName("existing risk").withRationale("some reason"))
                .build();

        SystemRiskUpdatedEvent event = new SystemRiskUpdatedEvent(0, new SR_Risk("existing risk", "low", "a different reason"), null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withRisks(SR_RiskBuilder.aLowRisk().withName("existing risk").withRationale("a different reason"))
                        .build());
    }
}
