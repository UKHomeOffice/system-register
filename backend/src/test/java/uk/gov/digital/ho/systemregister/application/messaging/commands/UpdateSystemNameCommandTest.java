package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSystemNameCommandTest {
    private static final int ID = 1;
    private static final SR_Person AUTHOR = aPerson()
            .withUsername("username")
            .withFirstName("forename")
            .withSurname("surname")
            .withEmail("mail@example.com")
            .build();
    private static final Instant TIMESTAMP = Instant.now();

    @Test
    void validatesSystemName() {
        assertThatField("name", UpdateSystemNameCommand.class)
                .hasAnnotations(SystemName.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fname", "name\r\n", " name "})
    void extraneousSpacesAreRemoved(String nameWithSpaces) {
        var command = new UpdateSystemNameCommand(ID, nameWithSpaces, AUTHOR, TIMESTAMP);

        assertThat(command).usingRecursiveComparison()
                .isEqualTo(new UpdateSystemNameCommand(ID, "name", AUTHOR, TIMESTAMP));
    }

    @Test
    void raisesExceptionIfSystemNameValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(ID)
                .withName("original system name")
                .build();
        var command = new UpdateSystemNameCommand(ID, "original system name", AUTHOR, TIMESTAMP);

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system name is the same: original system name");
    }
}
