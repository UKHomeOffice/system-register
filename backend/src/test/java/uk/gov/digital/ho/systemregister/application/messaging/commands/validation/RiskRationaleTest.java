package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class RiskRationaleTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", " "})
    @NullAndEmptySource
    void rationaleMustNotBeTooShortOrMissing(String rationale) {
        var command = new TestCommand(rationale);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "Rationale for risk"})
    void allowsRationaleStringToContainTwoOrMoreCharacters(String rationale) {
        var command = new TestCommand(rationale);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @RiskRationale
        final String rationale;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String rationale) {
            this.rationale = rationale;
        }
    }
}
