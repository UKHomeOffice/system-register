package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;
import javax.json.bind.annotation.JsonbProperty;

public class UpdateProductOwnerCommandDTO {
    @JsonbProperty("product_owner")
    public String productOwner;

    public UpdateProductOwnerCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateProductOwnerCommand(id, productOwner, author, timestamp);
    }
}
