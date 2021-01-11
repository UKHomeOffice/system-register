package uk.gov.digital.ho.systemregister.io.api.dto;

import org.eclipse.microprofile.jwt.JsonWebToken;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.messaging.commands.*;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class DtoMapper {
    private DtoMapper() {
    }

    public static UpdateSystemNameCommand map(UpdateSystemNameCommandDTO cmd, int id, SR_Person author, Instant timestamp) {
        return new UpdateSystemNameCommand(id, cmd.name, author, timestamp);
    }

    public static UpdateSystemDescriptionCommand map(UpdateSystemDescriptionCommandDTO cmd, int id, SR_Person author, Instant timestamp) {
        return new UpdateSystemDescriptionCommand(id, cmd.description, author, timestamp);
    }

    public static UpdateCriticalityCommand map(UpdateCriticalityCommandDTO cmd, int id, SR_Person author, Instant timestamp) {
        return new UpdateCriticalityCommand(author, timestamp, id, cmd.criticality);
    }

    public static UpdateInvestmentStateCommand map(UpdateInvestmentStateCommandDTO cmd, int id, SR_Person author, Instant timestamp) {
        return new UpdateInvestmentStateCommand(author, timestamp, id, cmd.investmentState);
    }

    public static UpdateProductOwnerCommand map(UpdateProductOwnerCommandDTO cmd, int id, SR_Person author, Instant timestamp) {
        return new UpdateProductOwnerCommand(id, cmd.productOwner, author, timestamp);
    }

    public static AddSystemCommand map(AddSystemCommandDTO cmd, SR_Person author, Instant timestamp) {
        return new AddSystemCommand(author, timestamp, cmd.system.name,
                cmd.system.description, cmd.system.portfolio,
                cmd.system.criticality, cmd.system.investmentState, cmd.system.businessOwner, cmd.system.serviceOwner,
                cmd.system.technicalOwner, cmd.system.productOwner, cmd.system.informationAssetOwner,
                cmd.system.developedBy, cmd.system.supportedBy, cmd.system.aliases, DtoMapper.mapToDomain(cmd.system.risks));
    }

    public static SnapshotDTO map(Snapshot snapshot) {
        return new SnapshotDTO(snapshot.systems.stream().map(s -> map(s)).collect(toList()), snapshot.timestamp);
    }

    public static RegisteredSystemDTO map(SR_System s) {
        return new RegisteredSystemDTO(s.id, s.name, s.description, s.portfolio, s.criticality, s.investmentState, s.businessOwner
                , s.serviceOwner, s.technicalOwner, s.productOwner, s.informationAssetOwner, s.developedBy,
                s.supportedBy, s.lastUpdated, s.aliases, mapToDto(s.risks));
    }

    public static List<SR_Risk> mapToDomain(List<RiskDTO> risks) {
        return risks.stream().map(r -> new SR_Risk(r.name, r.level, r.rationale))
                .collect(toList());
    }

    public static List<RiskDTO> mapToDto(List<SR_Risk> risks) {
        return risks.stream().map(r -> new RiskDTO(r.name, r.level, r.rationale))
                .collect(toList());
    }

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
                            system.businessOwner,
                            system.serviceOwner,
                            system.technicalOwner,
                            system.productOwner,
                            system.informationAssetOwner,
                            system.developedBy,
                            system.supportedBy,
                            system.aliases,
                            mapToDto(system.risks),
                            toUpdateMetadata(metadata));
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
