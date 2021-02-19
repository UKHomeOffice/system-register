package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SystemData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class AddSystemCommand {
    @SystemName
    private final String name;
    private final String description;
    private final String portfolio;
    private final String criticality;
    private final String investmentState;
    private final String businessOwner;
    private final String serviceOwner;
    private final String technicalOwner;
    private final String productOwner;
    private final String informationAssetOwner;
    private final String developedBy;
    private final String supportedBy;
    private final List<String> aliases;
    private final List<SR_Risk> risks;
    private final SR_Person author;
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public AddSystemCommand(
            String name, String description, String portfolio, String criticality, String investmentState,
            String businessOwner, String serviceOwner, String technicalOwner, String productOwner,
            String informationAssetOwner, String developedBy, String supportedBy, List<String> aliases,
            List<SR_Risk> risks, SR_Person author, Instant timestamp
    ) {
        this.name = safelyTrimmed(name);
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
        this.aliases = new ArrayList<>(aliases);
        this.risks = new ArrayList<>(risks);
        this.author = author;
        this.timestamp = timestamp;
    }

    private static String safelyTrimmed(String name) {
        return name != null ? name.trim() : null;
    }

    public SR_Person getAuthor() {
        return author;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public SystemData toSystemData() {
        return new SystemData(
                name, description, portfolio, criticality, investmentState, businessOwner, serviceOwner, technicalOwner,
                productOwner, informationAssetOwner, developedBy, supportedBy, unmodifiableList(aliases),
                unmodifiableList(risks));
    }
}
