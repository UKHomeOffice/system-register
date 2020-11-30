package uk.gov.digital.ho.systemregister.io.database.dao.v1;

public class RiskDAO_v1 {
    public final String name;
    public final String level;
    public final String rationale;

    public RiskDAO_v1(String name, String level, String rationale) {
        this.name = name;
        this.level = level;
        this.rationale = rationale;
    }
}
