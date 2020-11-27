package uk.gov.digital.ho.systemregister.test.io.api.dto;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.test.TestDataUtil;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.io.api.dto.AddSystemCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.DtoMapper;
import uk.gov.digital.ho.systemregister.io.api.dto.SnapshotDTO;
import uk.gov.digital.ho.systemregister.test.io.database.SnapshotBuilder;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class DtoMapperTest extends TestDataUtil {
    Instant timestamp = Instant.now();

    @Test
    public void mapAddSystemCommand() {
        AddSystemCommandDTO dto = new AddSystemCommandDTO(a_system_dto.build());
        AddSystemCommand result = DtoMapper.map(dto, expectedAuthor, timestamp);

        expectMetaDataToBeCorrect(expectedAuthor, authorUsername, result);
        expectSystemToBeCorrect(result, a_system_dto.build());
    }

    @Test
    public void mapSnapshot() {
        Snapshot snapshot = new SnapshotBuilder().build();

        SnapshotDTO result = DtoMapper.map(snapshot);

        assertEquals(snapshot.timestamp, result.timestamp);
        expectSnapshotsToMatch(snapshot, result);
    }
}
