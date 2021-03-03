package uk.gov.digital.ho.systemregister.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.domain.SR_RiskBuilder.aLowRisk;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

public class SR_SystemTest {
    @Test
    void returnsRiskWithMatchingName() {
        var system = aSystem()
                .withRisks(aLowRisk().withName("existing risk"))
                .build();

        var returnedRisk = system.getRiskByName("existing risk");

        assertThat(returnedRisk).contains(aLowRisk().withName("existing risk").build());
    }

    @Test
    void returnsEmptyIfNoMatchingRisk() {
        var system = aSystem()
                .withRisks(aLowRisk().withName("existing risk"))
                .build();

        var returnedRisk = system.getRiskByName("a different risk");

        assertThat(returnedRisk).isEmpty();
    }

    @Test
    void returnsEmptyIfSystemHasNoRisks() {
        var system = aSystem().withNoRisks().build();

        var returnedRisk = system.getRiskByName("any risk");

        assertThat(returnedRisk).isEmpty();
    }
}
