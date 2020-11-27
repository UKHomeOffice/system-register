package uk.gov.digital.ho.systemregister.io.database.dao;

import java.beans.ConstructorProperties;

public class PersonDAO_v1 {
    public final String name;

    @ConstructorProperties({"name"})
    public PersonDAO_v1(String name) {
        this.name = name;
    }
}
