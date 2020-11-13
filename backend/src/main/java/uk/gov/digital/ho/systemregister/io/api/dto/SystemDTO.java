package uk.gov.digital.ho.systemregister.io.api.dto;

import java.beans.ConstructorProperties;
import java.util.Collections;
import java.util.List;
import javax.json.bind.annotation.JsonbProperty;

import static java.util.Collections.unmodifiableList;

public class SystemDTO {
    public final String name;
    public final String description;
    public final String portfolio;
    public final String criticality;
    @JsonbProperty("investment_state")
    public final String investmentState;
    @JsonbProperty("business_owner")
    public final String businessOwner;
    @JsonbProperty("service_owner")
    public final String serviceOwner;
    @JsonbProperty("tech_owner")
    public final String technicalOwner;
    @JsonbProperty("product_owner")
    public final String productOwner;
    @JsonbProperty("information_asset_owner")
    public final String informationAssetOwner;
    @JsonbProperty("developed_by")
    public final String developedBy;
    @JsonbProperty("supported_by")
    public final String supportedBy;
    public final List<String> aliases;
    public final List<RiskDTO> risks;

    @ConstructorProperties({
            "name", "description", "portfolio", "criticality", "investment_state", "business_owner", "service_owner",
            "tech_owner", "product_owner", "information_asset_owner", "developed_by", "supported_by", "aliases",
            "risks",
    })
    @SuppressWarnings("CdiInjectionPointsInspection")
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
        this.aliases = unmodifiableList(aliases);
        this.risks = unmodifiableList(risks);
    }
}
