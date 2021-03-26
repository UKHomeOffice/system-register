package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicFacingTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"public", "INTERNALSYSTEM"})
    void rejectsStringsOtherThanAllowedValues(String value) {
        var command = new TestCommand(value);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"publicfacing", "internalsystem", "unknown"})
    @NullSource
    void allowsPublicFacingStringToBeNullOrContainSpecifiedValueFromList(String value) {
        var command = new TestCommand(value);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @PublicFacing
        final String value;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String value) {
            this.value = value;
        }
    }
}
