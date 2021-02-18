package uk.gov.digital.ho.systemregister.application.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.AddSystemCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.SystemNameNotUniqueException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAddedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.helpers.FakeEventStore;
import uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddSystemCommandHandlerTest {
    private final SystemAddedEventHandler eventHandler = mock(SystemAddedEventHandler.class);
    private final AddSystemCommandBuilder addSystemCommandBuilder = new AddSystemCommandBuilder();
    private final IEventStore eventStore = new FakeEventStore();

    private AddSystemCommandHandler commandHandler;

    @BeforeEach
    public void setup() {
        var currentSystemRegisterState = new CurrentSystemRegisterState(eventStore, new CurrentStateCalculator());
        commandHandler = new AddSystemCommandHandler(currentSystemRegisterState, eventHandler);
    }

    @Test
    public void forwardsNewSystemToEventHandler() throws SystemNameNotUniqueException {
        AddSystemCommand command = addSystemCommandBuilder.build();

        commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SystemAddedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(eventCaptor.getValue())
                .hasFieldOrPropertyWithValue("author", command.author);
    }

    @Test
    public void forwardsNewSystemWithMinimalDataToEventHandler() throws SystemNameNotUniqueException {
        var justBeforeEventCreated = Instant.now();
        var command = addSystemCommandBuilder.withJustName().build();

        commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SystemAddedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        var event = eventCaptor.getValue();
        assertThat(event)
                .hasFieldOrPropertyWithValue("author", command.author)
                .hasFieldOrPropertyWithValue("system", command.systemData);
        assertThat(event.timestamp)
                .isAfterOrEqualTo(justBeforeEventCreated);
    }
}
