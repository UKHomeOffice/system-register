package uk.gov.digital.ho.systemregister.io.api.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCriticalityCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

@SuppressFBWarnings(
        value = "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD",
        justification = "Fields are deserialized by Jsonb"
)
public class UpdateCriticalityCommandDTO {
    public String criticality;

    public UpdateCriticalityCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateCriticalityCommand(id, criticality, author, timestamp);
    }
}
