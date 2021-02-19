package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class RiskNameTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", " "})
    @NullAndEmptySource
    void riskNameMustNotBeTooShort(String name) {
        var command = new TestCommand(name);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"!", "£", "$", "%", "^", "*", "|", "<", ">", "~", "\"", "="})
    void rejectsStringsContainingInvalidSpecialCharacters(String illegalCharacter) {
        var command = new TestCommand("name" + illegalCharacter);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "Name of risk"})
    void allowsRiskNameStringToContainTwoOrMoreCharacters(String name) {
        var command = new TestCommand(name);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @RiskName
        final String name;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String name) {
            this.name = name;
        }
    }
}
