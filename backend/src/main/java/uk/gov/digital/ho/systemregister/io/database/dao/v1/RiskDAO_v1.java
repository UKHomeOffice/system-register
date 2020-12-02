package uk.gov.digital.ho.systemregister.io.database.dao.v1;

public class RiskDAO_v1 {
    public String name;
    public String level;
    public String rationale;

    @SuppressWarnings("unused")
    public RiskDAO_v1() {
    }

    public RiskDAO_v1(String name, String level, String rationale) {
        this.name = name;
        this.level = level;
        this.rationale = rationale;
    }
}
