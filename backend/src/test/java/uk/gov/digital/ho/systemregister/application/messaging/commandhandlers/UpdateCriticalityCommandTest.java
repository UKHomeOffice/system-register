package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import org.junit.jupiter.api.BeforeEach;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.Instant;

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



}