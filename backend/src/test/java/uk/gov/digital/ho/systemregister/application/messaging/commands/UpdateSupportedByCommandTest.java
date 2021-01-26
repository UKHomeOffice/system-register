package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSupportedByCommandTest {
    private static final int ID = 2;
    private static final SR_Person AUTHOR = aPerson()
            .withUsername("username")
            .withFirstName("forename")
            .withSurname("surname")
            .withEmail("mail@example.com")
            .build();
    private static final Instant TIMESTAMP = Instant.now();

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", " "})
    @EmptySource
    void supporterMustNotBeTooShort(String supportedBy) {
        var command = new UpdateSupportedByCommand(ID, supportedBy, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {" x", "x "})
    void extraneousSpacesDoNotCountTowardsTheMinimumCharacterCount(String supportedBy) {
        var command = new UpdateSupportedByCommand(ID, supportedBy, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"!", "£", "$", "%", "^", "*", "|", "<", ">", "~", "\"", "="})
    void rejectsStringsContainingInvalidSpecialCharacters(String illegalCharacter) {
        String supportedBy = "name" + illegalCharacter;
        var command = new UpdateSupportedByCommand(ID, supportedBy, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "Name of person"})
    @NullSource
    void allowsSupportedByStringToBeNullOrContainTwoOrMoreCharacters(String supportedBy) {
        var command = new UpdateSupportedByCommand(ID, supportedBy, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void raisesExceptionIfSupportedByValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(567)
                .withSupportedBy("supporter")
                .build();
        var command = new UpdateSupportedByCommand(567, "supporter", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("supporter is the same: supporter");
    }
}
