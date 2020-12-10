package uk.gov.digital.ho.systemregister.io.api.dto;

import javax.json.bind.annotation.JsonbProperty;

public class UpdateProductOwnerCommandDTO {
    @JsonbProperty("product_owner")
    public String productOwner;
}