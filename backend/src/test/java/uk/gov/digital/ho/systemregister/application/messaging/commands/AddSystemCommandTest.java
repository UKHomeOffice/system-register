package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemDescription;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.RiskBuilder.aHighRisk;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.aMinimalAddSystemCommand;

class AddSystemCommandTest {
    @Test
    void validatesSystemName() {
        assertThatField("name", AddSystemCommand.class)
                .hasAnnotations(SystemName.class);
    }

    @Test
    void validatesSystemDescription() {
        assertThatField("description", AddSystemCommand.class)
                .hasAnnotations(SystemDescription.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fvalue", "value\r\n", " value "})
    void extraneousSpacesAreRemoved(String valueWithSpaces) {
        var command = aMinimalAddSystemCommand()
                .withName(valueWithSpaces)
                .withDescription(valueWithSpaces)
                .withPortfolio(valueWithSpaces)
                .withCriticality(valueWithSpaces)
                .withInvestmentState(valueWithSpaces)
                .withBusinessOwner(valueWithSpaces)
                .withServiceOwner(valueWithSpaces)
                .withTechnicalOwner(valueWithSpaces)
                .withProductOwner(valueWithSpaces)
                .withInformationAssetOwner(valueWithSpaces)
                .withSupportedBy(valueWithSpaces)
                .withDevelopedBy(valueWithSpaces)
                .withAliases(valueWithSpaces)
                .withRisks(aHighRisk()
                        .withName(valueWithSpaces)
                        .withRationale(valueWithSpaces))
                .build();

        assertThat(command).usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(aMinimalAddSystemCommand()
                        .withName("value")
                        .withDescription("value")
                        .withPortfolio("value")
                        .withCriticality("value")
                        .withInvestmentState("value")
                        .withBusinessOwner("value")
                        .withServiceOwner("value")
                        .withTechnicalOwner("value")
                        .withProductOwner("value")
                        .withInformationAssetOwner("value")
                        .withSupportedBy("value")
                        .withDevelopedBy("value")
                        .withAliases("value")
                        .withRisks(aHighRisk()
                                .withName("value")
                                .withRationale("value"))
                        .build());
    }
}
