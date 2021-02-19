package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.ContactName;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateBusinessOwnerCommandTest {
    private static final int ID = 1;
    private static final SR_Person AUTHOR = aPerson()
            .withUsername("username")
            .withFirstName("forename")
            .withSurname("surname")
            .withEmail("mail@example.com")
            .build();
    private static final Instant TIMESTAMP = Instant.now();

    @Test
    void validatesBusinessOwner() {
        assertThatField("businessOwner", UpdateBusinessOwnerCommand.class)
                .hasAnnotations(ContactName.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fname", "name\r\n", " name "})
    void extraneousSpacesAreRemoved(String nameWithSpaces) {
        var command = new UpdateBusinessOwnerCommand(ID, nameWithSpaces, AUTHOR, TIMESTAMP);

        assertThat(command).usingRecursiveComparison()
                .isEqualTo(new UpdateBusinessOwnerCommand(ID, "name", AUTHOR, TIMESTAMP));
    }

    @Test
    void raisesExceptionIfOwnerValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(ID)
                .withBusinessOwner("owner")
                .build();
        var command = new UpdateBusinessOwnerCommand(ID, "owner", AUTHOR, TIMESTAMP);

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("business owner is the same: owner");
    }
}
