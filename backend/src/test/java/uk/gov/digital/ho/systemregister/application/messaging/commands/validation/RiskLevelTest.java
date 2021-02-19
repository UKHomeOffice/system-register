package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class RiskLevelTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"not-allowed", "HIGH"})
    void rejectsStringsOtherThanAllowedValues(String value) {
        var command = new TestCommand(value);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"low", "medium", "high", "not_applicable", "unknown"})
    @NullSource
    void allowsRiskLevelToBeNullOrContainSpecifiedValueFromList(String value) {
        var command = new TestCommand(value);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @RiskLevel
        final String level;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String level) {
            this.level = level;
        }
    }
}
