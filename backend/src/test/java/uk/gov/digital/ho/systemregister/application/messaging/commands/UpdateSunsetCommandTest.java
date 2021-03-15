package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.AdditionalInformation;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.Valid;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;


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

    @Test
    void willUpdateReturnsTrueWhenDateIsNull() {
        var cmd = new UpdateSunsetCommand(123, new UpdateSunsetCommand.Sunset(LocalDate.parse("2020-03-07"), "Not important"), aPerson().build(), Instant.now());
        SR_System system = aSystem().build();
        system.sunset.date = null;

        var result = cmd.willUpdate(system);

        assertThat(result).isTrue();
    }
}
