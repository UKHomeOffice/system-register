package uk.gov.digital.ho.systemregister.test.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.test.TestDataUtil;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.AddSystemResult;
import uk.gov.digital.ho.systemregister.domain.CHANGE;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import java.util.ArrayList;
import java.util.Arrays;

public class SystemRegisterTest {
    TestDataUtil util = new TestDataUtil();

    @Test
    public void addSystem_toEmptyRegister() {
        SystemRegister systemRegister = new SystemRegister(new ArrayList<>());
        AddSystemCommand cmd = util.n_add_system_command.build();

        AddSystemResult actual = systemRegister.addSystem(cmd.systemData);

        assertEquals(CHANGE.ADDED, actual.result);
        assertTrue(actual.system.lastUpdated.compareTo(cmd.timestamp) > 0);
        util.expectSystemToBeCorrect(cmd, actual.system);
    }

    @Test
    public void addSystem_duplicate_name_not_added() {
        String duplicateSystemName = "Duplicate Sys";
        SR_System seedSystem = util.an_sr_system.withName(duplicateSystemName).build();
        SystemRegister systemRegister = new SystemRegister(Arrays.asList(seedSystem));
        AddSystemCommand cmd_1 = util.n_add_system_command.withName(duplicateSystemName).build();

        AddSystemResult actual = systemRegister.addSystem(cmd_1.systemData);

        assertEquals(CHANGE.DUPLICATE, actual.result);
        assertEquals(seedSystem.id, actual.system.id);
    }

    @Test
    public void addSystem_not_empty_register() {
        String exisitingSystemName = "exisitng system";
        String addedSystemName = "added system";
        SR_System seedSystem = util.an_sr_system.withName(exisitingSystemName).build();
        SystemRegister systemRegister = new SystemRegister(Arrays.asList(seedSystem));
        AddSystemCommand cmd_1 = util.n_add_system_command.withName(addedSystemName).build();

        AddSystemResult actual = systemRegister.addSystem(cmd_1.systemData);

        assertEquals(CHANGE.ADDED, actual.result);
        assertNotEquals(seedSystem.id, actual.system.id);
    }

    @Test
    public void addTwoSystems() {
        String systemA = "system A";
        String systemB = "system B";
        SystemRegister systemRegister = new SystemRegister(new ArrayList<>());
        AddSystemCommand cmd_1 = util.n_add_system_command.withName(systemA).build();
        AddSystemCommand cmd_2 = util.n_add_system_command.withName(systemB).build();

        systemRegister.addSystem(cmd_1.systemData);
        systemRegister.addSystem(cmd_2.systemData);

        var actual = systemRegister.getAllSysytems();

        assertEquals(2, actual.size());
        assertEquals(actual.get(1).name, systemA);
        assertEquals(actual.get(0).name, systemB);
    }

    // todo test to check copy copy lystem list , mutsation safe
}
