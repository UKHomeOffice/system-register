package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import java.beans.ConstructorProperties;

public class RiskDAO_v1 {
    public final String name;
    public final String level;
    public final String rationale;

    @ConstructorProperties({"name", "level", "rationale"})
    @SuppressWarnings("CdiInjectionPointsInspection")
    public RiskDAO_v1(String name, String level, String rationale) {
        this.name = name;
        this.level = level;
        this.rationale = rationale;
    }
}
