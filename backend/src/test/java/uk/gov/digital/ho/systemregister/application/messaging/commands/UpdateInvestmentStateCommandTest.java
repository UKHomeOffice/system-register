package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

class UpdateInvestmentStateCommandTest {
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
    @ValueSource(strings = {"someValue"})
    void rejectsStringsContainingInvalidValues(String illegalString) {
        var command = new UpdateInvestmentStateCommand(ID, illegalString, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"evergreen", "invest", "maintain", "sunset", "decommissioned", "cancelled"})
    @NullSource
    void allowsInvestmentStateStringToBeNullOrContainSpecifiedValueFromList(String investmentState) {
        var command = new UpdateInvestmentStateCommand(ID, investmentState, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {" invest", "invest "})
    void extraneousSpacesAreRemovedFromInvestmentStateValue(String investmentStateWithSpaces) {
        var command = new UpdateInvestmentStateCommand(ID, investmentStateWithSpaces, AUTHOR, TIMESTAMP);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void raisesExceptionIfInvestmentStateValueIsUnchanged() {
        SR_System system = aSystem()
                .withId(456)
                .withInvestmentState("evergreen")
                .build();
        var command = new UpdateInvestmentStateCommand(456, "evergreen", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> command.ensureCommandUpdatesSystem(system))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("investment state is the same: evergreen");
    }
}
