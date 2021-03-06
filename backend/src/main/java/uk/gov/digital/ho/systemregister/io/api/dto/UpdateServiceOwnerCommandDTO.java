package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateServiceOwnerCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

public class UpdateServiceOwnerCommandDTO {
    @JsonbProperty("service_owner")
    public String serviceOwner;

    public UpdateServiceOwnerCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateServiceOwnerCommand(id, serviceOwner, author, timestamp);
    }
}
