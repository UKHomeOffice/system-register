package uk.gov.digital.ho.systemregister.io.api.dto;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class RiskDTO {
    public final String name;
    public final String level;
    public final String rationale;

    @JsonbCreator
    public RiskDTO(String name, String level, String rationale) {
        this.name = name;
        this.level = level;
        this.rationale = rationale;
    }

}