package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class CriticalityTest {
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
    @ValueSource(strings = {"high", "low", "medium", "cni", "unknown"})
    @NullSource
    void allowsCriticalityStringToBeNullOrContainSpecifiedValueFromList(String value) {
        var command = new TestCommand(value);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @Criticality
        final String value;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String value) {
            this.value = value;
        }
    }
}
