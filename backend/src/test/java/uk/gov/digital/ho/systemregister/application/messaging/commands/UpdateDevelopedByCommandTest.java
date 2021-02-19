package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.EntityName;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateDevelopedByCommandTest {
    private static final int ID = 1;
    private static final SR_Person AUTHOR = aPerson()
            .withUsername("username")
            .withFirstName("forename")
            .withSurname("surname")
            .withEmail("mail@example.com")
            .build();
    private static final Instant TIMESTAMP = Instant.now();

    @Test
    void validatesDevelopedBy() {
        assertThatField("developedBy", UpdateDevelopedByCommand.class)
                .hasAnnotations(EntityName.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fname", "name\r\n", " name "})
    void extraneousSpacesAreRemoved(String nameWithSpaces) {
        var command = new UpdateDevelopedByCommand(ID, nameWithSpaces, AUTHOR, TIMESTAMP);

        assertThat(command).usingRecursiveComparison()
                .isEqualTo(new UpdateDevelopedByCommand(ID, "name", AUTHOR, TIMESTAMP));
    }

    @Test
    void raisesExceptionIfDevelopedByValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(ID)
                .withDevelopedBy("developer")
                .build();
        var command = new UpdateDevelopedByCommand(ID, "developer", AUTHOR, TIMESTAMP);

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("developed by is the same: developer");
    }
}
