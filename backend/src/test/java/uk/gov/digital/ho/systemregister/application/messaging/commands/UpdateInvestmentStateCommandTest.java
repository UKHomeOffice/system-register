package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

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
        var command = new UpdateInvestmentStateCommand(AUTHOR, TIMESTAMP, ID, illegalString);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"evergreen", "invest", "maintain", "sunset", "decommissioned", "cancelled"})
    @NullSource
    void allowsInvestmentStateStringToBeNullOrContainSpecifiedValueFromList(String investmentState) {
        var command = new UpdateInvestmentStateCommand(AUTHOR, TIMESTAMP, ID, investmentState);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {" invest", "invest "})
    void extraneousSpacesAreRemovedFromInvestmentStateValue(String investmentStateWithSpaces) {
        var command = new UpdateInvestmentStateCommand(AUTHOR, TIMESTAMP, ID, investmentStateWithSpaces);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }
}
