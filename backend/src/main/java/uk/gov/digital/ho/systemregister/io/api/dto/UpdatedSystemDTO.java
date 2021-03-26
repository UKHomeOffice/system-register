package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@JsonbNillable(false)
public class UpdatedSystemDTO {
    public final int id;
    public final String name;
    public final String description;
    public final String portfolio;
    public final String criticality;
    @JsonbProperty("investment_state")
    public final String investmentState;
    @JsonbProperty("public_facing")
    public final String publicFacing;
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
    public final SunsetDTO sunset;
    @JsonbProperty("last_updated")
    public final Metadata lastUpdated;


    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdatedSystemDTO(
            int id, String name, String description,
            String portfolio, String criticality,
            String investmentState,
            String publicFacing,
            String businessOwner,
            String serviceOwner,
            String technicalOwner,
            String productOwner,
            String informationAssetOwner,
            String developedBy,
            String supportedBy,
            List<String> aliases,
            List<RiskDTO> risks,
            SunsetDTO sunset,
            Metadata lastUpdated) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.portfolio = portfolio;
        this.criticality = criticality;
        this.investmentState = investmentState;
        this.publicFacing = publicFacing;
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

    public static UpdatedSystemDTO from(SR_System system, UpdateMetadata metadata) {
        return new UpdatedSystemDTO(system.id, system.name, system.description, system.portfolio, system.criticality,
                system.investmentState, system.publicFacing, system.businessOwner, system.serviceOwner, system.technicalOwner,
                system.productOwner, system.informationAssetOwner, system.developedBy, system.supportedBy,
                List.copyOf(system.aliases), DtoMapper.mapToDto(system.risks), DtoMapper.mapToDto(system.sunset), new Metadata(metadata.updatedAt, metadata.updatedBy.toAuthorName()));
    }

    public static class Metadata {
        public final Instant timestamp;
        @JsonbProperty("author_name")
        public final String authorName;

        @SuppressWarnings("CdiInjectionPointsInspection")
        public Metadata(Instant timestamp, String authorName) {
            this.timestamp = timestamp;
            this.authorName = authorName;
        }
    }
}
