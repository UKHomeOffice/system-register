package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateBusinessOwnerCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

public class UpdateBusinessOwnerCommandDTO {
    @JsonbProperty("business_owner")
    public String businessOwner;

    public UpdateBusinessOwnerCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateBusinessOwnerCommand(id, businessOwner, author, timestamp);
    }
}
