package uk.gov.digital.ho.systemregister.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class SystemData {
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
    public List<SR_Risk> risks;


    public SystemData() {
    }

    public SystemData(String name, String description, String portfolio, String criticality,
            String investmentState, String businessOwner, String serviceOwner,
            String technicalOwner, String productOwner, String informationAssetOwner,
            String developedBy, String supportedBy, List<String> aliases, List<SR_Risk> risks) {
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SystemData)) {
            return false;
        }
        SystemData systemData = (SystemData) o;
        return Objects.equals(name, systemData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
