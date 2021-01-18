package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemNameCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class UpdateSystemNameCommandDTO {
    public String name;

    public UpdateSystemNameCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateSystemNameCommand(id, name, author, timestamp);
    }
}
