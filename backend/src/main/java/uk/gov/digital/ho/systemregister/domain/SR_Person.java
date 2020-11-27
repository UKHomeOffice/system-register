package uk.gov.digital.ho.systemregister.domain;

import java.beans.ConstructorProperties;

public class SR_Person {
    public final String name;

    @ConstructorProperties("name")
    @SuppressWarnings("CdiInjectionPointsInspection")
    public SR_Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" + " name='" + name + "'" + "}";
    }

    public boolean isValid() {
        return !name.isBlank();
    }
}
