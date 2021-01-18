package uk.gov.digital.ho.systemregister.helpers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;
import uk.gov.digital.ho.systemregister.helpers.builders.SystemDTOBuilder;
import uk.gov.digital.ho.systemregister.io.api.dto.RegisteredSystemDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.RiskDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.SnapshotDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.SystemDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDataUtil {
    protected String expectedAuthor = "Author";
    protected String authorUsername = "p/tf1";

    public SystemDTOBuilder a_system_dto = new SystemDTOBuilder();
    public AddSystemCommandBuilder n_add_system_command = new AddSystemCommandBuilder();
    public SR_SystemBuilder an_sr_system = new SR_SystemBuilder();

    public void
    expectSystemToBeCorrect(AddSystemCommand cmd, SystemDTO expectedSystem) {
        assertEquals(expectedSystem.aliases, cmd.systemData.aliases);
        assertEquals(expectedSystem.businessOwner, cmd.systemData.businessOwner);
        assertEquals(expectedSystem.criticality, cmd.systemData.criticality);
        assertEquals(expectedSystem.description, cmd.systemData.description);
        assertEquals(expectedSystem.developedBy, cmd.systemData.developedBy);
        assertEquals(expectedSystem.informationAssetOwner, cmd.systemData.informationAssetOwner);
        assertEquals(expectedSystem.investmentState, cmd.systemData.investmentState);
        assertEquals(expectedSystem.name, cmd.systemData.name);
        assertEquals(expectedSystem.portfolio, cmd.systemData.portfolio);
        assertEquals(expectedSystem.productOwner, cmd.systemData.productOwner);
    }

    public void expectSystemToBeCorrect(AddSystemCommand cmd, SR_System expectedSystem) {
        assertEquals(expectedSystem.aliases, cmd.systemData.aliases);
        assertEquals(expectedSystem.businessOwner, cmd.systemData.businessOwner);
        assertEquals(expectedSystem.criticality, cmd.systemData.criticality);
        assertEquals(expectedSystem.description, cmd.systemData.description);
        assertEquals(expectedSystem.developedBy, cmd.systemData.developedBy);
        assertEquals(expectedSystem.informationAssetOwner, cmd.systemData.informationAssetOwner);
        assertEquals(expectedSystem.investmentState, cmd.systemData.investmentState);
        assertEquals(expectedSystem.name, cmd.systemData.name);
        assertEquals(expectedSystem.portfolio, cmd.systemData.portfolio);
        assertEquals(expectedSystem.productOwner, cmd.systemData.productOwner);
    }

    public void expectSnapshotsToMatch(Snapshot domainSnapshot, SnapshotDTO snapshotDTO) {
        assertEquals(domainSnapshot.timestamp, snapshotDTO.timestamp);
        assertEquals(domainSnapshot.systems.size(), snapshotDTO.systems.size());
        domainSnapshot.systems
                .forEach(s -> assertTrue(snapshotDTO.systems.stream().anyMatch(dto -> areEqual(s, dto))));
    }

    private boolean areEqual(SR_System domainSys, RegisteredSystemDTO dto) {
        if (domainSys.id == dto.id &&
                domainSys.name == dto.name &&
                domainSys.businessOwner == dto.businessOwner &&
                domainSys.productOwner == dto.productOwner &&
                domainSys.informationAssetOwner == dto.informationAssetOwner &&
                domainSys.technicalOwner == dto.technicalOwner &&
                domainSys.criticality == dto.criticality &&
                domainSys.developedBy == dto.developedBy &&
                domainSys.description == dto.description &&
                domainSys.supportedBy == dto.supportedBy &&
                domainSys.investmentState == dto.investmentState &&
                domainSys.aliases == dto.aliases
        ) {
            domainSys.risks
                    .forEach(sRisk -> assertTrue(dto.risks.stream()
                            .anyMatch(dtoRisk -> areEqual(sRisk, dtoRisk))));
            return true;
        }
        return false;
    }

    private boolean areEqual(SR_Risk domainRisk, RiskDTO dtoRisk) {
        if (domainRisk.name == dtoRisk.name &&
                domainRisk.level == dtoRisk.level &&
                domainRisk.rationale == dtoRisk.rationale
        ) {
            return true;
        }
        return false;
    }

    protected void expectMetaDataToBeCorrect(String expectedAuthor, String authorUsername,
                                             AddSystemCommand result) {
        assertEquals(expectedAuthor, result.author.username);
    }
}
