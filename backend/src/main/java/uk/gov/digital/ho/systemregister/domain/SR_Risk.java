package uk.gov.digital.ho.systemregister.domain;

import java.util.Objects;

public final class SR_Risk {//todo I want to make all these immutable but cant because they need to be serialisable, help
    public String name;
    public String level;
    public String rationale;

    public SR_Risk() {}

    public SR_Risk(String name, String level, String rationale) {
        this.name = name;
        this.level = level;
        this.rationale = rationale;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SR_Risk)) {
            return false;
        }
        SR_Risk sR_Risk = (SR_Risk) o;
        return Objects.equals(name, sR_Risk.name) && Objects.equals(level, sR_Risk.level) && Objects.equals(rationale, sR_Risk.rationale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, rationale);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + name + "'" +
            ", level='" + level + "'" +
            ", rationale='" + rationale + "'" +
            "}";
    }

}
