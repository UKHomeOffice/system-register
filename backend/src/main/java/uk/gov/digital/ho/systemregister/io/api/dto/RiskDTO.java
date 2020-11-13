package uk.gov.digital.ho.systemregister.io.api.dto;

import java.beans.ConstructorProperties;

public class RiskDTO {
    public final String name;
    public final String level;
    public final String rationale;

    @ConstructorProperties({"name", "level", "rationale"})
    @SuppressWarnings("CdiInjectionPointsInspection")
    public RiskDTO(String name, String level, String rationale) {
        this.name = name;
        this.level = level;
        this.rationale = rationale;
    }
}