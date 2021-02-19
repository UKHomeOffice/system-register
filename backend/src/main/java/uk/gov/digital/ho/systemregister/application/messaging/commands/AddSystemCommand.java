package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemDescription;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SystemData;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
import static uk.gov.digital.ho.systemregister.util.NullSafeUtils.*;

public class AddSystemCommand {
    @SystemName
    private final String name;
    @SystemDescription
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
    private final List<Risk> risks;
    private final SR_Person author;
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public AddSystemCommand(
            String name, String description, String portfolio, String criticality, String investmentState,
            String businessOwner, String serviceOwner, String technicalOwner, String productOwner,
            String informationAssetOwner, String developedBy, String supportedBy, List<String> aliases,
            List<Risk> risks, SR_Person author, Instant timestamp
    ) {
        this.name = safelyTrimmed(name);
        this.description = safelyTrimmed(description);
        this.portfolio = safelyTrimmed(portfolio);
        this.criticality = safelyTrimmed(criticality);
        this.investmentState = safelyTrimmed(investmentState);
        this.businessOwner = safelyTrimmed(businessOwner);
        this.serviceOwner = safelyTrimmed(serviceOwner);
        this.technicalOwner = safelyTrimmed(technicalOwner);
        this.productOwner = safelyTrimmed(productOwner);
        this.informationAssetOwner = safelyTrimmed(informationAssetOwner);
        this.developedBy = safelyTrimmed(developedBy);
        this.supportedBy = safelyTrimmed(supportedBy);
        this.aliases = allSafelyTrimmed(aliases);
        this.risks = safelyCopied(risks);
        this.author = author;
        this.timestamp = timestamp;
    }

    public SR_Person getAuthor() {
        return author;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public SystemData toSystemData() {
        var risks = this.risks.stream()
                .map(Risk::toSrRisk)
                .collect(toUnmodifiableList());
        return new SystemData(
                name, description, portfolio, criticality, investmentState, businessOwner, serviceOwner, technicalOwner,
                productOwner, informationAssetOwner, developedBy, supportedBy, aliases, risks);
    }

    @SuppressWarnings("CdiInjectionPointsInspection")
    public static class Risk {
        private final String name;
        private final String level;
        private final String rationale;

        public Risk(String name, String level, String rationale) {
            this.name = safelyTrimmed(name);
            this.level = safelyTrimmed(level);
            this.rationale = safelyTrimmed(rationale);
        }

        SR_Risk toSrRisk() {
            return new SR_Risk(name, level, rationale);
        }
    }
}
