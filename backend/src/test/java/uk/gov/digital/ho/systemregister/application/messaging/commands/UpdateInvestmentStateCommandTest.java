package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.InvestmentState;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateInvestmentStateCommandTest {
    private static final int ID = 1;
    private static final SR_Person AUTHOR = aPerson()
            .withUsername("username")
            .withFirstName("forename")
            .withSurname("surname")
            .withEmail("mail@example.com")
            .build();
    private static final Instant TIMESTAMP = Instant.now();

    @Test
    void validatesInvestmentState() {
        assertThatField("investmentState", UpdateInvestmentStateCommand.class)
                .hasAnnotations(InvestmentState.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fvalue", "value\r\n", " value "})
    void extraneousSpacesAreRemoved(String valueWithSpaces) {
        var command = new UpdateInvestmentStateCommand(ID, valueWithSpaces, AUTHOR, TIMESTAMP);

        assertThat(command).usingRecursiveComparison()
                .isEqualTo(new UpdateInvestmentStateCommand(ID, "value", AUTHOR, TIMESTAMP));
    }

    @Test
    void raisesExceptionIfInvestmentStateValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(ID)
                .withInvestmentState("evergreen")
                .build();
        var command = new UpdateInvestmentStateCommand(ID, "evergreen", AUTHOR, TIMESTAMP);

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("investment state is the same: evergreen");
    }
}
