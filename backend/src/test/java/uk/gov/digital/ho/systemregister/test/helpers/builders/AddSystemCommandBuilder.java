package uk.gov.digital.ho.systemregister.test.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddSystemCommandBuilder {

    SR_Person author = new SR_Person("Corey Logan", null, null, null);
    public Instant timeStamp = Instant.now();
    public String name = "System XYZ";
    public String description = "An example system description...";
    public String portfolio = "SPAN-R";
    public String criticality = "High";
    public String investmentState = "Sunset";
    public String businessOwner = "Busy Owner";
    public String serviceOwner = "Service Owner";
    public String technicalOwner = "Techy Owner";
    public String productOwner = "Product Owner";
    public String informationAssetOwner = "Info Owner";
    public String developedBy = "Home Office";
    public String supportedBy = "Supporter";
    public List<String> aliases = Arrays.asList("alias 1", "alias 2");
    public List<SR_Risk> risks = some_risks();

    public List<SR_Risk> some_risks() {
        List<SR_Risk> risks = new ArrayList() {
            {
                add(new SR_Risk("roadmap", "medium", "dunno"));
                add(new SR_Risk("sunset", "low", "not sure"));
            }
        };

        return risks;
    }

    public AddSystemCommand build() {
        return new AddSystemCommand(author, timeStamp, name, description, portfolio, criticality,
                investmentState, businessOwner, serviceOwner, technicalOwner, productOwner,
                informationAssetOwner, developedBy, supportedBy, aliases, risks);
    }

    public AddSystemCommandBuilder withName(String newName) {
        this.name = newName;
        return this;
    }

    public AddSystemCommandBuilder withJustName() {
        this.description = null;
        this.portfolio = null;
        this.criticality = null;
        this.investmentState = null;
        this.businessOwner = null;
        this.serviceOwner = null;
        this.technicalOwner = null;
        this.productOwner = null;
        this.informationAssetOwner = null;
        this.developedBy = null;
        this.supportedBy = null;
        this.aliases = Collections.emptyList();
        this.risks = Collections.emptyList();
        return this;
    }
}
