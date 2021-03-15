package uk.gov.digital.ho.systemregister.io.api.dto;

import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;

public class SunsetDTO {
    public String date;
    @JsonbProperty("additional_information")
    public String additionalInformation;

    @SuppressWarnings("unused")
    public SunsetDTO() {}

    public SunsetDTO(String date, String additionalInformation) {
        this.date = date;
        this.additionalInformation = additionalInformation;
    }
}
