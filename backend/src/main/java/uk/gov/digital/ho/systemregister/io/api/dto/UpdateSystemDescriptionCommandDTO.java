package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemDescriptionCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class UpdateSystemDescriptionCommandDTO {
    public String description;

    public UpdateSystemDescriptionCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateSystemDescriptionCommand(id, description, author, timestamp);
    }
}