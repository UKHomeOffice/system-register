package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.aMinimalAddSystemCommand;

class AddSystemCommandTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", " "})
    @EmptySource
    @NullSource
    void systemNameMustNotBeTooShort(String systemName) {
        var command = aMinimalAddSystemCommand()
                .withName(systemName)
                .build();

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {" x", "x ", " x "})
    void extraneousSpacesDoNotCountTowardsTheMinimumCharacterCount(String systemName) {
        var command = aMinimalAddSystemCommand()
                .withName(systemName)
                .build();

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"!", "£", "$", "%", "^", "*", "|", "<", ">", "~", "\"", "="})
    void rejectsStringsContainingInvalidSpecialCharacters(String illegalCharacter) {
        var command = aMinimalAddSystemCommand()
                .withName("name" + illegalCharacter)
                .build();

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "Name of person"})
    void allowsSystemNameStringToContainTwoOrMoreCharacters(String systemName) {
        var command = aMinimalAddSystemCommand()
                .withName(systemName)
                .build();

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }
}
