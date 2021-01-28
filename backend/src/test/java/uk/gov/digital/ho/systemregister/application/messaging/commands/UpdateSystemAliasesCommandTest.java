package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSystemAliasesCommandTest {
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
    @NullSource
    void systemAliasValuesMustNotBeTooShort(String alias) {
        var command = new UpdateSystemAliasesCommand(ID, singletonList(alias), AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {" x", "x "})
    void extraneousSpacesDoNotCountTowardsTheMinimumCharacterCount(String alias) {
        var command = new UpdateSystemAliasesCommand(ID, List.of(alias), AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"!", "£", "$", "%", "^", "*", "|", "<", ">", "~", "\"", "="})
    void rejectsStringsContainingInvalidSpecialCharacters(String illegalCharacter) {
        String alias = "alias" + illegalCharacter;
        var command = new UpdateSystemAliasesCommand(ID, List.of(alias), AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xy", "?-", "some alias"})
    void allowsSystemAliasStringToContainTwoOrMoreCharacters(String alias) {
        var command = new UpdateSystemAliasesCommand(ID, List.of(alias), AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void doesNotAllowListOfAliasesToBeNull() {
        var command = new UpdateSystemAliasesCommand(ID, null, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }


    @Test
    void raisesExceptionIfSystemAliasesValuesAreAllUnchanged() {
        SR_System system = aSystem()
                .withId(345)
                .withAliases("alias 1", "alias 2")
                .build();
        var command = new UpdateSystemAliasesCommand(345, List.of("alias 2", "alias 1"), aPerson().build(), Instant.now());

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system aliases are the same: [alias 1, alias 2]");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void willUpdateReturnsTrueIfExistingSystemAliasesIsNullOrEmptyAndCommandHasValues(List<String> existingAliases) {
        SR_System system = aSystem()
                .withId(345)
                .withAliases(existingAliases)
                .build();
        var command = new UpdateSystemAliasesCommand(345, List.of("alias 2", "alias 1"), aPerson().build(), Instant.now());

        var result = command.willUpdate(system);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonEmptyAliases")
    void willUpdateReturnsTrueIfExistingSystemAliasesHasDifferentValuesOrLengthToCommand(List<String> aliases) {
        SR_System system = aSystem()
                .withId(345)
                .withAliases(List.of("alias 2", "alias 1"))
                .build();
        var command = new UpdateSystemAliasesCommand(345, aliases, aPerson().build(), Instant.now());

        var result = command.willUpdate(system);

        assertThat(result).isTrue();
    }

    static Stream<List<String>> nonEmptyAliases() {
        return Stream.of(
                List.of("alias 1"),
                List.of("alias 3"),
                List.of("alias3", "alias 4"),
                List.of("alias 1", "alias 3"),
                List.of("alias 1", "alias 2", "alias 3"),
                List.of("alias 3", "alias 4", "alias 5"));
    }
}
