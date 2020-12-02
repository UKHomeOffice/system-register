package uk.gov.digital.ho.systemregister.domain;

public final class SR_PersonBuilder {
    private String username;
    private String firstName;
    private String surname;
    private String email;

    private SR_PersonBuilder() {
    }

    public static SR_PersonBuilder aPerson() {
        return new SR_PersonBuilder();
    }

    public SR_PersonBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public SR_PersonBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public SR_PersonBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public SR_PersonBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public SR_Person build() {
        return new SR_Person(username, firstName, surname, email);
    }
}
