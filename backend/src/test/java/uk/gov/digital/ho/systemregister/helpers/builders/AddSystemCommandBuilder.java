package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;

import java.time.Instant;
import java.util.List;

import static java.util.Arrays.asList;

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
    private List<SR_Risk> risks = some_risks();

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

    public List<SR_Risk> some_risks() {
        return List.of(
                new SR_Risk("roadmap", "medium", "dunno"),
                new SR_Risk("sunset", "low", "not sure"));
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

    public AddSystemCommandBuilder withRisks(SR_Risk... risks) {
        return withRisks(asList(risks));
    }

    public AddSystemCommandBuilder withRisks(List<SR_Risk> risks) {
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
        return new AddSystemCommand(
                name, description, portfolio, criticality, investmentState, businessOwner, serviceOwner, technicalOwner,
                productOwner, informationAssetOwner, developedBy, supportedBy, aliases, risks, author, timestamp);
    }
}
