package uk.gov.digital.ho.systemregister.io.api.dto;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.domain.SR_Sunset;
import uk.gov.digital.ho.systemregister.helpers.TestDataUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.domain.SR_RiskBuilder.aLowRisk;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

public class DtoMapperTest extends TestDataUtil {

    @Test
    void mapsEmptySunset() {
        SR_Sunset sunset = new SR_Sunset();

        var result = DtoMapper.mapToDto(sunset);

        assertThat(result.date).isNull();
        assertThat(result.additionalInformation).isNull();
    }

    @Test
    void mapsSunset() {
        SR_Sunset sunset = new SR_Sunset(LocalDate.parse("2021-09-01"), "info!");

        var result = DtoMapper.mapToDto(sunset);

        assertThat(result.date).isEqualTo("2021-09-01");
        assertThat(result.additionalInformation).isEqualTo(sunset.additionalInformation);
    }

    @Test
    void mapsCurrentSystemStateToResponse() {
        var timestamp = Instant.now();
        var system = aSystem().withName("system").withRisks().build();
        var anotherSystem = aSystem()
                .withName("other")
                .withRisks(aLowRisk()
                        .withName("risk")
                        .withRationale("rationale"))
                .build();
        var anotherSystemAuthor = aPerson().build();

        var stateDto = DtoMapper.map(new CurrentState(
                Map.of(
                        system, new UpdateMetadata(null, system.lastUpdated),
                        anotherSystem, new UpdateMetadata(anotherSystemAuthor, anotherSystem.lastUpdated)),
                timestamp));

        assertThat(stateDto).usingRecursiveComparison()
                .ignoringCollectionOrderInFields("systems")
                .isEqualTo(new CurrentSystemStateDTO(
                        List.of(
                                new CurrentSystemStateDTO.System(
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
                                        List.of(), new SunsetDTO(null,null), new CurrentSystemStateDTO.UpdateMetadata(system.lastUpdated, null)),
                                new CurrentSystemStateDTO.System(
                                        anotherSystem.id,
                                        anotherSystem.name,
                                        anotherSystem.description,
                                        anotherSystem.portfolio,
                                        anotherSystem.criticality,
                                        anotherSystem.investmentState,
                                        anotherSystem.publicFacing,
                                        anotherSystem.businessOwner,
                                        anotherSystem.serviceOwner,
                                        anotherSystem.technicalOwner,
                                        anotherSystem.productOwner,
                                        anotherSystem.informationAssetOwner,
                                        anotherSystem.developedBy,
                                        anotherSystem.supportedBy,
                                        anotherSystem.aliases,
                                        List.of(new RiskDTO("risk", "low", "rationale")), new SunsetDTO(null, null), new CurrentSystemStateDTO.UpdateMetadata(anotherSystem.lastUpdated, null))),
                        timestamp));
    }
}
