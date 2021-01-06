package uk.gov.digital.ho.systemregister.io.api.dto;

import javax.json.bind.annotation.JsonbProperty;

public class UpdateSystemNameCommandDTO {
    @JsonbProperty("name")
    public String name;
}