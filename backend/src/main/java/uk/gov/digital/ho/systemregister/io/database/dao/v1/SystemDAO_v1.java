package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import java.time.Instant;
import java.util.List;

public class SystemDAO_v1 {
    public int id;
    public Instant lastUpdated;
    public String name;
    public String description;
    public String portfolio;
    public String criticality;
    public String investmentState;
    public String businessOwner;
    public String serviceOwner;
    public String technicalOwner;
    public String productOwner;
    public String informationAssetOwner;
    public String developedBy;
    public String supportedBy;
    public List<String> aliases;
    public List<RiskDAO_v1> risks;

    @SuppressWarnings("unused")
    public SystemDAO_v1() {
    }

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
