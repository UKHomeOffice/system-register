package uk.gov.digital.ho.systemregister.io.api.dto;


public class AddSystemCommandDTO {
    public SystemDTO system;

    public AddSystemCommandDTO() {
    }

    public AddSystemCommandDTO(SystemDTO system) {
        this.system = system;
    }
}
