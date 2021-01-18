package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInvestmentStateCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;
import javax.json.bind.annotation.JsonbProperty;

public class UpdateInvestmentStateCommandDTO {
    @JsonbProperty("investment_state")
    public String investmentState;

    public UpdateInvestmentStateCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateInvestmentStateCommand(id, investmentState, author, timestamp);
    }
}
