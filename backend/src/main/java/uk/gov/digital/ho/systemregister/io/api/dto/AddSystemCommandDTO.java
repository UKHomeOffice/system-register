package uk.gov.digital.ho.systemregister.io.api.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static uk.gov.digital.ho.systemregister.util.NullSafeUtils.safelyCopied;
import static uk.gov.digital.ho.systemregister.util.NullSafeUtils.safelyMapped;

@SuppressFBWarnings(
        value = {"UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"},
        justification = "Fields are deserialized by Jsonb"
)
public class AddSystemCommandDTO {
    public SystemDTO system;

    public AddSystemCommand toCommand(SR_Person author, Instant timestamp) {
        var risks = safelyMapped(
                system.risks,
                risk -> new AddSystemCommand.Risk(risk.name, risk.level, risk.rationale));
        return new AddSystemCommand(
                system.name, system.description, system.portfolio, system.criticality, system.investmentState,
                system.businessOwner, system.serviceOwner, system.technicalOwner, system.productOwner,
                system.informationAssetOwner, system.developedBy, system.supportedBy, safelyCopied(system.aliases),
                risks, author, timestamp);
    }

    public static class SystemDTO {
        public final String name;
        public final String description;
        public final String portfolio;
        public final String criticality;
        @JsonbProperty("investment_state")
        public final String investmentState;
        @JsonbProperty("business_owner")
        public final String businessOwner;
        @JsonbProperty("service_owner")
        public final String serviceOwner;
        @JsonbProperty("tech_owner")
        public final String technicalOwner;
        @JsonbProperty("product_owner")
        public final String productOwner;
        @JsonbProperty("information_asset_owner")
        public final String informationAssetOwner;
        @JsonbProperty("developed_by")
        public final String developedBy;
        @JsonbProperty("supported_by")
        public final String supportedBy;
        public final List<String> aliases;
        public final List<RiskDTO> risks;

        @ConstructorProperties({
                "name", "description", "portfolio", "criticality", "investment_state", "business_owner", "service_owner",
                "tech_owner", "product_owner", "information_asset_owner", "developed_by", "supported_by", "aliases",
                "risks",
        })
        @SuppressWarnings({"CdiInjectionPointsInspection", "DuplicatedCode"})
        public SystemDTO(String name, String description,
                         String portfolio, String criticality,
                         String investmentState,
                         String businessOwner,
                         String serviceOwner,
                         String technicalOwner,
                         String productOwner,
                         String informationAssetOwner,
                         String developedBy,
                         String supportedBy,
                         List<String> aliases,
                         List<RiskDTO> risks) {
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
            this.aliases = unmodifiableList(aliases);
            this.risks = unmodifiableList(risks);
        }
    }
}
