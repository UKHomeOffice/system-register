package uk.gov.digital.ho.systemregister.io.api.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSunsetCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

import static java.time.LocalDate.parse;

@SuppressFBWarnings(
        value = {"UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"},
        justification = "Fields are deserialized by Jsonb"
)
public class UpdateSunsetCommandDTO {
    public String date;
    @JsonbProperty("additional_information")
    public String additionalInformation;

    public UpdateSunsetCommand toCommand(int id, SR_Person author, Instant timestamp) {
        var sunset = new UpdateSunsetCommand.Sunset(parse(date), additionalInformation);
        return new UpdateSunsetCommand(id, sunset, author, timestamp);
    }
}
