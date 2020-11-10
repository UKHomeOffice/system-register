package uk.gov.digital.ho.systemregister.domain;

import java.util.Objects;

public class SR_Person {//todo I want to make all these immutable but cant because they need to be serialisable, help
    public String name;

    public SR_Person() {}

    public SR_Person(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SR_Person)) {
            return false;
        }
        SR_Person sR_Person = (SR_Person) o;
        return Objects.equals(name, sR_Person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "{" + " name='" + name + "'" + "}";
    }

    public boolean isValid() {
        return name.isBlank() == false;
    }
}
