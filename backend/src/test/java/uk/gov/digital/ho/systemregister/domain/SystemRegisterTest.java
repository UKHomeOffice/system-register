package uk.gov.digital.ho.systemregister.domain;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.anAddSystemCommand;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

public class SystemRegisterTest {
    @Test
    public void addSystem_toEmptyRegister() throws DuplicateSystemException {
        SystemRegister systemRegister = new SystemRegister(new ArrayList<>());
        AddSystemCommand command = anAddSystemCommand().build();

        SR_System addedSystem = systemRegister.addSystem(command.toSystemData());

        assertThat(addedSystem.lastUpdated).isAfterOrEqualTo(command.getTimestamp());
        assertThat(addedSystem).isEqualTo(command.toSystemData());
    }

    @Test
    public void raisesExceptionWhenDuplicateIsAdded() {
        String duplicateSystemName = "Duplicate Sys";
        SR_System seedSystem = aSystem().withName(duplicateSystemName).build();
        SystemRegister systemRegister = new SystemRegister(List.of(seedSystem));
        AddSystemCommand command = anAddSystemCommand().withName(duplicateSystemName).build();

        assertThatThrownBy(() -> systemRegister.addSystem(command.toSystemData()))
                .isInstanceOf(DuplicateSystemException.class);
    }

    @Test
    public void addSystem_not_empty_register() throws DuplicateSystemException {
        String existingSystemName = "existing system";
        String addedSystemName = "added system";
        SR_System seedSystem = aSystem().withName(existingSystemName).build();
        SystemRegister systemRegister = new SystemRegister(List.of(seedSystem));
        AddSystemCommand command = anAddSystemCommand().withName(addedSystemName).build();

        SR_System addedSystem = systemRegister.addSystem(command.toSystemData());

        assertNotEquals(seedSystem.id, addedSystem.id);
    }

    @Test
    public void addTwoSystems() throws DuplicateSystemException {
        String systemA = "system A";
        String systemB = "system B";
        SystemRegister systemRegister = new SystemRegister(new ArrayList<>());
        AddSystemCommand firstCommand = anAddSystemCommand().withName(systemA).build();
        AddSystemCommand secondCommand = anAddSystemCommand().withName(systemB).build();

        systemRegister.addSystem(firstCommand.toSystemData());
        systemRegister.addSystem(secondCommand.toSystemData());

        var systems = systemRegister.getAllSystems();

        assertThat(systems)
                .extracting("name")
                .containsExactlyInAnyOrder(systemA, systemB);
    }

    @Test
    void returnsExpectedSystem() {
        SR_System system = aSystem()
                .withId(789)
                .build();
        SystemRegister systemRegister = new SystemRegister(List.of(system));

        var retrievedSystem = systemRegister.getSystemById(789);

        assertThat(retrievedSystem).contains(system);
    }

    @Test
    void returnsEmptyOptionalIfIdDoesNotExist() {
        SR_System system = aSystem()
                .withId(789)
                .build();
        SystemRegister systemRegister = new SystemRegister(List.of(system));

        var retrievedSystem = systemRegister.getSystemById(123);

        assertThat(retrievedSystem).isEmpty();
    }

    // todo test to check copy system list , mutation safe
}
