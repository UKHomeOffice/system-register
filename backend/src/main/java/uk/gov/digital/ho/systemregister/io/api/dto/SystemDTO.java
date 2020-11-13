package uk.gov.digital.ho.systemregister.io.api.dto;

import java.util.List;
import javax.json.bind.annotation.JsonbProperty;

public class SystemDTO {
    public String name;
    public String description;
    public String portfolio;
    public String criticality;
    @JsonbProperty("investment_state")
    public String investmentState;
    @JsonbProperty("business_owner")
    public String businessOwner;
    @JsonbProperty("service_owner")
    public String serviceOwner;
    @JsonbProperty("tech_owner")
    public String technicalOwner;
    @JsonbProperty("product_owner")
    public String productOwner;
    @JsonbProperty("information_asset_owner")
    public String informationAssetOwner;
    @JsonbProperty("developed_by")
    public String developedBy;
    @JsonbProperty("supported_by")
    public String supportedBy;
    public List<String> aliases;
    public List<RiskDTO> risks;

    public SystemDTO() {}

    public SystemDTO(String name, String description,
                     String portfolio, String criticality,
                     String investmentState,
                     String businessOwner,
                     String serviceOwner,
                     String technicalOwner,
                     String productOwner,
                     String informationAssetOwner,
                     String developedBy,
                     String supportedBy,
                     List<String> aliases,
                     List<RiskDTO> risks) {
        this.name = name;
        this.description = description;
        this.portfolio = portfolio;
        this.criticality = criticality;
        this.investmentState = investmentState;
        this.businessOwner = businessOwner;
        this.serviceOwner = serviceOwner;
        this.technicalOwner = technicalOwner;
        this.productOwner = productOwner;
        this.informationAssetOwner = informationAssetOwner;
        this.developedBy = developedBy;
        this.supportedBy = supportedBy;
        this.aliases = aliases;
        this.risks = risks;
    }
}
