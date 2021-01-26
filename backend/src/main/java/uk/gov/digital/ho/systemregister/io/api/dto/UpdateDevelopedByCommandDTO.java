package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateDevelopedByCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

public class UpdateDevelopedByCommandDTO {
    @JsonbProperty("developed_by")
    public String developedBy;

    public UpdateDevelopedByCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateDevelopedByCommand(id, developedBy, author, timestamp);
    }
}
