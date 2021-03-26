package uk.gov.digital.ho.systemregister.io.api.dto;

import org.eclipse.microprofile.jwt.JsonWebToken;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_Sunset;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class DtoMapper {
    private DtoMapper() {
    }

    public static List<RiskDTO> mapToDto(List<SR_Risk> risks) {
        return risks.stream().map(r -> new RiskDTO(r.name, r.level, r.rationale))
                .collect(toList());
    }

    public static SunsetDTO mapToDto(SR_Sunset sunset) {
        return new SunsetDTO(sunset.date, sunset.additionalInformation);    }

    public static CurrentSystemStateDTO map(CurrentState currentState) {
        List<CurrentSystemStateDTO.System> systems = currentState.getUpdatesBySystem()
                .entrySet().stream()
                .map(entry -> {
                    var system = entry.getKey();
                    var metadata = entry.getValue();
                    return new CurrentSystemStateDTO.System(
                            system.id,
                            system.name,
                            system.description,
                            system.portfolio,
                            system.criticality,
                            system.investmentState,
                            system.publicFacing,
                            system.businessOwner,
                            system.serviceOwner,
                            system.technicalOwner,
                            system.productOwner,
                            system.informationAssetOwner,
                            system.developedBy,
                            system.supportedBy,
                            system.aliases,
                            mapToDto(system.risks),
                            mapToDto(system.sunset), toUpdateMetadata(metadata));
                })
                .collect(toList());
        return new CurrentSystemStateDTO(systems, currentState.getLastUpdatedAt());
    }

    private static CurrentSystemStateDTO.UpdateMetadata toUpdateMetadata(uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata metadata) {
        String authorName = metadata.updatedBy == null ? null : metadata.updatedBy.toAuthorName();
        return new CurrentSystemStateDTO.UpdateMetadata(
                metadata.updatedAt,
                authorName);
    }

    public static SR_Person extractAuthor(JsonWebToken token) {
        return new SR_Person(
                token.getName(),
                token.getClaim("given_name"),
                token.getClaim("family_name"),
                token.getClaim("email"));
    }
}
