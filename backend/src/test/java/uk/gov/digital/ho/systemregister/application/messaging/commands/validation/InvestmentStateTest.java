package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class InvestmentStateTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invested", "SUNSET"})
    void rejectsStringsOtherThanAllowedValues(String value) {
        var command = new TestCommand(value);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"evergreen", "invest", "maintain", "sunset", "decommissioned", "cancelled", "unknown"})
    @NullSource
    void allowsInvestmentStateStringToBeNullOrContainSpecifiedValueFromList(String value) {
        var command = new TestCommand(value);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @InvestmentState
        final String value;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String value) {
            this.value = value;
        }
    }
}
