package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class SystemAddedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private SystemAddedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SystemAddedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new SystemAddedEvent(aSystem().build(), aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
