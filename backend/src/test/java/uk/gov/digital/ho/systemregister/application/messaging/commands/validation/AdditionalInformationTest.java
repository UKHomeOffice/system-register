package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class AdditionalInformationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", " "})
    @EmptySource
    void additionalInformationMustNotBeTooShort(String additionalInfo) {
        var command = new TestCommand(additionalInfo);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "Description of system"})
    @NullSource
    void allowsAdditionalInformationStringToBeNullOrContainTwoOrMoreCharacters(String additionalInfo) {
        var command = new TestCommand(additionalInfo);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @AdditionalInformation
        final String additionalInfo;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String additionalInfo) {
                this.additionalInfo = additionalInfo;
        }
    }
}
