package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemNameCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemNameUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemNameUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSystemNameCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final SystemNameUpdatedEventHandler eventHandler = mock(SystemNameUpdatedEventHandler.class);

    private UpdateSystemNameCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateSystemNameCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesSystemNameValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(123);
        givenCurrentStateWithSystem(partialSystem.withName("original system name"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("username2").build();
        var command = new UpdateSystemNameCommand(
                123,
                "updated system name",
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SystemNameUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withName("updated system name")
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new SystemNameUpdatedEvent(
                        123,
                        "updated system name",
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    public void raisesExceptionIfSystemWithSameNameAlreadyExists() {
        givenCurrentStateWithSystems(
                aSystem().withId(123).withName("existing system").build(),
                aSystem().withId(456).withName("system to be updated").build());
        var command = new UpdateSystemNameCommand(456, "existing system", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(SystemNameNotUniqueException.class)
                .hasMessageContaining("existing system");
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateSystemNameCommand(789, "system 1", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("789");
    }

    @Test
    void raisesExceptionIfSystemNameValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(345)
                .withName("original system name"));
        var command = new UpdateSystemNameCommand(345, "original system name", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system name is the same: original system name");
    }

    private void givenCurrentStateWithSystem(SR_SystemBuilder systemBuilder) {
        UpdateMetadata metadata = new UpdateMetadata(aPerson().withUsername("username1").build(), Instant.now());

        when(systemRegisterState.getCurrentState())
                .thenReturn(new CurrentState(
                        Map.of(systemBuilder.build(), metadata),
                        Instant.now()));
    }

    private void givenCurrentStateWithSystems(SR_System... existing_systems) {
        UpdateMetadata metadata = new UpdateMetadata(aPerson().withUsername("username1").build(), Instant.now());

        Map<SR_System, UpdateMetadata> sysMap = new HashMap<SR_System, UpdateMetadata>();
        for (SR_System sys : existing_systems) {
            sysMap.put(sys, metadata);
        }
        when(systemRegisterState.getCurrentState())
                .thenReturn(new CurrentState(sysMap, Instant.now()));
    }
}
