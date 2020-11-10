package uk.gov.digital.ho.systemregister.io.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_System;

public final class DtoMapper {
    private DtoMapper() {
    }

    public static AddSystemCommand map(AddSystemCommandDTO cmd, String authorName,
                                       Instant timestamp) {
        return new AddSystemCommand(new SR_Person(authorName), timestamp, cmd.system.name,
                cmd.system.description, cmd.system.portfolio,
                cmd.system.criticality, cmd.system.investmentState, cmd.system.businessOwner, cmd.system.serviceOwner,
                cmd.system.technicalOwner, cmd.system.productOwner, cmd.system.informationAssetOwner,
                cmd.system.developedBy, cmd.system.supportedBy, cmd.system.aliases, DtoMapper.mapToDomain(cmd.system.risks));
    }

    public static SnapshotDTO map(Snapshot snapshot) {
        return new SnapshotDTO(snapshot.systems.stream().map(s -> map(s)).collect(Collectors.toList()), snapshot.timestamp);
    }

    public static RegisteredSystemDTO map(SR_System s) {
        return new RegisteredSystemDTO(s.id, s.name, s.description, s.portfolio, s.criticality, s.investmentState, s.businessOwner
                , s.serviceOwner, s.technicalOwner, s.productOwner, s.informationAssetOwner, s.developedBy,
                s.supportedBy, s.lastUpdated, s.aliases, mapToDto(s.risks));
    }

    public static List<SR_Risk> mapToDomain(List<RiskDTO> risks) {
        return risks.stream().map(r -> new SR_Risk(r.name, r.level, r.rationale))
                .collect(Collectors.toList());
    }

    public static List<RiskDTO> mapToDto(List<SR_Risk> risks) {
        return risks.stream().map(r -> new RiskDTO(r.name, r.level, r.rationale))
                .collect(Collectors.toList());
    }

}
