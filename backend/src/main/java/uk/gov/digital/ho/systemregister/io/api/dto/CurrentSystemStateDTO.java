package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.domain.SR_Sunset;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@SuppressWarnings({"CdiInjectionPointsInspection", "DuplicatedCode"})
public class CurrentSystemStateDTO {
    public final List<System> systems;
    public final Instant timestamp;

    public CurrentSystemStateDTO(List<System> systems, Instant timestamp) {
        this.systems = systems;
        this.timestamp = timestamp;
    }

    public static class System {
        public final int id;
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
        @JsonbProperty("sunset")
        public final SunsetDTO sunset;
        @JsonbProperty("last_updated")
        public final UpdateMetadata lastUpdated;

        public System(int id, String name, String description,
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
                      List<RiskDTO> risks,
                      SunsetDTO sunset, UpdateMetadata lastUpdated) {
            this.id = id;
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
            this.sunset = sunset;
            this.lastUpdated = lastUpdated;
        }
    }

    public static class UpdateMetadata {
        public final Instant timestamp;
        @JsonbProperty("author_name")
        public final String authorName;

        public UpdateMetadata(Instant timestamp, String authorName) {
            this.timestamp = timestamp;
            this.authorName = authorName;
        }
    }
}
