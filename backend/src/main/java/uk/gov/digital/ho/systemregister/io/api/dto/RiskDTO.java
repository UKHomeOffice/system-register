package uk.gov.digital.ho.systemregister.io.api.dto;

public class RiskDTO {
    public String name;
    public String level;
    public String rationale;

    @SuppressWarnings("unused")
    public RiskDTO() {}

    public RiskDTO(String name, String level, String rationale) {
        this.name = name;
        this.level = level;
        this.rationale = rationale;
    }
}
