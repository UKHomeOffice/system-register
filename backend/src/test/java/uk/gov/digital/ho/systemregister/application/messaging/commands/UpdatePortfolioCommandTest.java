package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.Portfolio;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdatePortfolioCommandTest {
    private static final int ID = 1;
    private static final SR_Person AUTHOR = aPerson()
            .withUsername("username")
            .withFirstName("forename")
            .withSurname("surname")
            .withEmail("mail@example.com")
            .build();
    private static final Instant TIMESTAMP = Instant.now();

    @Test
    void validatesPortfolio() {
        assertThatField("portfolio", UpdatePortfolioCommand.class)
                .hasAnnotations(Portfolio.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fname", "name\r\n", " name "})
    void extraneousSpacesAreRemoved(String nameWithSpaces) {
        var command = new UpdatePortfolioCommand(ID, nameWithSpaces, AUTHOR, TIMESTAMP);

        assertThat(command).usingRecursiveComparison()
                .isEqualTo(new UpdatePortfolioCommand(ID, "name", AUTHOR, TIMESTAMP));
    }

    @Test
    void raisesExceptionIfPortfolioValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(345)
                .withPortfolio("original portfolio")
                .build();
        var command = new UpdatePortfolioCommand(345, "original portfolio", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("portfolio is the same: original portfolio");
    }
}
