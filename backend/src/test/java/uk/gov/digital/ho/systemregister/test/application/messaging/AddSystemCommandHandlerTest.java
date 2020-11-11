package uk.gov.digital.ho.systemregister.test.application.messaging;

import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.MissingAuthorException;
import uk.gov.digital.ho.systemregister.application.messaging.AuthoredMessage;
import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
import uk.gov.digital.ho.systemregister.test.helpers.FakeEventStore;
import uk.gov.digital.ho.systemregister.test.helpers.builders.AddSystemCommandBuilder;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.AddSystemCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddSystemCommandHandlerTest {
    AddSystemCommandBuilder addSystemCommandBuilder = new AddSystemCommandBuilder();
    IEventStore eventStore;
    CurrentSystemRegisterState currentSystemRegisterState;

    SR_EventBus eventBus = new SR_EventBus();
    List<AuthoredMessage> recievedEvents = new ArrayList<>();

    @BeforeEach
    public void setup() {
        eventStore = new FakeEventStore();
        recievedEvents.clear();
        currentSystemRegisterState = new CurrentSystemRegisterState(eventStore);
        eventBus.subscribe(new Object() {
            @Subscribe
            public void handle(AuthoredMessage evt) {
                recievedEvents.add(evt);
            }
        });
    }

    @Test
    public void addSystem_EmptyRegister() throws MissingAuthorException {
        var sut = new AddSystemCommandHandler(eventBus, currentSystemRegisterState);
        var cmd = addSystemCommandBuilder.build();

        sut.handle(cmd);

        assertEquals(1, recievedEvents.size());
        assertEquals(SystemAddedEvent.class, recievedEvents.get(0).getClass());
        Assertions.assertEquals(cmd.author, recievedEvents.get(0).author);
    }

    @Test
    public void addSystem_withOnlyName_EmptyRegister() throws MissingAuthorException {
        var sut = new AddSystemCommandHandler(eventBus, currentSystemRegisterState);
        var cmd = addSystemCommandBuilder.withJustName().build();

        sut.handle(cmd);

        assertEquals(1, recievedEvents.size());
        assertEquals(SystemAddedEvent.class, recievedEvents.get(0).getClass());
        Assertions.assertEquals(cmd.author, recievedEvents.get(0).author);
        Assertions.assertEquals(cmd.systemData.name, ((SystemAddedEvent)recievedEvents.get(0)).system.name);
        assertTrue(recievedEvents.get(0).timestamp.compareTo(Instant.MIN) > 0);
    }
}