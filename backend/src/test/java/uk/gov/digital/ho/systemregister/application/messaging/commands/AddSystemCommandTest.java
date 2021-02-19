package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
                Arguments.of("criticality", Criticality.class),
                Arguments.of("investmentState", InvestmentState.class),
                Arguments.of("businessOwner", ContactName.class),
                Arguments.of("serviceOwner", ContactName.class),
                Arguments.of("technicalOwner", ContactName.class),
                Arguments.of("productOwner", ContactName.class),
                Arguments.of("informationAssetOwner", ContactName.class),
                Arguments.of("developedBy", EntityName.class),
                Arguments.of("supportedBy", EntityName.class));
    }

    private static Stream<Arguments> riskFieldValidators() {
        return Stream.of(
                Arguments.of("name", RiskName.class),
                Arguments.of("level", RiskLevel.class),
                Arguments.of("rationale", RiskRationale.class));
    }

    @ParameterizedTest
    @MethodSource("fieldValidators")
    void validatesFields(String field, Class<? extends Annotation> annotation) {
        assertThatField(field, AddSystemCommand.class)
                .hasAnnotations(annotation);
    }

    @Test
    void validatesAliases() {
        assertThatField("aliases", AddSystemCommand.class)
                .hasTypeArgumentAnnotations(SystemName.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"aliases", "risks"})
    void listsCannotBeNull(String field) {
        assertThatField(field, AddSystemCommand.class)
                .hasAnnotations(NotNull.class);
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

    @Nested
    class Risks {
        @Test
        void validatesRisks() {
            assertThatField("risks", AddSystemCommand.class)
                    .hasTypeArgumentAnnotations(Valid.class);
        }

        @ParameterizedTest
        @MethodSource("uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommandTest#riskFieldValidators")
        void validatesFields(String field, Class<? extends Annotation> annotation) {
            assertThatField(field, AddSystemCommand.Risk.class)
                    .hasAnnotations(annotation);
        }
    }
}
