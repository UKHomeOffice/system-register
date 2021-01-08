package uk.gov.digital.ho.systemregister.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class SR_System extends SystemData {
     // todo I want to make all these immutable but cant
    //  because they need to be serialisable; help!
    public int id;
    public Instant lastUpdated;

    public SR_System() {
    }

    public SR_System(int id, String name, String description, Instant lastUpdated, String portfolio,
                     String criticality, String investmentState, String businessOwner, String serviceOwner,
                     String technicalOwner, String productOwner, String informationAssetOwner,
                     String developedBy, String supportedBy, List<String> aliases, List<SR_Risk> risks) {
        super(name, description, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy,
                supportedBy, aliases, risks);
        this.id = id;
        this.lastUpdated = lastUpdated;
    }

    public SR_System withName(String systemName) {
        return new SR_System(id, systemName, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withDescription(String description) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withProductOwner(String productOwner) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withCriticality(String criticality) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SR_System)) {
            return false;
        }
        SR_System sR_System = (SR_System) o;
        return Objects.equals(name, sR_System.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "{" + " id='" + id + "'" + ", name='" + name + "'" + ", description='" + description
                + "'" + ", lastUpdated='" + lastUpdated.toString() + "'" + ", portfolio='"
                + portfolio + "'" + ", criticality='" + criticality + "'" + ", investmentState='"
                + investmentState + "'" + ", businessOwner='" + businessOwner + "'"
                + ", serviceOwner='" + serviceOwner + "'" + ", technicalOwner='" + technicalOwner
                + "'" + ", productOwner='" + productOwner + "'" + ", informationAssetOwner='"
                + informationAssetOwner + "'" + ", developedBy='" + developedBy + "'"
                + ", supportedBy='" + supportedBy + "'" + ", aliases='" + aliases + "'"
                + ", risks='" + risks + "'" + "}";
    }
}
