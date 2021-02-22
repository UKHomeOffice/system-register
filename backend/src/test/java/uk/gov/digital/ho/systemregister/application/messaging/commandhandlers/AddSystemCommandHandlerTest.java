package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.Command;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAddedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.FakeEventStore;
import uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.helpers.builders.AddSystemCommandBuilder.aMinimalAddSystemCommand;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;
import static uk.gov.digital.ho.systemregister.helpers.builders.SystemAddedEventBuilder.aSystemAddedEvent;

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
    void validatesCommand() throws NoSuchMethodException {
        Method handleMethod = commandHandler.getClass()
                .getMethod("handle", Command.class);
        Parameter commandArgument = handleMethod.getParameters()[0];

        boolean hasValidAnnotation = commandArgument.isAnnotationPresent(Valid.class);

        assertThat(hasValidAnnotation).isTrue();
    }

    @Test
    public void forwardsNewSystemToEventHandler() throws CommandProcessingException, NoSuchSystemException {
        AddSystemCommand command = addSystemCommandBuilder.build();

        var addedSystemAndMetadata = commandHandler.handle(command);
        SR_System system = addedSystemAndMetadata.getItem1();

        assertThat(system).usingRecursiveComparison()
                .ignoringFields("id", "lastUpdated")
                .isEqualTo(command.toSystemData());
        var eventCaptor = ArgumentCaptor.forClass(SystemAddedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(eventCaptor.getValue())
                .hasFieldOrPropertyWithValue("author", command.getAuthor());
    }

    @Test
    public void forwardsNewSystemWithMinimalDataToEventHandler() throws NoSuchSystemException, CommandProcessingException {
        var justBeforeEventCreated = Instant.now();
        var command = aMinimalAddSystemCommand().build();

        commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SystemAddedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        var event = eventCaptor.getValue();
        assertThat(event)
                .hasFieldOrPropertyWithValue("author", command.getAuthor())
                .hasFieldOrPropertyWithValue("system", command.toSystemData());
        assertThat(event.timestamp)
                .isAfterOrEqualTo(justBeforeEventCreated);
    }

    @Test
    void rejectsNewSystemsWithMatchingNames() {
        eventStore.save(aSystemAddedEvent()
                .withSystem(aSystem()
                        .withName("a name"))
                .build());

        assertThatThrownBy(() -> commandHandler.handle(addSystemCommandBuilder.withName("a name").build()))
                .isInstanceOf(SystemNameNotUniqueException.class);
    }
}
