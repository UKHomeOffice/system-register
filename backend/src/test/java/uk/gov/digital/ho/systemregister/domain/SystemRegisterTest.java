package uk.gov.digital.ho.systemregister.domain;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.helpers.TestDataUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

public class SystemRegisterTest {
    TestDataUtil util = new TestDataUtil();

    @Test
    public void addSystem_toEmptyRegister() {
        SystemRegister systemRegister = new SystemRegister(new ArrayList<>());
        AddSystemCommand cmd = util.n_add_system_command.build();

        AddSystemResult actual = systemRegister.addSystem(cmd.toSystemData());

        assertEquals(Change.ADDED, actual.result);
        assertTrue(actual.system.lastUpdated.compareTo(cmd.getTimestamp()) > 0);
        util.expectSystemToBeCorrect(cmd, actual.system);
    }

    @Test
    public void addSystem_duplicate_name_not_added() {
        String duplicateSystemName = "Duplicate Sys";
        SR_System seedSystem = util.an_sr_system.withName(duplicateSystemName).build();
        SystemRegister systemRegister = new SystemRegister(Arrays.asList(seedSystem));
        AddSystemCommand cmd_1 = util.n_add_system_command.withName(duplicateSystemName).build();

        AddSystemResult actual = systemRegister.addSystem(cmd_1.toSystemData());

        assertEquals(Change.DUPLICATE, actual.result);
        assertEquals(seedSystem.id, actual.system.id);
    }

    @Test
    public void addSystem_not_empty_register() {
        String existingSystemName = "existing system";
        String addedSystemName = "added system";
        SR_System seedSystem = util.an_sr_system.withName(existingSystemName).build();
        SystemRegister systemRegister = new SystemRegister(Arrays.asList(seedSystem));
        AddSystemCommand cmd_1 = util.n_add_system_command.withName(addedSystemName).build();

        AddSystemResult actual = systemRegister.addSystem(cmd_1.toSystemData());

        assertEquals(Change.ADDED, actual.result);
        assertNotEquals(seedSystem.id, actual.system.id);
    }

    @Test
    public void addTwoSystems() {
        String systemA = "system A";
        String systemB = "system B";
        SystemRegister systemRegister = new SystemRegister(new ArrayList<>());
        AddSystemCommand cmd_1 = util.n_add_system_command.withName(systemA).build();
        AddSystemCommand cmd_2 = util.n_add_system_command.withName(systemB).build();

        systemRegister.addSystem(cmd_1.toSystemData());
        systemRegister.addSystem(cmd_2.toSystemData());

        var actual = systemRegister.getAllSystems();

        assertThat(actual)
                .extracting("name")
                .containsExactlyInAnyOrder(systemA, systemB);
    }

    @Test
    void returnsExpectedSystem() {
        SR_System system = aSystem()
                .withId(789)
                .build();
        SystemRegister systemRegister = new SystemRegister(List.of(system));

        var actual = systemRegister.getSystemById(789);

        assertThat(actual).contains(system);
    }

    @Test
    void returnsEmptyOptionalIfIdDoesNotExist() {
        SR_System system = aSystem()
                .withId(789)
                .build();
        SystemRegister systemRegister = new SystemRegister(List.of(system));

        var actual = systemRegister.getSystemById(123);

        assertThat(actual).isEmpty();
    }

    // todo test to check copy system list , mutation safe
}
