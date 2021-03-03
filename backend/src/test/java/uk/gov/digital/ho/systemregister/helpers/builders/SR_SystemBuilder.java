package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_RiskBuilder;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SR_SystemBuilder {
    SecureRandom secureRandom = new SecureRandom();
    private int id = secureRandom.nextInt();
    private String name = "System Register";
    private String description = "Central source of system names, contacts and risk information";
    private Instant lastUpdated = Instant.now();
    private String portfolio = "SPAN-R";
    private String criticality = "low";
    private String investmentState = "evergreen";
    private String businessOwner = null;
    private String serviceOwner = "Service Owner";
    private String technicalOwner = "Techy Owner";
    private String productOwner = null;
    private String informationAssetOwner = null;
    private String developedBy = "Developers";
    private String supportedBy = "All";
    private List<String> aliases = Arrays.asList("systems register", "systems audit", "system audit");
    private List<SR_Risk> risks = some_risks();

    public static SR_SystemBuilder aSystem() {
        return new SR_SystemBuilder();
    }

    public List<SR_Risk> some_risks() {
        List<SR_Risk> risks = new ArrayList() {
            {
                add(new SR_Risk("change", "low", "Designed to be easy to change"));
            }
        };

        return risks;
    }

    public SR_System build() {
        return new SR_System(id, name, description, lastUpdated, portfolio, criticality,
                investmentState, businessOwner, serviceOwner, technicalOwner, productOwner,
                informationAssetOwner, developedBy, supportedBy, aliases, risks);
    }

    public SR_SystemBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SR_SystemBuilder withLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public SR_SystemBuilder withLastUpdated(String timestamp) {
        return withLastUpdated(Instant.parse(timestamp));
    }

    public SR_SystemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SR_SystemBuilder withAliases(String... aliases) {
        return withAliases(List.of(aliases));
    }

    public SR_SystemBuilder withAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public SR_SystemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public SR_SystemBuilder withCriticality(String criticality) {
        this.criticality = criticality;
        return this;
    }

    public SR_SystemBuilder withPortfolio(String portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public SR_SystemBuilder withInvestmentState(String investmentState) {
        this.investmentState = investmentState;
        return this;
    }

    public SR_SystemBuilder withDevelopedBy(String developedBy) {
        this.developedBy = developedBy;
        return this;
    }

    public SR_SystemBuilder withRisks(SR_RiskBuilder... risks) {
        this.risks = Arrays.stream(risks)
                .map(SR_RiskBuilder::build)
                .collect(toList());
        return this;
    }

    public SR_SystemBuilder withNoRisks() {
        this.risks = null;
        return this;
    }

    public SR_SystemBuilder withTechnicalOwner(String technicalOwner) {
        this.technicalOwner = technicalOwner;
        return this;
    }

    public SR_SystemBuilder withProductOwner(String productOwner) {
        this.productOwner = productOwner;
        return this;
    }

    public SR_SystemBuilder withBusinessOwner(String businessOwner) {
        this.businessOwner = businessOwner;
        return this;
    }

    public SR_SystemBuilder withServiceOwner(String serviceOwner) {
        this.serviceOwner = serviceOwner;
        return this;
    }

    public SR_SystemBuilder withInformationAssetOwner(String informationAssetOwner) {
        this.informationAssetOwner = informationAssetOwner;
        return this;
    }

    public SR_SystemBuilder withSupportedBy(String supportedBy) {
        this.supportedBy = supportedBy;
        return this;
    }
}
