package uk.gov.digital.ho.systemregister.domain;

import java.beans.ConstructorProperties;

public class SR_Person {
    public final String username;
    public final String firstName;
    public final String surname;
    public final String email;

    @ConstructorProperties({"username", "firstName", "surname", "email"})
    @SuppressWarnings("CdiInjectionPointsInspection")
    public SR_Person(String username, String firstName, String surname, String email) {
        this.username = username;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
    }

    @Override
    public String toString() {
        return "SR_Person{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public boolean isValid() {
        return !username.isBlank();
    }

    public String toAuthorName() {
        return (this.firstName == null || this.surname == null)
                ? null
                : String.format("%s %s", this.firstName, this.surname);
    }
}
