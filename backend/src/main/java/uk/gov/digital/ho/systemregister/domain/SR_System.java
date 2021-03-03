package uk.gov.digital.ho.systemregister.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableList;

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

    public SR_System withAliases(Set<String> aliases) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withCriticality(String criticality) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withInvestmentState(String investmentState) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withPortfolio(String portfolio) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withProductOwner(String productOwner) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withBusinessOwner(String businessOwner) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withInformationAssetOwner(String informationAssetOwner) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withTechnicalOwner(String technicalOwner) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withServiceOwner(String serviceOwner) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }


    public SR_System withDevelopedBy(String developedBy) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withSupportedBy(String supportedBy) {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), List.copyOf(risks));
    }

    public SR_System withRisk(SR_Risk risk) {
        var risks = this.risks.stream()
                .map(replacingMatchingRisk(risk))
                .collect(toUnmodifiableList());

        return new SR_System(id, name, description, lastUpdated, portfolio, criticality, investmentState, businessOwner,
                serviceOwner, technicalOwner, productOwner, informationAssetOwner, developedBy, supportedBy,
                List.copyOf(aliases), risks);
    }

    private Function<SR_Risk, SR_Risk> replacingMatchingRisk(SR_Risk risk) {
        return existingRisk -> existingRisk.name.equals(risk.name) ? risk : existingRisk;
    }

    public Optional<SR_Risk> getRiskByName(String name) {
        if (risks == null) {
            return Optional.empty();
        }
        return risks.stream()
                .filter(risk -> name.equals(risk.name))
                .findAny();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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
