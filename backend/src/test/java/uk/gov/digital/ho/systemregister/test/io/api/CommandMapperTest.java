package uk.gov.digital.ho.systemregister.test.io.api;

import java.time.Instant;
import org.junit.Test;
import uk.gov.digital.ho.systemregister.test.TestDataUtil;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.io.api.dto.AddSystemCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.DtoMapper;

public class CommandMapperTest extends TestDataUtil {
    Instant timestamp = Instant.now();

    @Test
    public void mapAddSystemCommand() {
        AddSystemCommandDTO dto = new AddSystemCommandDTO(a_system_dto.build());
        AddSystemCommand result = DtoMapper.map(dto, expectedAuthor, timestamp);

        expectMetaDataToBeCorrect(expectedAuthor, authorUsername, result);
        expectSystemToBeCorrect(result, a_system_dto.build());
    }

    @Test
    public void mapAddSystemCommand_NeverBeenUpdated() {
        AddSystemCommandDTO dto = new AddSystemCommandDTO(a_system_dto.build());
        AddSystemCommand result = DtoMapper.map(dto, expectedAuthor, timestamp);

        expectMetaDataToBeCorrect(expectedAuthor, authorUsername, result);
        expectSystemToBeCorrect(result, a_system_dto.build());
    }
}
