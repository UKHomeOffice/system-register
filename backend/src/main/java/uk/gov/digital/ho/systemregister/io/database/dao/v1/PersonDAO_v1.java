package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import java.beans.ConstructorProperties;

public class PersonDAO_v1 {
    public final String name;

    @ConstructorProperties({"name"})
    public PersonDAO_v1(String name) {
        this.name = name;
    }
}
