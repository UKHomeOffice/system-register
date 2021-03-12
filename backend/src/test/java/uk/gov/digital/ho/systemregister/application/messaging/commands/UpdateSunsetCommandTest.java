package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.AdditionalInformation;

import javax.validation.Valid;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class UpdateSunsetCommandTest {
    @Test
    void sunsetMustBeValid() {
        assertThatField("sunset", UpdateSunsetCommand.class)
                .hasAnnotations(Valid.class);
    }

    @Test
    void sunsetAdditionalInformationMustBeValid() {
        assertThatField("additionalInformation", UpdateSunsetCommand.Sunset.class)
                .hasAnnotations(AdditionalInformation.class);
    }

    @Test
    void trimsValuesForSunsetAdditionalInfoField() {
        var timeStamp = Instant.now();
        var actual = new UpdateSunsetCommand(
                123,
                new UpdateSunsetCommand.Sunset(LocalDate.parse("2020-03-07"), "     some information    "),
                aPerson().build(),
                timeStamp);
        var expected = new UpdateSunsetCommand(
                123,
                new UpdateSunsetCommand.Sunset(LocalDate.parse("2020-03-07"), "some information"),
                aPerson().build(),
                timeStamp);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
