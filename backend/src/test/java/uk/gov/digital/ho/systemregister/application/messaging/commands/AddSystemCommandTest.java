package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.Criticality;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.Portfolio;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemDescription;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.RiskBuilder.aHighRisk;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.aMinimalAddSystemCommand;

class AddSystemCommandTest {
    private static Stream<Arguments> fieldValidators() {
        return Stream.of(
                Arguments.of("name", SystemName.class),
                Arguments.of("description", SystemDescription.class),
                Arguments.of("portfolio", Portfolio.class),
                Arguments.of("criticality", Criticality.class));
    }

    @ParameterizedTest
    @MethodSource("fieldValidators")
    void validatesFields(String field, Class<? extends Annotation> annotation) {
        assertThatField(field, AddSystemCommand.class)
                .hasAnnotations(annotation);
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
