package uk.gov.digital.ho.systemregister.io.api.dto;

import java.time.Instant;
import java.util.List;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;
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

    @JsonbCreator
    public SystemDTO(String name, String description,
                     String portfolio, String criticality,
                     @JsonbProperty("investment_state") String investmentState,
                     @JsonbProperty("business_owner") String businessOwner,
                     @JsonbProperty("service_owner") String serviceOwner,
                     @JsonbProperty("tech_owner") String technicalOwner,
                     @JsonbProperty("product_owner") String productOwner,
                     @JsonbProperty("information_asset_owner") String informationAssetOwner,
                     @JsonbProperty("developed_by") String developedBy,
                     @JsonbProperty("supported_by") String supportedBy,
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
