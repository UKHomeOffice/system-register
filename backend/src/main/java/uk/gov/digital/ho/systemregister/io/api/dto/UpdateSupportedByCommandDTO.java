package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSupportedByCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

public class UpdateSupportedByCommandDTO {
    @JsonbProperty("supported_by")
    public String supportedBy;

    public UpdateSupportedByCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateSupportedByCommand(id, supportedBy, author, timestamp);
    }
}
