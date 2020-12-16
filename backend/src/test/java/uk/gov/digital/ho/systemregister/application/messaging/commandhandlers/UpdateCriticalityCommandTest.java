package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCriticalityCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class UpdateCriticalityCommandTest {
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
    void rejectsStringsContainingInvalidSpecialCharacters(String illegalString) {
        var command = new UpdateCriticalityCommand(AUTHOR, TIMESTAMP, ID, illegalString);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"high", "low", "medium", "cni", "High", "Low", "Medium", "CNI", "UNKNOWN"})
    @NullSource
    void allowsProductOwnerStringToBeNullOrContainTwoOrMoreCharacters(String criticality) {
        var command = new UpdateCriticalityCommand(AUTHOR, TIMESTAMP, ID, criticality);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {" high", "High "})
    void extraneousSpaceAreRemovedFromCriticalityValue(String criticalityWithSpaces) {
        var command = new UpdateCriticalityCommand(AUTHOR, TIMESTAMP, ID, criticalityWithSpaces);

        var constraintViolations = validator.validate(command);

        assertThat(constraintViolations).isEmpty();
    }
}