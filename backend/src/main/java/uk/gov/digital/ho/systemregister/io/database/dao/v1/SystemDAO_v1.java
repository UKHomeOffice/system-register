package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.List;

public class SystemDAO_v1 {
    public final int id;
    public final Instant lastUpdated;
    public final String name;
    public final String description;
    public final String portfolio;
    public final String criticality;
    public final String investmentState;
    public final String businessOwner;
    public final String serviceOwner;
    public final String technicalOwner;
    public final String productOwner;
    public final String informationAssetOwner;
    public final String developedBy;
    public final String supportedBy;
    public final List<String> aliases;
    public final List<RiskDAO_v1> risks;

    @ConstructorProperties({
            "id", "lastUpdated", "name", "description", "portfolio", "criticality", "investmentState", "businessOwner",
            "serviceOwner", "technicalOwner", "productOwner", "informationAssetOwner", "developedBy", "supportedBy",
            "aliases", "risks",
    })
    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemDAO_v1(int id, Instant lastUpdated, String name, String description, String portfolio,
                        String criticality, String investmentState, String businessOwner, String serviceOwner,
                        String technicalOwner, String productOwner, String informationAssetOwner, String developedBy,
                        String supportedBy, List<String> aliases, List<RiskDAO_v1> risks) {
        this.id = id;
        this.lastUpdated = lastUpdated;
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
