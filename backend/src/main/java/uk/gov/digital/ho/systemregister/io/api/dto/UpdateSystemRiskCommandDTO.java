package uk.gov.digital.ho.systemregister.io.api.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemRiskCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

@SuppressFBWarnings(
        value = {"UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"},
        justification = "Fields are deserialized by Jsonb"
)
public class UpdateSystemRiskCommandDTO {
    public String name;
    public String level;
    public String rationale;

    public UpdateSystemRiskCommand toCommand(int id, SR_Person author, Instant timestamp) {
        var risk = new UpdateSystemRiskCommand.Risk(name, level, rationale);
        return new UpdateSystemRiskCommand(id, risk, author, timestamp);
    }
}
