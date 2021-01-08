package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemDescriptionCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemDescriptionUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemDescriptionUpdatedEvent;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSystemDescriptionCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final SystemDescriptionUpdatedEventHandler eventHandler = mock(SystemDescriptionUpdatedEventHandler.class);

    private UpdateSystemDescriptionCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateSystemDescriptionCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesSystemDescriptionValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(234);
        givenCurrentStateWithSystem(partialSystem.withDescription("existing description"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("user").build();
        var command = new UpdateSystemDescriptionCommand(
                234,
                "new description",
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SystemDescriptionUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withDescription("new description")
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new SystemDescriptionUpdatedEvent(
                        234,
                        "new description",
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateSystemDescriptionCommand(678, "description", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("678");
    }

    @Test
    void raisesExceptionIfSystemDescriptionIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(456)
                .withDescription("description"));
        var command = new UpdateSystemDescriptionCommand(456, "description", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system description is the same: description");
    }

    private void givenCurrentStateWithSystem(SR_SystemBuilder systemBuilder) {
        UpdateMetadata metadata = new UpdateMetadata(aPerson().withUsername("username1").build(), Instant.now());

        when(systemRegisterState.getCurrentState())
                .thenReturn(new CurrentState(
                        Map.of(systemBuilder.build(), metadata),
                        Instant.now()));
    }
}
