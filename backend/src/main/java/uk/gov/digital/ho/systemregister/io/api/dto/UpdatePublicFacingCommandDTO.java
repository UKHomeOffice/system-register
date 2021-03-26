package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInvestmentStateCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdatePublicFacingCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

public class UpdatePublicFacingCommandDTO {
    @JsonbProperty("public_facing")
    public String publicFacing;

    public UpdatePublicFacingCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdatePublicFacingCommand(id, publicFacing, author, timestamp);
    }
}
