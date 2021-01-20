package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateTechnicalOwnerCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

public class UpdateTechnicalOwnerCommandDTO {
    @JsonbProperty("technical_owner")
    public String technicalOwner;

    public UpdateTechnicalOwnerCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateTechnicalOwnerCommand(id, technicalOwner, author, timestamp);
    }
}
