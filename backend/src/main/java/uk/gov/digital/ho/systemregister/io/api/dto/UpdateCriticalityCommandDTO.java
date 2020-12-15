package uk.gov.digital.ho.systemregister.io.api.dto;

import javax.json.bind.annotation.JsonbProperty;

public class UpdateCriticalityCommandDTO {
    @JsonbProperty("criticality")
    public String criticality;
}