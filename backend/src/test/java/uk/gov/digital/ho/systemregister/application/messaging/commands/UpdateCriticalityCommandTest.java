package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.Criticality;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateCriticalityCommandTest {
    private static final int ID = 1;
    private static final SR_Person AUTHOR = aPerson()
            .withUsername("username")
            .withFirstName("forename")
            .withSurname("surname")
            .withEmail("mail@example.com")
            .build();
    private static final Instant TIMESTAMP = Instant.now();

    @Test
    void validatesCriticality() {
        assertThatField("criticality", UpdateCriticalityCommand.class)
                .hasAnnotations(Criticality.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fvalue", "value\r\n", " value "})
    void extraneousSpacesAreRemoved(String valueWithSpaces) {
        var command = new UpdateCriticalityCommand(ID, valueWithSpaces, AUTHOR, TIMESTAMP);

        assertThat(command).usingRecursiveComparison()
                .isEqualTo(new UpdateCriticalityCommand(ID, "value", AUTHOR, TIMESTAMP));
    }

    @Test
    void raisesExceptionIfCriticalityValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(456)
                .withCriticality("high")
                .build();
        var command = new UpdateCriticalityCommand(ID, "high", AUTHOR, TIMESTAMP);

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("criticality level is the same: high");
    }
}
