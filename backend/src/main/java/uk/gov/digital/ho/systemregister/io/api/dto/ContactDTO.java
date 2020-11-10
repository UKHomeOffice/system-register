package uk.gov.digital.ho.systemregister.io.api.dto;

public class ContactDTO {
    public String name;
    public ContactType type;

    public ContactDTO() {
    }

    public ContactDTO(String name, ContactType type) {
        this.type = type;
        this.name = name;
    }

    public enum ContactType {
        REGISTER_OWNER, SERVICE_OWNER, BUSINESS_OWNER, TECHNICAL_OWNER,
    }
}
