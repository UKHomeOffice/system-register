package uk.gov.digital.ho.systemregister.io.api.dto;

/*
{
    "systems": [
        {
            "aliases": [
                "new sys 1"
            ],
            "criticality": "low",
            "description": "A test system for testing purposes",
            "developed_by": "Home Office",
            "investment_state": "evergreen",
            "name": "Riskinator 5000",
            "portfolio": "SPAN-R",
            "risks": [
                {
                    "level": "low",
                    "name": "roadmap",
                    "rationale": "Meaningful data here"
                },
                {
                    "level": "high",
                    "name": "explosion",
                    "rationale": "RUN!!!"
                }
            ],
            "id": 1,
            "last_updated": {
                "timestamp": "2020-11-18T11:22:50.326570Z",
                "author_name": "Betty Franklin"
            }
        }
    ],
    "timestamp": "2020-11-18T11:22:50.327476Z"
}
 */

import java.time.Instant;
import java.util.List;
import javax.json.bind.annotation.JsonbProperty;

import static java.util.Collections.unmodifiableList;

@SuppressWarnings("CdiInjectionPointsInspection")
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
                      UpdateMetadata lastUpdated) {
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
