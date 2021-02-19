package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.aMinimalAddSystemCommand;

class AddSystemCommandTest {
    @Test
    void validatesSystemName() {
        assertThatField("name", AddSystemCommand.class)
                .hasAnnotations(SystemName.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\t\fvalue", "value\r\n", " value "})
    void extraneousSpacesAreRemoved(String valueWithSpaces) {
        var command = aMinimalAddSystemCommand()
                .withName(valueWithSpaces)
                .build();

        assertThat(command).usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(aMinimalAddSystemCommand()
                        .withName("value")
                        .build());
    }
}
