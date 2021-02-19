package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class PortfolioTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", " "})
    @EmptySource
    void portfolioMustNotBeTooShort(String portfolio) {
        var command = new TestCommand(portfolio);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @Test
    void portfolioMustNotBeMissing() {
        var command = new TestCommand(null);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "portfolio"})
    void allowsPortfolioStringToContainTwoOrMoreCharacters(String portfolio) {
        var command = new TestCommand(portfolio);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    private static class TestCommand {
        @Portfolio
        final String name;

        @SuppressWarnings("CdiInjectionPointsInspection")
        TestCommand(String name) {
            this.name = name;
        }
    }
}
