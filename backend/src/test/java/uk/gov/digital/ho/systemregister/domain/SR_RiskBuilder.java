package uk.gov.digital.ho.systemregister.domain;

public final class SR_RiskBuilder {
    private final String level;
    private String name;
    private String rationale;

    @SuppressWarnings({"QsPrivateBeanMembersInspection", "CdiInjectionPointsInspection"})
    private SR_RiskBuilder(String level) {
        this.level = level;
    }

    public static SR_RiskBuilder aLowRisk() {
        return new SR_RiskBuilder("low");
    }

    public static SR_RiskBuilder aHighRisk() {
        return new SR_RiskBuilder("high");
    }

    public SR_RiskBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SR_RiskBuilder withRationale(String rationale) {
        this.rationale = rationale;
        return this;
    }

    public SR_Risk build() {
        return new SR_Risk(name, level, rationale);
    }
}
