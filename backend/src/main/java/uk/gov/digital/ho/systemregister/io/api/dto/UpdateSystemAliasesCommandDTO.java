package uk.gov.digital.ho.systemregister.io.api.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemAliasesCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;
import java.util.List;

@SuppressFBWarnings(
        value = "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD",
        justification = "Fields are deserialized by Jsonb"
)
public class UpdateSystemAliasesCommandDTO {
    public List<String> aliases;

    public UpdateSystemAliasesCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateSystemAliasesCommand(id, aliases, author, timestamp);
    }
}
