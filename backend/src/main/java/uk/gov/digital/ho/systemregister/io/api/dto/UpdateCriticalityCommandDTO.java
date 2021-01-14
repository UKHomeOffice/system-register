package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCriticalityCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class UpdateCriticalityCommandDTO {
    public String criticality;

    public UpdateCriticalityCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateCriticalityCommand(author, timestamp, id, criticality);
    }
}
