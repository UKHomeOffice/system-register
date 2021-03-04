package uk.gov.digital.ho.systemregister.io.api.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;
import java.util.List;

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
        public String name;
        public String description;
        public String portfolio;
        public String criticality;
        @JsonbProperty("investment_state")
        public String investmentState;
        @JsonbProperty("business_owner")
        public String businessOwner;
        @JsonbProperty("service_owner")
        public String serviceOwner;
        @JsonbProperty("tech_owner")
        public String technicalOwner;
        @JsonbProperty("product_owner")
        public String productOwner;
        @JsonbProperty("information_asset_owner")
        public String informationAssetOwner;
        @JsonbProperty("developed_by")
        public String developedBy;
        @JsonbProperty("supported_by")
        public String supportedBy;
        public List<String> aliases;
        public List<RiskDTO> risks;
    }
}
