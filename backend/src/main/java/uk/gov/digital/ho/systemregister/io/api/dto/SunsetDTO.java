package uk.gov.digital.ho.systemregister.io.api.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;

@SuppressFBWarnings(
        value = {"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"},
        justification = "Fields are deserialized by Jsonb"
)
public class SunsetDTO {
    public LocalDate date;
    @JsonbProperty("additional_information")
    public String additionalInformation;

    @SuppressWarnings("unused")
    public SunsetDTO() {}


    public SunsetDTO(LocalDate date, String additionalInformation) {
        this.date = date;
        this.additionalInformation = additionalInformation;
    }
}
