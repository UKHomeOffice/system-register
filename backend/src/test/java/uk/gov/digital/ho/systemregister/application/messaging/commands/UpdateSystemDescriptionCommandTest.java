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

class UpdateSystemDescriptionCommandTest {
    private static final int ID = 1;
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
    void descriptionMustNotBeTooShort(String description) {
        var command = new UpdateSystemDescriptionCommand(ID, description, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {" x", "x "})
    void extraneousSpacesDoNotCountTowardsTheMinimumCharacterCount(String description) {
        var command = new UpdateSystemDescriptionCommand(ID, description, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "Description of system"})
    @NullSource
    void allowsDescriptionStringToBeNullOrContainTwoOrMoreCharacters(String description) {
        var command = new UpdateSystemDescriptionCommand(ID, description, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void raisesExceptionIfDescriptionValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(456)
                .withDescription("description")
                .build();
        var command = new UpdateSystemDescriptionCommand(456, "description", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system description is the same: description");
    }
}
