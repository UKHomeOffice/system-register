package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
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

    @Test
    void validatesAliases() {
        assertThatField("aliases", UpdateSystemAliasesCommand.class)
                .hasTypeArgumentAnnotations(SystemName.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fname", "name\r\n", " name "})
    void extraneousSpacesAreRemoved(String nameWithSpaces) {
        var command = new UpdateSystemAliasesCommand(ID, List.of(nameWithSpaces), AUTHOR, TIMESTAMP);

        assertThat(command).usingRecursiveComparison()
                .isEqualTo(new UpdateSystemAliasesCommand(ID, List.of("name"), AUTHOR, TIMESTAMP));
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
                .withId(ID)
                .withAliases("alias 1", "alias 2")
                .build();
        var command = new UpdateSystemAliasesCommand(ID, List.of("alias 2", "alias 1"), AUTHOR, TIMESTAMP);

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system aliases are the same: [alias 1, alias 2]");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void willUpdateReturnsTrueIfExistingSystemAliasesIsNullOrEmptyAndCommandHasValues(List<String> existingAliases) {
        SR_System system = aSystem()
                .withId(ID)
                .withAliases(existingAliases)
                .build();
        var command = new UpdateSystemAliasesCommand(ID, List.of("alias 2", "alias 1"), AUTHOR, TIMESTAMP);

        var result = command.willUpdate(system);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonEmptyAliases")
    void willUpdateReturnsTrueIfExistingSystemAliasesHasDifferentValuesOrLengthToCommand(List<String> aliases) {
        SR_System system = aSystem()
                .withId(ID)
                .withAliases(List.of("alias 2", "alias 1"))
                .build();
        var command = new UpdateSystemAliasesCommand(ID, aliases, AUTHOR, TIMESTAMP);

        var result = command.willUpdate(system);

        assertThat(result).isTrue();
    }

    private static Stream<List<String>> nonEmptyAliases() {
        return Stream.of(
                List.of("alias 1"),
                List.of("alias 3"),
                List.of("alias3", "alias 4"),
                List.of("alias 1", "alias 3"),
                List.of("alias 1", "alias 2", "alias 3"),
                List.of("alias 3", "alias 4", "alias 5"));
    }
}
