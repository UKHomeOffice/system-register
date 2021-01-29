package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemAliasesCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;
import java.util.List;

public class UpdateSystemAliasesCommandDTO {
    public List<String> aliases;

    public UpdateSystemAliasesCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateSystemAliasesCommand(id, aliases, author, timestamp);
    }
}
