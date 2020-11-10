package uk.gov.digital.ho.systemregister.io.api.dto;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;
import java.util.List;

public class RegisteredSystemDTO extends SystemDTO {
    public Integer id;
    @JsonbProperty("last_updated")
    public Instant lastUpdated;

    public RegisteredSystemDTO(Integer id, String name, String description, String portfolio, String criticality,
                               String investmentState, String businessOwner, String serviceOwner, String technicalOwner,
                               String productOwner, String informationAssetOwner, String developedBy, String supportedBy,
                               @JsonbProperty("last_updated") Instant lastUpdated, List<String> aliases, List<RiskDTO> risks) {
        super(name, description, portfolio, criticality, investmentState, businessOwner, serviceOwner,
                technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                aliases, risks);
        this.id = id;
        this.lastUpdated = lastUpdated;
    }
}
