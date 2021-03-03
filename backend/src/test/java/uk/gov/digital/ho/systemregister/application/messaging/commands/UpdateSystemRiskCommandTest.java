package uk.gov.digital.ho.systemregister.application.messaging.commands;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.RiskLevel;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.RiskName;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.RiskRationale;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.assertions.FieldAssert.assertThatField;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class UpdateSystemRiskCommandTest {

    @Test
    void riskCannotBeNullAndMustBeValid() {
        assertThatField("risk", UpdateSystemRiskCommand.class)
                .hasAnnotations(NotNull.class, Valid.class);
    }

    @Test
    void validatesRiskName() {
        assertThatField("name", UpdateSystemRiskCommand.Risk.class)
                .hasAnnotations(RiskName.class);
    }

    @Test
    void validatesRiskLevel() {
        assertThatField("level", UpdateSystemRiskCommand.Risk.class)
                .hasAnnotations(RiskLevel.class);
    }

    @Test
    void validatesRiskRationale() {
        assertThatField("rationale", UpdateSystemRiskCommand.Risk.class)
                .hasAnnotations(RiskRationale.class);
    }

    @Test
    void trimsValuesForRiskFields() {
        var timeStamp = Instant.now();
        var actual = new UpdateSystemRiskCommand(123, new UpdateSystemRiskCommand.Risk("   some name   ", "   low   ", "   some reason   "), aPerson().build(), timeStamp);
        var expected = new UpdateSystemRiskCommand(123, new UpdateSystemRiskCommand.Risk("some name", "low", "some reason"), aPerson().build(), timeStamp);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
