package uk.gov.digital.ho.systemregister.helpers;

import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;
import uk.gov.digital.ho.systemregister.helpers.builders.SystemDTOBuilder;
import uk.gov.digital.ho.systemregister.io.api.dto.SystemDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDataUtil {
    protected String expectedAuthor = "Author";
    protected String authorUsername = "p/tf1";

    public SystemDTOBuilder a_system_dto = new SystemDTOBuilder();
    public AddSystemCommandBuilder n_add_system_command = new AddSystemCommandBuilder();
    public SR_SystemBuilder an_sr_system = new SR_SystemBuilder();

    public void expectSystemToBeCorrect(AddSystemCommand cmd, SystemDTO expectedSystem) {
        assertEquals(expectedSystem.aliases, cmd.toSystemData().aliases);
        assertEquals(expectedSystem.businessOwner, cmd.toSystemData().businessOwner);
        assertEquals(expectedSystem.criticality, cmd.toSystemData().criticality);
        assertEquals(expectedSystem.description, cmd.toSystemData().description);
        assertEquals(expectedSystem.developedBy, cmd.toSystemData().developedBy);
        assertEquals(expectedSystem.informationAssetOwner, cmd.toSystemData().informationAssetOwner);
        assertEquals(expectedSystem.investmentState, cmd.toSystemData().investmentState);
        assertEquals(expectedSystem.name, cmd.toSystemData().name);
        assertEquals(expectedSystem.portfolio, cmd.toSystemData().portfolio);
        assertEquals(expectedSystem.productOwner, cmd.toSystemData().productOwner);
    }

    public void expectSystemToBeCorrect(AddSystemCommand cmd, SR_System expectedSystem) {
        assertEquals(expectedSystem.aliases, cmd.toSystemData().aliases);
        assertEquals(expectedSystem.businessOwner, cmd.toSystemData().businessOwner);
        assertEquals(expectedSystem.criticality, cmd.toSystemData().criticality);
        assertEquals(expectedSystem.description, cmd.toSystemData().description);
        assertEquals(expectedSystem.developedBy, cmd.toSystemData().developedBy);
        assertEquals(expectedSystem.informationAssetOwner, cmd.toSystemData().informationAssetOwner);
        assertEquals(expectedSystem.investmentState, cmd.toSystemData().investmentState);
        assertEquals(expectedSystem.name, cmd.toSystemData().name);
        assertEquals(expectedSystem.portfolio, cmd.toSystemData().portfolio);
        assertEquals(expectedSystem.productOwner, cmd.toSystemData().productOwner);
    }

    protected void expectMetaDataToBeCorrect(String expectedAuthor, String authorUsername,
                                             AddSystemCommand result) {
        assertEquals(expectedAuthor, result.getAuthor().username);
    }
}
