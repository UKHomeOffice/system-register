package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toUnmodifiableList;

public class AddSystemCommandBuilder {
    private SR_Person author = new SR_Person("Corey Logan", null, null, null);
    private Instant timestamp = Instant.now();
    private String name = "System XYZ";
    private String description = "An example system description...";
    private String portfolio = "SPAN-R";
    private String criticality = "High";
    private String investmentState = "Sunset";
    private String businessOwner = "Busy Owner";
    private String serviceOwner = "Service Owner";
    private String technicalOwner = "Techy Owner";
    private String productOwner = "Product Owner";
    private String informationAssetOwner = "Info Owner";
    private String developedBy = "Home Office";
    private String supportedBy = "Supporter";
    private List<String> aliases = asList("alias 1", "alias 2");
    private List<RiskBuilder> risks = List.of();

    public static AddSystemCommandBuilder anAddSystemCommand() {
        return new AddSystemCommandBuilder();
    }

    public static AddSystemCommandBuilder aMinimalAddSystemCommand() {
        return anAddSystemCommand()
                .withDescription(null)
                .withPortfolio(null)
                .withCriticality(null)
                .withInvestmentState(null)
                .withBusinessOwner(null)
                .withServiceOwner(null)
                .withTechnicalOwner(null)
                .withProductOwner(null)
                .withInformationAssetOwner(null)
                .withDevelopedBy(null)
                .withSupportedBy(null)
                .withAliases()
                .withRisks();
    }

    public AddSystemCommandBuilder withName(String newName) {
        this.name = newName;
        return this;
    }

    public AddSystemCommandBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AddSystemCommandBuilder withPortfolio(String portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public AddSystemCommandBuilder withCriticality(String criticality) {
        this.criticality = criticality;
        return this;
    }

    public AddSystemCommandBuilder withInvestmentState(String investmentState) {
        this.investmentState = investmentState;
        return this;
    }

    public AddSystemCommandBuilder withBusinessOwner(String businessOwner) {
        this.businessOwner = businessOwner;
        return this;
    }

    public AddSystemCommandBuilder withServiceOwner(String serviceOwner) {
        this.serviceOwner = serviceOwner;
        return this;
    }

    public AddSystemCommandBuilder withTechnicalOwner(String technicalOwner) {
        this.technicalOwner = technicalOwner;
        return this;
    }

    public AddSystemCommandBuilder withProductOwner(String productOwner) {
        this.productOwner = productOwner;
        return this;
    }

    public AddSystemCommandBuilder withInformationAssetOwner(String informationAssetOwner) {
        this.informationAssetOwner = informationAssetOwner;
        return this;
    }

    public AddSystemCommandBuilder withDevelopedBy(String developedBy) {
        this.developedBy = developedBy;
        return this;
    }

    public AddSystemCommandBuilder withSupportedBy(String supportedBy) {
        this.supportedBy = supportedBy;
        return this;
    }

    public AddSystemCommandBuilder withAliases(String... aliases) {
        return withAliases(asList(aliases));
    }

    public AddSystemCommandBuilder withAliases(List<String> aliases) {
        this.aliases = List.copyOf(aliases);
        return this;
    }

    public AddSystemCommandBuilder withRisks(RiskBuilder... risks) {
        return withRisks(asList(risks));
    }

    public AddSystemCommandBuilder withRisks(List<RiskBuilder> risks) {
        this.risks = List.copyOf(risks);
        return this;
    }

    public AddSystemCommandBuilder withAuthor(SR_Person author) {
        this.author = author;
        return this;
    }

    public AddSystemCommandBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public AddSystemCommand build() {
        var risks = this.risks.stream()
                .map(RiskBuilder::build)
                .collect(toUnmodifiableList());
        return new AddSystemCommand(
                name, description, portfolio, criticality, investmentState, businessOwner, serviceOwner, technicalOwner,
                productOwner, informationAssetOwner, developedBy, supportedBy, aliases, risks, author, timestamp);
    }

    public static class RiskBuilder {
        private final String level;
        private String name = "a risk";
        private String rationale = "for some reason";

        @SuppressWarnings({"QsPrivateBeanMembersInspection", "CdiInjectionPointsInspection"})
        private RiskBuilder(String level) {
            this.level = level;
        }

        public static RiskBuilder aHighRisk() {
            return new RiskBuilder("high");
        }

        public RiskBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RiskBuilder withRationale(String rationale) {
            this.rationale = rationale;
            return this;
        }

        public AddSystemCommand.Risk build() {
            return new AddSystemCommand.Risk(name, level, rationale);
        }
    }
}
