package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInformationAssetOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;

public class UpdateInformationAssetOwnerCommandDTO {
    @JsonbProperty("information_asset_owner")
    public String informationAssetOwner;

    public UpdateInformationAssetOwnerCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdateInformationAssetOwnerCommand(id, informationAssetOwner, author, timestamp);
    }
}
